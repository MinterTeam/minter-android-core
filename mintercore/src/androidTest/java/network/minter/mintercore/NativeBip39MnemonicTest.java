package network.minter.mintercore;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import network.minter.mintercore.bip39.MnemonicResult;
import network.minter.mintercore.bip39.NativeBip39;
import network.minter.mintercore.internal.helpers.StringHelper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * <p>
 * <p>
 * * entropy: f0b9c942b9060af6a82d3ac340284d7e
 * * words: vague soft expose improve gaze kitten pass point select access battle wish
 * * bip39seed: f01e96ba468700a7fa92b8fdf500b8d3cef5cd88e1592a83f31631e9c3f3ed86cffbaba747e2d3f00476b17f3c8b4c19f3f6577cf619464886402ce0faeef01c
 * * bip32root key: xprv9s21ZrQH143K2Pr9zz5gPaxJHrJj1YR5As1SA2z6D5a9yTkN9nhUMt2Z1CJxFfSe8VzxmGYeeuVi26Uim7papujvs4hf5dwauQFrqgEU7Nf
 * *
 * * bip44:
 * * network: 36 (ETH)
 * * purpose: 44
 * * coin: 60
 * * accout: 0
 * * external/internal: 0/1
 * *
 * * derivation path: m/44'/60'/0'/0
 * * acc ext. private: xprv9zPRRprz3mGyL7YLgAFT1PoJ787ZZroCHxPpVdEhTaxsLh1uowZyX8cGMdbrmibV9bXBNMUtA6TGePGQrw5tWaM4VFFwwqFo52xTL8EXzZH
 * * acc ext. public:  xpub6DNmqLPst8qGYbconBnTNXk2f9x3yKX3fBKRJ1eK1vVrDVM4MUtE4vvkCw6N6Zj5YYTQB9G723vkNHaxEA7acuM5J2qH7QSs1ryRJ8Mb8kF
 */
@RunWith(AndroidJUnit4.class)
public class NativeBip39MnemonicTest {

    static {
        NativeBip39.init();
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
}
