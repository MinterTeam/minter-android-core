/*
 * Copyright (C) by MinterTeam. 2019
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

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock[at]gmail.com]
 */
public class MinterAddressTest {

    @Test
    public void testMinterAddressFromStringValidString() {
        final String addressSrc = "MxAAAA1111BBBB2222CCCC3333DDDD4444FFFF0000";

        // valid string
        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(t0);
        assertNotNull(address);
    }

    @Test
    public void testFirstZeroBytesAddressRLP() {
        MinterAddress address = new MinterAddress("Mx00aacae635ad329dc2a3f0509f947065703efc79");

        char[] enc = RLPBoxed.encode(new Object[]{address});
        DecodeResult res = RLPBoxed.decode(enc, 0);
        Object[] dec = (Object[]) res.getDecoded();
        char[] add = (char[]) dec[0];
        UnsignedBytesData bd = new UnsignedBytesData(add);
        System.out.println(bd.toHexString());
        assertEquals(20, add.length);

    }

    @Test
    public void testFirstZeroBytesPubKeyRLP() {
        MinterPublicKey pk = new MinterPublicKey("Mp009b5528f09d1c74a83d18414f2e4263e14850c47a3fac3f855f200111111111");
        char[] enc = RLPBoxed.encode(new Object[]{pk});
        DecodeResult res = RLPBoxed.decode(enc, 0);
        Object[] dec = (Object[]) res.getDecoded();
        char[] add = (char[]) dec[0];
        UnsignedBytesData bd = new UnsignedBytesData(add);
        System.out.println(bd.toHexString());
        assertEquals(32, add.length);
    }

    @Test
    public void testMinterAddressFromStringValidStringNoPrefix() {
        final String addressSrc = "AAAA1111BBBB2222CCCC3333DDDD4444FFFF0000";

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(t0);
        assertNotNull(address);
    }

    @Test
    public void testMinterAddressFromStringInvalidHex() {
        final String addressSrc = "MxZZZZ1111BBBB2222CCCC3333DDDD4444FFFF0000";

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(address);
        assertNotNull(t0);
    }

    @Test
    public void testMinterAddressFromStringTooLong() {
        final String addressSrc = "MxAAAA1111BBBB2222CCCC3333DDDD4444FFFF0000EEEE";

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(address);
        assertNotNull(t0);
    }

    @Test
    public void testMinterAddressFromStringTooShort() {
        final String addressSrc = "MxAAAA1111BBBB2222CCCC3333DDDD4444FFFF";

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(address);
        assertNotNull(t0);
    }

    @Test
    public void testMinterAddressFromStringNulValue() {
        final String addressSrc = null;

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(address);
        assertNotNull(t0);
    }

    @Test
    public void testMinterAddressFromBytesValidBytes() {
        final byte[] addressSrc = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00
        };

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(t0);
        assertNotNull(address);
        assertEquals("Mx0000000000000000000000000000000000000000", address.toString());
        assertEquals("Mx000000...000000", address.toShortString());
        assertEquals(20, address.size());
        assertTrue(address.equals("Mx0000000000000000000000000000000000000000"));
        assertFalse(address.equals("Mx000000...000000"));
        assertEquals(address, addressSrc);
    }

    @Test
    public void testMinterAddressFromBytesTooLong() {
        final byte[] addressSrc = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
        };

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(address);
        assertNotNull(t0);
    }

    @Test
    public void testMinterAddressFromBytesTooShort() {
        final byte[] addressSrc = new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
        };

        MinterAddress address = null;
        Throwable t0 = null;
        try {
            address = new MinterAddress(addressSrc);
        } catch (Throwable t) {
            t0 = t;
        }
        assertNull(address);
        assertNotNull(t0);
    }
}
