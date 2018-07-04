#
# MinterCore. 2018
#
# @author Eduard Maximovich <edward.vstock@gmail.com>
# native library secp256k1 and jni bindings
#

cmake_minimum_required(VERSION 3.4)
project(secp256k1 LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 14)

include(AndroidDetermine.cmake)
include(CheckTypeSize)
include_directories(src)

check_type_size("unsigned __int128" HAVE_BIGNUM LANGUAGE CXX)

set(COMMON_COMPILE_FLAGS ENABLE_MODULE_RECOVERY ENABLE_MODULE_ECDH USE_ECMULT_STATIC_PRECOMPUTATION USE_FIELD_INV_BUILTIN USE_NUM_NONE USE_SCALAR_INV_BUILTIN)
if (MSVC)
	set(COMPILE_FLAGS USE_FIELD_10X26 USE_SCALAR_8X32)
	set(COMPILE_OPTIONS "")
else ()
	#  -DUSE_ECMULT_STATIC_PRECOMPUTATION -DUSE_FIELD_5X52 -DUSE_SCALAR_4X64
	add_definitions(-DHAVE_BUILTIN_EXPECT -DUSE_FIELD_10X26 -DUSE_SCALAR_8X32)
	if (HAVE_BIGNUM)
		add_definitions(-DHAVE___INT128)
	endif ()
	set(COMPILE_OPTIONS -O3 -W -Wno-narrowing -Wall -Wextra -Wcast-align -Wnested-externs -Wshadow -Wstrict-prototypes -Wno-unused-function -Wno-long-long -Wno-overlength-strings -fvisibility=hidden)
endif ()

set(all_sources
    src/basic-config.h
    src/ecdsa.h
    src/ecdsa_impl.h
    src/eckey.h
    src/eckey_impl.h
    src/ecmult.h
    src/ecmult_const.h
    src/ecmult_const_impl.h
    src/ecmult_gen.h
    src/ecmult_gen_impl.h
    src/ecmult_impl.h
    src/field.h
    src/field_5x52.h
    src/field_5x52_impl.h
    src/field_5x52_int128_impl.h
    src/field_10x26.h
    src/field_10x26_impl.h
    src/field_impl.h
    src/group.h
    src/group_impl.h
    src/hash.h
    src/hash_impl.h
    src/num.h
    src/num_gmp.h
    src/num_gmp_impl.h
    src/num_impl.h
    src/scalar.h
    src/scalar_4x64.h
    src/scalar_4x64_impl.h
    src/scalar_8x32.h
    src/scalar_8x32_impl.h
    src/scalar_impl.h
    src/scalar_low.h
    src/scalar_low_impl.h
    src/scratch.h
    src/scratch_impl.h
    src/modules/ecdh/main_impl.h
    src/modules/recovery/main_impl.h
    src/secp256k1.cpp
    include/secp256k1.h
    include/secp256k1_ecdh.h
    include/secp256k1_recovery.h
    src/util.h
    src/ecmult_static_context.h
    )

set(bignum_cxx uint128_t/uint128_t.h uint128_t/uint128_t.cpp)
set(main_c_sources ${all_sources})
set(secp256k1_jni_sources src/java/NativeSecp256k1.cpp src/java/NativeSecp256k1.cpp)

# Context generator
set(GENERATOR_BIN_PATH ${CMAKE_BINARY_DIR}/src)
set(GENERATED_PATH ${CMAKE_ROOT_SOURCE_DIR}/src)
set(GENERATED_CONTEXT_PATH "src/ecmult_static_context.h")
message(STATUS "Context generator bin path: ${GENERATOR_BIN_PATH}")

if (ENABLE_GENERATE_CONTEXT)
	add_executable(gen_context
	               src/gen_context.cpp
	               src/ecmult.h
	               src/ecmult_gen.h
	               src/ecmult_gen_impl.h
	               src/ecmult_const.h
	               src/ecmult_const_impl.h
	               )
	target_include_directories(gen_context PRIVATE ${CMAKE_SOURCE_DIR})
	message(STATUS "Generate static context in ${GENERATED_CONTEXT_PATH}")

	add_custom_target(ecmult_static_context gen_context WORKING_DIRECTORY ${CMAKE_SOURCE_DIR})
	set(main_c_sources
	    ${main_c_sources}
	    ${GENERATED_CONTEXT_PATH}
	    )
endif ()

# Main lib
if (NOT HAVE_BIGNUM)
	set(main_c_sources
	    ${main_c_sources}
	    ${bignum_cxx}
	    )
endif ()
add_library(secp256k1_core STATIC ${main_c_sources})

target_compile_definitions(secp256k1_core PRIVATE ${COMMON_COMPILE_FLAGS} ${COMPILE_FLAGS})
target_include_directories(secp256k1_core PRIVATE ${CMAKE_SOURCE_DIR} ${CMAKE_SOURCE_DIR}/src)
target_compile_options(secp256k1_core PRIVATE ${COMPILE_OPTIONS})


# JNI
add_library(secp256k1_jni SHARED ${secp256k1_jni_sources})
target_link_libraries(secp256k1_jni secp256k1_core)
target_include_directories(secp256k1_jni PRIVATE ${CMAKE_SOURCE_DIR} ${CMAKE_SOURCE_DIR}/src)

if (ENABLE_GENERATE_CONTEXT)
	add_dependencies(secp256k1_core ecmult_static_context)
endif ()

# TODO - generate jni sources with build-time namespaces