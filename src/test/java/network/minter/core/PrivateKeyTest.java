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

import java.util.Arrays;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.PublicKey;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * MinterWallet. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class PrivateKeyTest {

    static {
        MinterSDK.initialize();
    }

    @Test
    public void testCreateFromMnemonic() {
        String mnemonic = "guide rescue bike august sunny mutual void such beyond angle adapt settle";
        PrivateKey outPk = new PrivateKey("78f7fe0189d8b1b5e16e4cb7225eb4dda747161d5919ac7c71f3a8a4ab2028fa");
        PrivateKey pk = PrivateKey.fromMnemonic(mnemonic);
        MinterAddress address = new MinterAddress("Mx92a1dd593e1a844ddcba964bd1fd822d3e761a0b");

        assertEquals(outPk.toHexString(), pk.toHexString());
        assertEquals(outPk, pk);
        assertEquals(address.toString(), outPk.getPublicKey().toMinter().toString());
    }

    @Test
    public void testVerifyPos() {
        PrivateKey pk = new PrivateKey("098e910195e8dc28075c000fa8a5dd811adf705e72f3ac62996049712da1801e");
        assertTrue(pk.verify());
    }

    @Test
    public void testExtractPublicKey() {
        final PublicKey publicKey = new PublicKey("Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e");
        final PrivateKey privateKey = new PrivateKey(
                "098e910195e8dc28075c000fa8a5dd811adf705e72f3ac62996049712da1801e");
        final MinterAddress extracted = privateKey.getPublicKey().toMinter();

        assertEquals(publicKey, extracted);
        assertTrue(Arrays.equals(publicKey.getData(), extracted.getData()));
        assertEquals("Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e", extracted.toString());
    }

}
