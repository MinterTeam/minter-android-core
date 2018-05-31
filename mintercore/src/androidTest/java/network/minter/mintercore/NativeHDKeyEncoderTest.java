package network.minter.mintercore;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.crypto.PublicKey;
import network.minter.mintercore.internal.helpers.StringHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class NativeHDKeyEncoderTest {

    static {
        NativeBip39.init();
    }

    @Test
    public void testMakeBip39RootKey() {
        String seedString = "f01e96ba468700a7fa92b8fdf500b8d3cef5cd88e1592a83f31631e9c3f3ed86cffbaba747e2d3f00476b17f3c8b4c19f3f6577cf619464886402ce0faeef01c";
        byte[] seed = StringHelper.hexStringToBytes(seedString);

        assertEquals(64, seed.length);

        HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed);
        assertNotNull("HDKey is null", rootKey);
        assertNotNull("Priv key is null", rootKey.getPublicKeyBytes());
        assertNotNull("Pub key is null", rootKey.getPrivateKeyBytes());
        assertNotNull("Chain code is null", rootKey.getChainCodeBytes());
        assertNotNull("Ext priv key is null", rootKey.getExtPrivateKeyBytes());
        assertNotNull("Ext pub key is null", rootKey.getExtPublicKeyBytes());

        assertEquals(HDKey.PUB_KEY_LEN, rootKey.getPublicKeyBytes().length);
        assertEquals(HDKey.PRIV_KEY_LEN, rootKey.getPrivateKeyBytes().length);
        assertEquals(HDKey.CHAIN_CODE_LEN, rootKey.getChainCodeBytes().length);
        assertEquals(HDKey.EXT_PUB_KEY_LEN, rootKey.getExtPublicKeyBytes().length);
        assertEquals(HDKey.EXT_PRIV_KEY_LEN, rootKey.getExtPrivateKeyBytes().length);


        String extPrivKeyString = rootKey.getExtPrivateKeyString();
        assertEquals(
                "xprv9s21ZrQH143K2Pr9zz5gPaxJHrJj1YR5As1SA2z6D5a9yTkN9nhUMt2Z1CJxFfSe8VzxmGYeeuVi26Uim7papujvs4hf5dwauQFrqgEU7Nf",
                extPrivKeyString
        );

        PrivateKey privateKey = rootKey.getPrivateKey();
        PublicKey publicKeyFromPrivate = privateKey.getPublicKey(true);
        PublicKey publicKey = rootKey.getPublicKey();

        assertEquals(32, privateKey.size());
        assertEquals(33, publicKey.size());

        MinterAddress address = publicKey.toMinter();
        assertNotNull(address);
        assertEquals(20, address.size());
        assertEquals(publicKey, publicKeyFromPrivate);
        assertTrue(privateKey.verify());

    }


    @Test
    public void testMakeBipExtendedKey() {
        String seedString = "f01e96ba468700a7fa92b8fdf500b8d3cef5cd88e1592a83f31631e9c3f3ed86cffbaba747e2d3f00476b17f3c8b4c19f3f6577cf619464886402ce0faeef01c";
        byte[] seed = StringHelper.hexStringToBytes(seedString);

        assertEquals(64, seed.length);

        HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed);
        assertNotNull(rootKey);

        HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey);

        // check for private and public exists
        PrivateKey privateKey = extKey.getPrivateKey();
        PublicKey publicKeyFromPrivate = privateKey.getPublicKey(true);
        PublicKey publicKey = extKey.getPublicKey();

        assertEquals(32, privateKey.size());
        assertEquals(33, publicKey.size());

        MinterAddress address = publicKey.toMinter();
        assertNotNull(address);
        assertEquals(20, address.size());
        assertEquals(publicKey, publicKeyFromPrivate);
        assertTrue(privateKey.verify());

        final String b44ExtPriv = "xprv9zPRRprz3mGyL7YLgAFT1PoJ787ZZroCHxPpVdEhTaxsLh1uowZyX8cGMdbrmibV9bXBNMUtA6TGePGQrw5tWaM4VFFwwqFo52xTL8EXzZH";
        final String b44ExtPub = "xpub6DNmqLPst8qGYbconBnTNXk2f9x3yKX3fBKRJ1eK1vVrDVM4MUtE4vvkCw6N6Zj5YYTQB9G723vkNHaxEA7acuM5J2qH7QSs1ryRJ8Mb8kF";

        assertEquals(
                b44ExtPriv,
                extKey.getExtPrivateKeyString()
        );

        assertEquals(
                b44ExtPub,
                extKey.getExtPublicKeyString()
        );
    }

}
