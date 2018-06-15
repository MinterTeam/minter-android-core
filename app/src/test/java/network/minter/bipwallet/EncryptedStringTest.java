/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
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

package network.minter.bipwallet;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import network.minter.mintercore.crypto.EncryptedString;
import network.minter.mintercore.crypto.HashUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public class EncryptedStringTest {

    @Test
    public void testEncrypting() throws NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String mnemonic = "globe arrange forget twice potato nurse ice dwarf arctic piano scorpion tube";
        String rawPassword = "123456";
        String IV = "pjSfpWAjdSaYpOBy";
        String hexPassword = HashUtil.sha256Hex(rawPassword);
        EncryptedString enc = new EncryptedString(mnemonic, hexPassword.substring(0, 32), IV);
        assertEquals(
                "b5e32fcc8c54fa00c42242cd8be2ce2b1d23d80113c1ba60b8f7ff2fd809b092b564249dd7feb4cb8fb65486412402d81cd4d17e67e2276a66b6b10773b71914706c2748a769847ec7d8817ab57f9f63",
                enc.getEncrypted()
        );
    }

    @Test
    public void testDecrypting() throws NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String mnemonic = "globe arrange forget twice potato nurse ice dwarf arctic piano scorpion tube";
        String rawPassword = "123456";
        String IV = "pjSfpWAjdSaYpOBy";
        String hexPassword = HashUtil.sha256Hex(rawPassword);
        EncryptedString enc = new EncryptedString(mnemonic, hexPassword.substring(0, 32), IV);
        String encrypted = "b5e32fcc8c54fa00c42242cd8be2ce2b1d23d80113c1ba60b8f7ff2fd809b092b564249dd7feb4cb8fb65486412402d81cd4d17e67e2276a66b6b10773b71914706c2748a769847ec7d8817ab57f9f63";
        assertEquals(
                encrypted,
                enc.getEncrypted()
        );


        String decrypted = enc.decrypt(hexPassword, IV);
        assertNotNull(decrypted);
        assertEquals(mnemonic, decrypted);
    }
}
