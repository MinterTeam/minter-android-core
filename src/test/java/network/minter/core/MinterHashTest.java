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

import org.junit.Test;

import network.minter.core.crypto.MinterHash;

import static org.junit.Assert.assertEquals;

/**
 * minter-android-core. 2020
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterHashTest {

    @Test
    public void testUppercaseMinterHash() {
        String hash = "MT23990E490762593E5ABD87CB43B10ACDD47215A5B35E2800FF3B1BDBC8D943E9";
        MinterHash res = new MinterHash(hash);

        assertEquals(32, res.size());
        assertEquals((char) 0x23, res.getData()[0]);
        assertEquals((char) 0xe9, res.getData()[31]);
        assertEquals("Mt23990e490762593e5abd87cb43b10acdd47215a5b35e2800ff3b1bdbc8d943e9", res.toString());
    }
}
