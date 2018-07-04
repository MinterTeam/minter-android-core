/**
 * bip39. 2018
 * main.cpp
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * @link https://github.com/edwardstock
 */

#include <vector>
#include <iostream>
#include <sstream>
#include <iomanip>
#include "minter/utils.h"
#include "minter/Bip39Mnemonic.h"
#include <minter/HDKeyEncoder.h>

#include <chrono>

using namespace minter;

int main(int argc, char **argv) {
    /*
     * entropy: f0b9c942b9060af6a82d3ac340284d7e
     * words: vague soft expose improve gaze kitten pass point select access battle wish
     * bip39seed: f01e96ba468700a7fa92b8fdf500b8d3cef5cd88e1592a83f31631e9c3f3ed86cffbaba747e2d3f00476b17f3c8b4c19f3f6577cf619464886402ce0faeef01c
     * bip32root key: xprv9s21ZrQH143K2Pr9zz5gPaxJHrJj1YR5As1SA2z6D5a9yTkN9nhUMt2Z1CJxFfSe8VzxmGYeeuVi26Uim7papujvs4hf5dwauQFrqgEU7Nf
     *
     * bip44:
     * network: 36 (ETH)
     * purpose: 44
     * coin: 60
     * accout: 0
     * external/internal: 0/1
     *
     * derivation path: m/44'/60'/0'/0
     * acc ext. private: xprv9zPRRprz3mGyL7YLgAFT1PoJ787ZZroCHxPpVdEhTaxsLh1uowZyX8cGMdbrmibV9bXBNMUtA6TGePGQrw5tWaM4VFFwwqFo52xTL8EXzZH
     * acc ext. public:  xpub6DNmqLPst8qGYbconBnTNXk2f9x3yKX3fBKRJ1eK1vVrDVM4MUtE4vvkCw6N6Zj5YYTQB9G723vkNHaxEA7acuM5J2qH7QSs1ryRJ8Mb8kF
     *
     *
     *
     */

    /*
     * bench

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

    ss << std::fixed << "10'000'000ull operations per "
       << std::chrono::duration_cast<std::chrono::seconds>(finish - start).count() << "s "
       << std::chrono::duration_cast<std::chrono::milliseconds>(finish - start).count() << "ms "
       << std::chrono::duration_cast<std::chrono::microseconds>(finish - start).count() << "mcs "
       << std::chrono::duration_cast<std::chrono::nanoseconds>(finish - start).count() << "ns "
        << "\nAVG speed: " << (((double)opsCount) / (((double)std::chrono::duration_cast<std::chrono::nanoseconds>(finish - start).count())/1e-9)) << " ops/s\n"
       << std::endl;

    std::cout << ss.str();

    // NO ASM 10'000'000 operations per 19s 19948ms 19948179mcs 19948179000ns
    // ASM    10'000'000 operations per 20s 20552ms 20552206mcs 20552206000ns

     */

    Data64 entropy("f0b9c942b9060af6a82d3ac340284d7e");
    Bip39Mnemonic::MnemonicResult
        encodedMnemonic = Bip39Mnemonic::encodeBytes(entropy.data(), "en", BIP39_ENTROPY_LEN_128);

    HDKey bip32RootKey = HDKeyEncoder::makeBip32RootKey(HDKeyEncoder::makeBip39Seed(encodedMnemonic.words));
    HDKey bip32ExtKey = HDKeyEncoder::makeExtendedKey(bip32RootKey, "m/44'/60'/0'/0");
    HDKey bip44ExtKey = HDKeyEncoder::makeExtendedKey(bip32RootKey, "m/44'/60'/0'");

    std::cout << "Mnemonic words count:    " << encodedMnemonic.len << std::endl;
    std::cout << "Mnemonic words:          " << encodedMnemonic.raw << std::endl;
    std::cout << "Bip32 root key:          " << bip32RootKey.extPrivateKey.toString() << std::endl;
    std::cout << "Bip32 extended priv key: " << bip32ExtKey.extPrivateKey.toString() << std::endl;
    std::cout << "Bip32 extended pub key:  " << bip32ExtKey.extPublicKey.toString() << std::endl;
    std::cout << "Bip44 priv key:          " << bip44ExtKey.privateKey.toHex() << std::endl;
    std::cout << "Bip44 pub key:           " << bip44ExtKey.publicKey.toHex() << std::endl;
    std::cout << "Bip44 extended priv key: " << bip44ExtKey.extPrivateKey.toString() << std::endl;
    std::cout << "Bip44 extended pub key:  " << bip44ExtKey.extPublicKey.toString() << std::endl;
    std::cout << "address:                 " << HDKeyEncoder::getAddress(bip44ExtKey) << std::endl;
    std::cout << "bip32 seed to words:     " << Bip39Mnemonic::decodeMnemonic(bip32RootKey.extPrivateKey.toString().c_str(), "en") << std::endl;

    bip32RootKey.clear();
    bip32ExtKey.clear();
    bip44ExtKey.clear();

    return 0;
}
 
 