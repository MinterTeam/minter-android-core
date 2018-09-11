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

import network.minter.core.crypto.MinterCheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock[at]gmail.com]
 */
public class MinterCheckTest {

    @Test
    public void testCreateValidCheck() {
        final String src = "Mcf8a7887b5bad595e2390008272748a4d4e54000000000000008906aafb55d011d28000b84140b4257e2e82d87ccb97e9db6ccaa4b797bf75bcd19efda0ea37a761844c4bf12487186cbb8811cc06b1d78e32a8c2e87b80c82e9cec0147ad6a3a78bf1865b7011ca0f92bc0fd3ff277bedfd65ac330446c7c71cda4b2c953be3d6a4335f0c4444b2ca017715c246f7d49b3bf36c2acaadc673c4c0504e98694b1a2f1852ef9f49ce8e6";
        final String srcShort = "Mcf8a788...9ce8e6";
        Throwable t = null;
        MinterCheck check = null;
        try {
            check = new MinterCheck(src);
        } catch (Throwable e) {
            t = e;
        }

        assertNull(t);
        assertNotNull(check);

        assertEquals(169, check.size());
        assertEquals(src, check.toString());
        assertEquals(srcShort, check.toShortString());
    }
}
