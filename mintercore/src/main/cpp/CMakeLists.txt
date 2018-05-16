project(mintercore-bindings)
cmake_minimum_required(VERSION 3.4)

include_directories(secp256k1)
include_directories(bip39)

add_subdirectory(bip39)
add_subdirectory(secp256k1)