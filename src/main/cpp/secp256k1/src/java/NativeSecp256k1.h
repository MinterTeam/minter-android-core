#include <jni.h>
#include "include/secp256k1.h"
/* Header for class network.minter.mintercore.crypto.NativeSecp256k1 */

#ifndef _Included_network_minter_mintercore_cryptoNativeSecp256k1
#define _Included_network_minter_mintercore_cryptoNativeSecp256k1
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     network_minter_mintercore_cryptoSecp256k1Context
 * Method:    secp256k1_init_context
 * Signature: ()J
 */

JNIEXPORT jlong
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1init_1context(JNIEnv *, jclass);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ctx_clone
 * Signature: (J)J
 */
JNIEXPORT jlong
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ctx_1clone
    (JNIEnv *, jclass, jlong);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_context_randomize
 * Signature: (Ljava/nio/ByteBuffer;J)I
 */
JNIEXPORT jint
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1context_1randomize
    (JNIEnv *, jclass, jobject, jlong);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_privkey_tweak_add
 * Signature: (Ljava/nio/ByteBuffer;J)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1privkey_1tweak_1add
    (JNIEnv *, jclass, jobject, jlong);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_privkey_tweak_mul
 * Signature: (Ljava/nio/ByteBuffer;J)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1privkey_1tweak_1mul
    (JNIEnv *, jclass, jobject, jlong);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_pubkey_tweak_add
 * Signature: (Ljava/nio/ByteBuffer;JI)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1pubkey_1tweak_1add
    (JNIEnv *, jclass, jobject, jlong, jint);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_pubkey_tweak_mul
 * Signature: (Ljava/nio/ByteBuffer;JI)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1pubkey_1tweak_1mul
    (JNIEnv *, jclass, jobject, jlong, jint);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_destroy_context
 * Signature: (J)V
 */
JNIEXPORT void JNICALL
Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1destroy_1context
(JNIEnv * , jclass , jlong ) ;

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ecdsa_verify
 * Signature: (Ljava/nio/ByteBuffer;JII)I
 */
JNIEXPORT jint
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1verify
    (JNIEnv * , jclass, jobject, jlong, jint, jint);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ecdsa_sign
 * Signature: (Ljava/nio/ByteBuffer;J)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1sign
    (JNIEnv * , jclass, jobject, jlong);

JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdsa_1sign_1recoverable_1serialized
    (JNIEnv * , jclass, jobject, jlong);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ec_seckey_verify
 * Signature: (Ljava/nio/ByteBuffer;J)I
 */
JNIEXPORT jint
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ec_1seckey_1verify
    (JNIEnv * , jclass, jobject, jlong);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ec_pubkey_create
 * Signature: (Ljava/nio/ByteBuffer;J)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ec_1pubkey_1create
    (JNIEnv * , jclass, jobject, jlong, jboolean);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ec_pubkey_parse
 * Signature: (Ljava/nio/ByteBuffer;JI)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ec_1pubkey_1parse
    (JNIEnv * , jclass, jobject, jlong, jint);

/*
 * Class:     network_minter_mintercore_cryptoNativeSecp256k1
 * Method:    secp256k1_ecdh
 * Signature: (Ljava/nio/ByteBuffer;JI)[[B
 */
JNIEXPORT jobjectArray
JNICALL Java_network_minter_mintercore_crypto_NativeSecp256k1_secp256k1_1ecdh
    (JNIEnv * env, jclass
classObject , jobject byteBufferObject, jlong
ctx_l , jint publen ) ;

#ifdef __cplusplus
}
#endif
#endif
