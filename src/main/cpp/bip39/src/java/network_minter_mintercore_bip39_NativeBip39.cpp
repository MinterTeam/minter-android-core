#include <jni.h>
#include <minter/Bip39Mnemonic.h>
#include "network_minter_mintercore_bip39_NativeBip39.h"

/**
 * bip39. 2018
 * NativeBip39.cpp
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * @link https://github.com/edwardstock
 */

jobjectArray Java_network_minter_mintercore_bip39_NativeBip39_bip39GetLanguages(JNIEnv *env, jclass) {
    const auto langs = minter::Bip39Mnemonic::getLanguages();

    jobjectArray
        langArr = env->NewObjectArray(static_cast<jsize>(langs.size()), env->FindClass("java/lang/String"), NULL);
    for (int i = 0; i < langs.size(); i++) {
        env->SetObjectArrayElement(langArr, i, env->NewStringUTF(langs[i]));
    }

    return langArr;
}

jobjectArray Java_network_minter_mintercore_bip39_NativeBip39_bip39GetWordsFromLanguage(
    JNIEnv *env, jclass, jstring language_) {
    const char *language = env->GetStringUTFChars(language_, 0);

    const std::vector<const char *> words = minter::Bip39Mnemonic::getWordsFromLanguage(language);

    jobjectArray
        wordsArr = env->NewObjectArray(static_cast<jsize>(words.size()), env->FindClass("java/lang/String"), NULL);
    for (size_t i = 0; i < words.size(); i++) {
        jstring s = env->NewStringUTF(words[i]);
        env->SetObjectArrayElement(wordsArr, static_cast<jsize>(i), s);
        env->DeleteLocalRef(s);
    }

    env->ReleaseStringUTFChars(language_, language);
    return wordsArr;
}
jboolean Java_network_minter_mintercore_bip39_NativeBip39_bip39ValidateMnemonic(
    JNIEnv *env,
    jclass,
    jstring mnemonic_,
    jstring language_) {
    const char *mnemonic = env->GetStringUTFChars(mnemonic_, 0);
    const char *language = env->GetStringUTFChars(language_, 0);

    bool res = minter::Bip39Mnemonic::validateWords(language, mnemonic);

    env->ReleaseStringUTFChars(mnemonic_, mnemonic);
    env->ReleaseStringUTFChars(language_, language);
    return static_cast<jboolean>(res ? 1 : 0);
}

jobject Java_network_minter_mintercore_bip39_NativeBip39_bip39EncodeBytes(
    JNIEnv *env, jclass, jobject input, jstring language_, jint entropy) {

    uint8_t *buffer = (uint8_t *) env->GetDirectBufferAddress(input);
    const char *language = env->GetStringUTFChars(language_, 0);

    const minter::Bip39Mnemonic::MnemonicResult out = minter::Bip39Mnemonic::encodeBytes(
        buffer, language, static_cast<size_t>(entropy)
    );
    env->ReleaseStringUTFChars(language_, language);

    jclass mrClass = env->FindClass("network/minter/mintercore/bip39/MnemonicResult");
    jobject mrObj = env->AllocObject(mrClass);
    env->SetIntField(mrObj, env->GetFieldID(mrClass, "status", "I"), out.status);
    env->SetIntField(mrObj, env->GetFieldID(mrClass, "len", "I"), static_cast<jint>(out.len));
    env->SetObjectField(
        mrObj,
        env->GetFieldID(mrClass, "words", "Ljava/lang/String;"),
        env->NewStringUTF(out.raw.c_str())
    );

    return mrObj;
}

jbyteArray Java_network_minter_mintercore_bip39_NativeBip39_bip39WordsToSeed(
    JNIEnv *env, jclass, jstring mnemonic_) {
    const char *mnemonic = env->GetStringUTFChars(mnemonic_, 0);

    minter::Data tmp(64);
    size_t written;
    minter::Bip39Mnemonic::wordsToSeed(mnemonic, tmp.data(), &written);

    env->ReleaseStringUTFChars(mnemonic_, mnemonic);

    jbyteArray out = env->NewByteArray(64);
    env->SetByteArrayRegion(out, 0, 64, reinterpret_cast<const jbyte *>(tmp.cdata()));

    return out;
}

jint Java_network_minter_mintercore_bip39_NativeBip39_countOps(JNIEnv *env, jclass type) {
    minter::Data s("f0b9c942b9060af6a82d3ac340284d7e");
    uint64_t opsCount = 10'000'000ull;

    // record start time
    auto start = std::chrono::high_resolution_clock::now();
    minter::Data next;
    for (auto size = 1ull; size < opsCount; size++) {
        // do some work
        {
            minter::FixedData<64> out;
            CSHA256 sh;

            if(size == 1) {
                sh.Write(s.data(), s.size());
            } else {
                sh.Write(next.data(), next.size());
            }

            sh.Finalize(out.data());
            next = out;
        }
    }

    auto finish = std::chrono::high_resolution_clock::now();

    std::stringstream ss;

    ss << std::fixed << "10'000'000 operations per "
       << std::chrono::duration_cast<std::chrono::seconds>(finish - start).count() << "s "
       << std::chrono::duration_cast<std::chrono::milliseconds>(finish - start).count() << "ms "
       << std::chrono::duration_cast<std::chrono::microseconds>(finish - start).count() << "mcs "
       << std::chrono::duration_cast<std::chrono::nanoseconds>(finish - start).count() << "ns "
       << "\nAVG speed: " << (((double)opsCount) / ((double)std::chrono::duration_cast<std::chrono::seconds>(finish - start).count())) << " ops/s\n"
       << std::endl;

    const char *logout = ss.str().c_str();
//    LOGD("OPS: ", "");
    __android_log_print(ANDROID_LOG_DEBUG, "OPS: ", "%s", logout);
    return 0;
}

