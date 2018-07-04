#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <include/secp256k1_recovery.h>
#include "NativeSecp256k1.h"
#include "include/secp256k1.h"
#include "include/secp256k1_ecdh.h"

JNIEXPORT jlong
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1init_1context(JNIEnv *env,
                                                                                       jclass
                                                                                       classObject) {
    secp256k1_context *ctx = secp256k1_context_create(SECP256K1_CONTEXT_SIGN | SECP256K1_CONTEXT_VERIFY);

    (void)
        classObject;
    (void)
        env;

    return (uintptr_t)
        ctx;
}

JNIEXPORT jlong
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ctx_1clone
    (JNIEnv *env, jclass
    classObject,
     jlong ctx_l
    ) {
    const secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;

    jlong ctx_clone_l = (uintptr_t) secp256k1_context_clone(ctx);

    (void)
        classObject;
    (void)
        env;

    return
        ctx_clone_l;

}

JNIEXPORT jint
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1context_1randomize
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;

    const unsigned char *seed = (unsigned char *) env->GetDirectBufferAddress(byteBufferObject);

    (void)
        classObject;

    return
        secp256k1_context_randomize(ctx, seed
        );

}

JNIEXPORT void JNICALL
Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1destroy_1context
    (JNIEnv
     *, jclass,
     jlong ctx_l
    ) {

    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    secp256k1_context_destroy(ctx);
}

JNIEXPORT jint
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1verify
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l,
     jint siglen, jint
     publen) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;

    unsigned char *data = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);
    const unsigned char *sigdata = {(data + 32)};
    const unsigned char *pubdata = {(data + siglen + 32)};

    secp256k1_ecdsa_signature sig;
    secp256k1_pubkey pubkey;

    int ret = secp256k1_ecdsa_signature_parse_der(ctx, &sig, sigdata, (size_t) siglen);

    if (ret) {
        ret = secp256k1_ec_pubkey_parse(ctx, &pubkey, pubdata, (size_t) publen);

        if (ret) {
            ret = secp256k1_ecdsa_verify(ctx, &sig, data, &pubkey);
        }
    }

    return
        ret;
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1sign(
    JNIEnv *env, jclass
classObject,
    jobject byteBufferObject, jlong
    ctx_l
) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    unsigned char *data = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);
    unsigned char *secKey = (unsigned char *) (data + 32);

    jobjectArray retArray;
    jbyteArray sigArray, intsByteArray;
    unsigned char intsarray[2];

    secp256k1_ecdsa_signature sig[72];

    int ret = secp256k1_ecdsa_sign(ctx, sig, data, secKey, NULL, NULL);

    unsigned char outputSer[72];
    size_t outputLen = 72;

    if (ret) {
        secp256k1_ecdsa_signature_serialize_der(ctx, outputSer, &outputLen, sig
        );
    }

    intsarray[0] =
        outputLen;
    intsarray[1] =
        ret;

    retArray = (env)->NewObjectArray(2, (env)->FindClass("[B"), (env)->NewByteArray(1));

    sigArray = (env)->NewByteArray(outputLen);
    (env)->
             SetByteArrayRegion(sigArray,
                                0, outputLen, (jbyte *) outputSer);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, sigArray);

    intsByteArray = (env)->NewByteArray(2);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 2, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    return
        retArray;
}

jobjectArray Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1sign_1recoverable_1serialized(
    JNIEnv *env, jclass
classObject,
    jobject byteBufferObject, jlong
    ctx_l) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;

    unsigned char *buffer = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);
    unsigned char data[32];
    memset(data,
           0, 32);
    memmove(data, buffer,
            32);
    unsigned char *secKey = buffer + 32;

    secp256k1_ecdsa_recoverable_signature sig;

    int ret = secp256k1_ecdsa_sign_recoverable(ctx, &sig, data, secKey, NULL, NULL);
    memset(secKey,
           0, 32);
    memset(data,
           0, 32);

    unsigned char outputSer[65];
    int recoveryId = 0;

    if (ret) {
        int serializeRet = secp256k1_ecdsa_recoverable_signature_serialize_compact(ctx, outputSer, &recoveryId, &sig);
        outputSer[64] = ((uint8_t) recoveryId) + (uint8_t) 27;
    } else {
        return NULL;
    }

    jbyteArray rOut = env->NewByteArray(32);
    jbyteArray sOut = env->NewByteArray(32);
    jbyteArray vOut = env->NewByteArray(1);

    jbyte r[32], s[32], v[1];
    memcpy(r, outputSer
        + 0, 32);
    memcpy(s, outputSer
        + 32, 32);
    v[0] = outputSer[64];
    memset(outputSer,
           0, 65);

    env->
           SetByteArrayRegion(rOut,
                              0, 32, r);
    env->
           SetByteArrayRegion(sOut,
                              0, 32, s);
    env->
           SetByteArrayRegion(vOut,
                              0, 1, v);

    jobjectArray retArray = (env)->NewObjectArray(3, (env)->FindClass("[B"), NULL);
    env->
           SetObjectArrayElement(retArray,
                                 0, rOut);
    env->
           SetObjectArrayElement(retArray,
                                 1, sOut);
    env->
           SetObjectArrayElement(retArray,
                                 2, vOut);

    return
        retArray;
}

JNIEXPORT jint
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ec_1seckey_1verify
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l) {

    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    unsigned char *secKey = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);

    return
        secp256k1_ec_seckey_verify(ctx, secKey
        );
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ec_1pubkey_1create
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l,
     jboolean compressed
    ) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    const unsigned char *secKey = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);

    secp256k1_pubkey pubkey;

    jobjectArray retArray;
    jbyteArray pubkeyArray, intsByteArray;
    unsigned char intsarray[2];

    int ret = secp256k1_ec_pubkey_create(ctx, &pubkey, secKey);

    unsigned char outputSer[65];
    memset(outputSer,
           0, 65);
    size_t outputLen = 65;

    unsigned int compFlag = compressed == ((uint8_t) 1) ? SECP256K1_EC_COMPRESSED : SECP256K1_EC_UNCOMPRESSED;

    if (ret) {
        int ret2 = secp256k1_ec_pubkey_serialize(ctx, outputSer, &outputLen, &pubkey, compFlag);
    }

    intsarray[0] =
        outputLen;
    intsarray[1] =
        ret;

    retArray = (env)->NewObjectArray(2, (env)->FindClass("[B"), (env)->NewByteArray(1));

    pubkeyArray = (env)->NewByteArray(outputLen);
    (env)->
             SetByteArrayRegion(pubkeyArray,
                                0, outputLen, (jbyte *) outputSer);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, pubkeyArray);

    intsByteArray = (env)->NewByteArray(2);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 2, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    return
        retArray;
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1privkey_1tweak_1add
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    unsigned char *privkey = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);
    const unsigned char *tweak = (unsigned char *) (privkey + 32);

    jobjectArray retArray;
    jbyteArray privArray, intsByteArray;
    unsigned char intsarray[2];

    int privkeylen = 32;

    int ret = secp256k1_ec_privkey_tweak_add(ctx, privkey, tweak);

    intsarray[0] =
        privkeylen;
    intsarray[1] =
        ret;

    retArray = (env)->NewObjectArray(2, (env)->FindClass("[B"), (env)->NewByteArray(1));

    privArray = (env)->NewByteArray(privkeylen);
    (env)->
             SetByteArrayRegion(privArray,
                                0, privkeylen, (jbyte *) privkey);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, privArray);

    intsByteArray = (env)->NewByteArray(2);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 2, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    return
        retArray;
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1privkey_1tweak_1mul
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    unsigned char *privkey = (unsigned char *) (env)->GetDirectBufferAddress(byteBufferObject);
    const unsigned char *tweak = (unsigned char *) (privkey + 32);

    jobjectArray retArray;
    jbyteArray privArray, intsByteArray;
    unsigned char intsarray[2];

    int privkeylen = 32;

    int ret = secp256k1_ec_privkey_tweak_mul(ctx, privkey, tweak);

    intsarray[0] =
        privkeylen;
    intsarray[1] =
        ret;

    retArray = (env)->NewObjectArray(2, (env)->FindClass("[B"), (env)->NewByteArray(1));

    privArray = (env)->NewByteArray(privkeylen);
    (env)->
             SetByteArrayRegion(privArray,
                                0, privkeylen, (jbyte *) privkey);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, privArray);

    intsByteArray = (env)->NewByteArray(2);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 2, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    return
        retArray;
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1pubkey_1tweak_1add
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l,
     jint publen
    ) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
/*  secp256k1_pubkey* pubkey = (secp256k1_pubkey*) (*env)->GetDirectBufferAddress(byteBufferObject);*/
    unsigned char *pkey = (unsigned char *) ((env)->GetDirectBufferAddress(byteBufferObject));
    const unsigned char *tweak = (unsigned char *) (pkey + publen);

    jobjectArray retArray;
    jbyteArray pubArray, intsByteArray;
    unsigned char intsarray[2];
    unsigned char outputSer[65];
    size_t outputLen = 65;

    secp256k1_pubkey pubkey;
    int ret = secp256k1_ec_pubkey_parse(ctx, &pubkey, pkey, publen);

    if (ret) {
        ret = secp256k1_ec_pubkey_tweak_add(ctx, &pubkey, tweak);
    }

    if (ret) {
        int ret2 = secp256k1_ec_pubkey_serialize(ctx, outputSer, &outputLen, &pubkey, SECP256K1_EC_UNCOMPRESSED);
        (void)
            ret2;
    }

    intsarray[0] =
        outputLen;
    intsarray[1] =
        ret;

    retArray = (env)->NewObjectArray(2,
                                     (env)->FindClass("[B"),
                                     (env)->NewByteArray(1));

    pubArray = (env)->NewByteArray(outputLen);
    (env)->
             SetByteArrayRegion(pubArray,
                                0, outputLen, (jbyte *) outputSer);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, pubArray);

    intsByteArray = (env)->NewByteArray(2);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 2, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    (void)
        classObject;

    return
        retArray;
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1pubkey_1tweak_1mul
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l,
     jint publen
    ) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    unsigned char *pkey = static_cast<unsigned char *>((env)->GetDirectBufferAddress(byteBufferObject));
    const unsigned char *tweak = pkey + publen;

    jobjectArray retArray;
    jbyteArray pubArray, intsByteArray;
    unsigned char intsarray[2];
    unsigned char outputSer[65];
    size_t outputLen = 65;

    secp256k1_pubkey pubkey;
    int ret = secp256k1_ec_pubkey_parse(ctx, &pubkey, pkey, publen);

    if (ret) {
        ret = secp256k1_ec_pubkey_tweak_mul(ctx, &pubkey, tweak);
    }

    if (ret) {
        int ret2 = secp256k1_ec_pubkey_serialize(ctx, outputSer, &outputLen, &pubkey, SECP256K1_EC_UNCOMPRESSED);
        (void)
            ret2;
    }

    intsarray[0] =
        outputLen;
    intsarray[1] =
        ret;

    retArray = (env)->NewObjectArray(2, (env)->FindClass("[B"), (env)->NewByteArray(1));

    pubArray = (env)->NewByteArray(outputLen);
    (env)->
             SetByteArrayRegion(pubArray,
                                0, outputLen, (jbyte *) outputSer);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, pubArray);

    intsByteArray = (env)->NewByteArray(2);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 2, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    (void)
        classObject;

    return
        retArray;
}

JNIEXPORT jlong
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1pubkey_1combine
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l,
     jint numkeys
    ) {
    (void)
        classObject;
    (void)
        env;
    (void)
        byteBufferObject;
    (void)
        ctx_l;
    (void)
        numkeys;

    return 0;
}

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdh
    (JNIEnv *env, jclass
    classObject,
     jobject byteBufferObject, jlong
     ctx_l,
     jint publen
    ) {
    secp256k1_context *ctx = (secp256k1_context *) (uintptr_t) ctx_l;
    const unsigned char *secdata = static_cast<unsigned char *>((env)->GetDirectBufferAddress(byteBufferObject));
    const unsigned char *pubdata = secdata + 32;

    jobjectArray retArray;
    jbyteArray outArray, intsByteArray;
    unsigned char intsarray[1];
    secp256k1_pubkey pubkey;
    unsigned char nonce_res[32];
    size_t outputLen = 32;

    int ret = secp256k1_ec_pubkey_parse(ctx, &pubkey, pubdata, publen);

    if (ret) {
        ret = secp256k1_ecdh(
            ctx,
            nonce_res,
            &pubkey,
            secdata
        );
    }

    intsarray[0] =
        ret;

    retArray = (env)->NewObjectArray(2,
                                     (env)->FindClass("[B"),
                                     (env)->NewByteArray(1));

    outArray = (env)->NewByteArray(outputLen);
    (env)->
             SetByteArrayRegion(outArray,
                                0, 32, (jbyte *) nonce_res);
    (env)->
             SetObjectArrayElement(retArray,
                                   0, outArray);

    intsByteArray = (env)->NewByteArray(1);
    (env)->
             SetByteArrayRegion(intsByteArray,
                                0, 1, (jbyte *) intsarray);
    (env)->
             SetObjectArrayElement(retArray,
                                   1, intsByteArray);

    (void)
        classObject;

    return
        retArray;
}
