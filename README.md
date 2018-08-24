Minter Android Core SDK
==========
[![Download](https://api.bintray.com/packages/minterteam/android/minter-android-core-testnet/images/download.svg?version=0.1.0) ](https://bintray.com/minterteam/android/minter-android-core-testnet/0.1.0/link)


Minter core sdk library, contains signin and common helpers
-------

## Setup

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
    minterSdkVersion = "0.1.0"
}

dependencies {
    // for testnet use suffix "-testnet"
    implementation "network.minter.android:minter-android-core-testnet:${minterSdkVersion}"
    
    // for main net
    implementation "network.minter.android:minter-android-core:${minterSdkVersion}"
}
```

## Initialize it
```java

MinterSDK.initialize();
```

## Docs
TODO (tests and javadocs available for now)

# Build
TODO (ndk required)