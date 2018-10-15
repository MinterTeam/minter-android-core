# Release notes

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