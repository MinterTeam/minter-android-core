# Release notes

## 0.7.0
 - Refactored chars buffer
 - Removed old deserializers
 - Deleted old BytesData (with byte[] backend), renamed UnsignedBytesData to BytesData - now backend is char[]

## 0.6.3
 - Deprecated old-style Gson converters, pay attention on deprecated notices

## 0.6.2
 - Added support for **UnsignedBytesData** to RLP encoding
 - Fixed major issue: UnsignedBytesData#equals() on the same length and different data, returned true instead of false

## 0.6.1
 - Interpret RLP empty object[] as empty char[]

## 0.6.0
 - Fixed encoding zero value in RLP

## 0.5.0
 - Fixed bytes comparison if both arrays have zero length
 - Fixed RLP encoding empty string with fixed bytes length
 - Now SDK uses androidx instead of legacy support libraries

## 0.4.0
 - Updated BIP39 library
 - Updated secp256k1 library - fixed NPE after fail signing

## 0.3.2
 - Fixed RLP encoding for raw bytes contains leading zeroes

## 0.3.1
 - Checking for existence `android.os*` class in `ApiService`

## 0.3.0
 - Created Unsigned byte buffer to deal with RLP single byte (value 128) encoding as `{128}` than as `{0, -127}`. Backend for it: `char` type instead of byte.
 - Created RLP for working with `char[]` values 
 - Updated native libraries to latest version (changed projects structure)

## 0.2.6
 - Added helper function for dropping leading zero bytes

## 0.2.5
 - Enable TLS v1.1 and v1.2 an android 4.4

## 0.2.4
 - BigIntegerDeserializer now handles empty strings as zero value

## 0.2.3
 - Added `removeHttpInterceptor` to `ApiService` client, for easy http request mocking
 - Added `java.io.EOFException` as friend exception to `NetworkException`. Also added `IOException` and `IOError`

## 0.2.2
 - Hotfix: logger Mint.tag() worked wrong with NPE. Fixed with dummy object on uninitialized logger.

## 0.2.1
 - MinterHash fix, length can be more than 20 bytes
 - Added ability to pass `byte[][]` or `byte[]...` to `BytesData` (to combine multiple arrays like from RecoverableSignature)
 - Added **sha256** hash method to `BytesData`

## 0.2.0
 - **BREAKING CHANGES**:
    - Removed UriDeserializer
    - Replaced `android.util.Pair` with custom class. Android's `Pair` uses `java.util.Objects` and it's not supported on android < 19
    - Added checked `NativeLoadException` for `MinterSDK.initialize()`
 - Reduced android dependencies usage
 - Added random generate method to create random mnemonic (using native PCG random)
 - `CallbackProvider<T>` now deprecated. Use `Lazy<T>`
 - Added `Retrofit.Builder` configuration callback in `ApiService.Builder`
 - Added unified logger: see `Mint`
 - Added public SONAME and arrays of native lib file names

## 0.1.3
 - Added ability to create private key from mnemonic phrase directly
 - Added more constructors to private key
 - Moved tests from instrumentation to unit (now, native lib required to be installed in system)

## 0.1.2
 - Added minter "check" bytes model
 - More tests

## 0.1.1
 - Readme additions
 - Target api 28