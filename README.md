Minter Android Core SDK
==========
[![Download](https://api.bintray.com/packages/minterteam/android/minter-android-core-testnet/images/download.svg) ](https://bintray.com/minterteam/android/minter-android-core-testnet/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


Minter core sdk library, contains transaction signing and common helpers
------------------------------------------------------------------------

## Setup

**The library contains JNI bindings, so you need to install android NDK if it's not yet. In future, all native code will be moved to dependencies.**

Gradle 
root build.gradle
```groovy
allprojects {
    repositories {
       // ... some repos
        maven { url "https://dl.bintray.com/minterteam/android" }
    }
}
```

project build.gradle 
```groovy

ext {
    minterSdkVersion = "0.7.1"
}

dependencies {
    // for testnet use suffix "-testnet"
    implementation "network.minter.android:minter-android-core-testnet:${minterSdkVersion}"
    
    // for main net
    implementation "network.minter.android:minter-android-core:${minterSdkVersion}"
}
```

## Basic Usage
### Initialize it
```java
try {
    MinterSDK.initialize();
} catch (NativeLoadException e) {
    // handle linkage error
}
```

### Usage
See [tests](src/androidTest/java/network/minter/core)

## Docs
TODO (javadocs available for now)

# Build
TODO (NDK required)