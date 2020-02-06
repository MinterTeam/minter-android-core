/*
 * Copyright (C) by MinterTeam. 2020
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.core;

import com.edwardstock.bip3x.HDKey;
import com.edwardstock.bip3x.MnemonicResult;
import com.edwardstock.bip3x.NativeBip39;
import com.edwardstock.bip3x.NativeHDKeyEncoder;

import org.junit.Test;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.PublicKey;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.internal.helpers.StringHelper;

import static com.edwardstock.bip3x.NativeHDKeyEncoder.MAIN_NET;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * MinterWallet. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 *         <p>
 *         <p>
 *         * entropy: f0b9c942b9060af6a82d3ac340284d7e
 *         * words: vague soft expose improve gaze kitten pass point select access battle wish
 *         * bip39seed: f01e96ba468700a7fa92b8fdf500b8d3cef5cd88e1592a83f31631e9c3f3ed86cffbaba747e2d3f00476b17f3c8b4c19f3f6577cf619464886402ce0faeef01c
 *         * bip32root key: xprv9s21ZrQH143K2Pr9zz5gPaxJHrJj1YR5As1SA2z6D5a9yTkN9nhUMt2Z1CJxFfSe8VzxmGYeeuVi26Uim7papujvs4hf5dwauQFrqgEU7Nf
 *         *
 *         * bip44:
 *         * network: 36 (ETH)
 *         * purpose: 44
 *         * coin: 60
 *         * accout: 0
 *         * external/internal: 0/1
 *         *
 *         * derivation path: m/44'/60'/0'/0
 *         * acc ext. private: xprv9zPRRprz3mGyL7YLgAFT1PoJ787ZZroCHxPpVdEhTaxsLh1uowZyX8cGMdbrmibV9bXBNMUtA6TGePGQrw5tWaM4VFFwwqFo52xTL8EXzZH
 *         * acc ext. public:  xpub6DNmqLPst8qGYbconBnTNXk2f9x3yKX3fBKRJ1eK1vVrDVM4MUtE4vvkCw6N6Zj5YYTQB9G723vkNHaxEA7acuM5J2qH7QSs1ryRJ8Mb8kF
 */
public class NativeBip39MnemonicTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetLanguages() {
        String[] langs = NativeBip39.getLanguages();
        assertNotNull(langs);
        assertEquals(7, langs.length);
    }

    @Test
    public void testGetEnglishWords() {
        String[] words = NativeBip39.getWordsFromLanguage(NativeBip39.LANG_DEFAULT);
        assertEquals(2048, words.length);
        assertEquals("abandon", words[0]);
        assertEquals("zoo", words[words.length - 1]);
    }

    @Test
    public void testEncodeEntropy() {
        // 16 bytes
        byte[] entropy = StringHelper.hexStringToBytes("f0b9c942b9060af6a82d3ac340284d7e");
        assertEquals(16, entropy.length);

        MnemonicResult res = NativeBip39.encodeBytes(entropy, NativeBip39.LANG_DEFAULT, NativeBip39.ENTROPY_LEN_128);
        assertEquals(12, res.size());
        assertEquals("vague soft expose improve gaze kitten pass point select access battle wish", res.getMnemonic());
        assertTrue(res.getStatus(), res.isOk());
    }

    @Test
    public void testValidateMnemonicWords() {
        final String mnemonic = "vague soft expose improve gaze kitten pass point select access battle wish";
        assertTrue(NativeBip39.validateMnemonic(mnemonic, NativeBip39.LANG_DEFAULT));
        assertFalse(NativeBip39.validateMnemonic(mnemonic, "it"));
        assertFalse(NativeBip39.validateMnemonic(
                "unknown unknown unknown unknown unknown unknown unknown unknown unknown unknown unknown unknown",
                "en"));
    }

    @Test
    public void testMakeBip39Seed() {
        final String mnemonic = "vague soft expose improve gaze kitten pass point select access battle wish";
        byte[] seed = NativeBip39.mnemonicToBip39Seed(mnemonic);
        assertNotNull(seed);
        assertEquals(64, seed.length);
        String seedHex = StringHelper.bytesToHexString(seed);
        assertEquals(
                "Given seed: " + seedHex,
                "f01e96ba468700a7fa92b8fdf500b8d3cef5cd88e1592a83f31631e9c3f3ed86cffbaba747e2d3f00476b17f3c8b4c19f3f6577cf619464886402ce0faeef01c",
                seedHex
        );
    }

    @Test
    public void testGenerateMnemonic() {
        final MnemonicResult res = NativeBip39.generate();
        assertNotNull(res.getMnemonic());
        assertEquals(12, res.size());
        assertTrue(res.getStatus(), res.isOk());
    }

    @Test
    public void testBip39SeedToNikita() {
        final String mnemonic = "globe arrange forget twice potato nurse ice dwarf arctic piano scorpion tube";
        final String validSeed = "e4d6956689cfba26aab4156e5f4600f5f386cdd56ea57f1bfdb40f33048d4d463989c170738ae582f07227af9fd707b40cc7d7d6fd24d14a4ee1e0f45928940d";

        final byte[] seed = NativeBip39.mnemonicToBip39Seed(mnemonic);
        String seedHex = StringHelper.bytesToHexString(seed);

        assertEquals(
                "Given seed: " + seedHex,
                validSeed,
                seedHex
        );

        // bip 32 root key    with derivation path: m/44'/60'/0'/0
        final HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed);

        // bip 39 private key with derivation path: m/44'/60'/0' - dropped external/internal index
        final HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey, MAIN_NET, "m/44'/60'/0'/0");
        final PrivateKey privateKey = new PrivateKey(extKey.getPrivateKeyBytes());
        // to get minter address, we should always use uncompressed key
        final PublicKey publicKey = privateKey.getPublicKey(false);
        final MinterAddress outAddress = publicKey.toMinter();
        System.out.println("address: " + outAddress.toString());


    }

    @Test
    public void testGetAddress() {
        final String mnemonic = "exercise fantasy smooth enough arrive steak demise donkey true employ jealous decide blossom bind someone";
        final byte[] seed = NativeBip39.mnemonicToBip39Seed(mnemonic);
        // bip 32 root key    with derivation path: m/44'/60'/0'/0
        final HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed);
        // bip 39 private key with derivation path: m/44'/60'/0' - dropped external/internal index
        final HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey, MAIN_NET, "m/44'/60'/0'/0/0");
        final PrivateKey privateKey = new PrivateKey(extKey.getPrivateKeyBytes());
        // to get minter address, we should always use uncompressed key
        final PublicKey publicKey = privateKey.getPublicKey(false);
        final MinterAddress outAddress = publicKey.toMinter();

        System.out.println("ADDRESS: " + outAddress.toString());
    }

    @Test
    public void testGetMyAddress() {
        final String mnemonic = "royal park wage travel execute focus brother click twin stove drift margin";
        final MinterAddress validAddress = new MinterAddress("Mxb503f13cD20cB3e42D87b20fAD869c564cc4EF8c");

        final byte[] seed = NativeBip39.mnemonicToBip39Seed(mnemonic);
        // bip 32 root key
        final HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed);
        // bip 39 private key with derivation path: m/44'/60'/0'/0/0
        final HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey);
        final PrivateKey privateKey = new PrivateKey(extKey.getPrivateKeyBytes());
        // to get minter address, we should always use uncompressed key
        final PublicKey publicKey = privateKey.getPublicKey(false);
        final MinterAddress outAddress = publicKey.toMinter();

        assertEquals(validAddress, outAddress);

    }

    @Test
    public void testBip39SeedToAddress() {
        final String mnemonic = "royal park wage travel execute focus brother click twin stove drift margin";
        final MinterAddress address = new MinterAddress("Mxb503f13cD20cB3e42D87b20fAD869c564cc4EF8c");
        final byte[] seed = NativeBip39.mnemonicToBip39Seed(mnemonic);
        assertNotNull(seed);
        assertEquals(64, seed.length);
        String seedHex = StringHelper.bytesToHexString(seed);
        assertEquals(
                "Given seed: " + seedHex,
                "f88392faf3c460b05bbd50278362d12b19dc8e0cf2ebd5c97cc9ce01b5db819c7364f4d72d3f2ef85c96328bcc49763ad31bf546f8d0e56009524fe3b9b01502",
                seedHex
        );

        // bip 32 root key    with derivation path: m/44'/60'/0'/0
        final HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed);
        // bip 39 private key with derivation path: m/44'/60'/0' - dropped external/internal index
        final HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey);
        final PrivateKey privateKey = new PrivateKey(extKey.getPrivateKeyBytes());
        // to get minter address, we should always use uncompressed key
        final PublicKey publicKey = privateKey.getPublicKey(false);
        final MinterAddress outAddress = publicKey.toMinter();

        assertEquals(address, outAddress);

    }
}
