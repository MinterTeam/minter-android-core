/*
 * Copyright (C) by MinterTeam. 2018
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

import org.junit.Test;

import network.minter.core.bip39.HDKey;
import network.minter.core.bip39.NativeBip39;
import network.minter.core.bip39.NativeHDKeyEncoder;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.PublicKey;
import network.minter.core.internal.helpers.StringHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * MinterWallet. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class NativeHDKeyEncoderTest {

    static {
        MinterSDK.initialize();
    }

    @Test
    public void testMakeBip39RootKey() {
        NativeBip39.init();
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

        final String b44ExtPriv = "xprvA41WfdCkwqbHFi9MTEJrJ1RxPhZXgYzVUkpzXNht4RtQ7V3K1t8QkCTNDEGNxHnck4cN9J4A6bxj3vG6FYQK5gQuSEkiRFV9VLaaiiytAyJ";
        final String b44ExtPub = "xpub6Gzs58jenD9aUCDpZFqrf9NgwjQ261iLqykbKm7VcmRNzHNTZRSfHzmr4Wv5ffJyBAQD8noAsTR6xkJbspPwv9UXimt9HhJPKymZwWDZGWD";

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
