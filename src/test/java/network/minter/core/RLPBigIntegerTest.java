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

import java.math.BigInteger;

import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static network.minter.core.internal.helpers.StringHelper.charsToHexString;
import static network.minter.core.internal.helpers.StringHelper.charsToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RLPBigIntegerTest {
	private final static BigInteger VALUE_MUL = new BigInteger("1000000000000000000", 10);

	@Test
	public void resultBeforeAndAfterAreEquals_10() {
		final String amount = "10";
		final BigInteger val = new BigInteger(amount).multiply(VALUE_MUL);
		final byte[] encoded = RLP.encode(val);
		final DecodeResult decodedResult = RLP.decode(encoded, 0);
		final byte[] decoded = ((byte[]) decodedResult.getDecoded());
		final byte[] decodedFixed = new byte[decoded.length + 1];
		decodedFixed[0] = 0x00;// bigint here only unsigned, so, adding zero
		System.arraycopy(decoded, 0, decodedFixed, 1, decoded.length);
		final BigInteger invalidVal = new BigInteger(decoded);
		final BigInteger validVal = new BigInteger(decodedFixed);

		assertNotEquals(val, invalidVal); // fails! because rlp drops first zero from bigint (sign mask)
		assertEquals(val, validVal); // success
		assertEquals(val, fixBigintSignedByte(invalidVal)); // success
	}

	@Test
	public void resultBeforeAndAfterAreEquals_9() {
		final String amount = "9";
		final BigInteger val = new BigInteger(amount).multiply(VALUE_MUL);
		final byte[] encoded = RLP.encode(val);
		final DecodeResult decodedResult = RLP.decode(encoded, 0);
		final byte[] decoded = ((byte[]) decodedResult.getDecoded());
		final BigInteger decodedVal = new BigInteger(decoded);

		assertEquals(val, decodedVal); // success!
	}

	@Test
	public void resultBeforeAndAfterAreEquals_2362() {
		final String amount = "2362";
		final BigInteger val = new BigInteger(amount).multiply(VALUE_MUL);
		final byte[] encoded = RLP.encode(val);
		final DecodeResult decodedResult = RLP.decode(encoded, 0);
		final byte[] decoded = ((byte[]) decodedResult.getDecoded());
		final BigInteger decodedVal = fixBigintSignedByte(decoded);

		assertEquals(val, decodedVal); // success!
	}

	@Test
	public void resultManyValuesAreEqualsBeforeAndAfter()
			throws Throwable {

		for (int i = 0; i < 10000; i++) {
			Throwable e = null;
			try {
				final String amount = String.valueOf(i);
				final BigInteger val = new BigInteger(amount).multiply(VALUE_MUL);
				final byte[] encoded = RLP.encode(val);
				final DecodeResult decodedResult = RLP.decode(encoded, 0);
				final byte[] decoded;
				if (decodedResult.getDecoded() instanceof String) {
					decoded = new BigInteger("0").toByteArray();
				} else {
					decoded = ((byte[]) decodedResult.getDecoded());
				}
				final BigInteger decodedVal = fixBigintSignedByte(decoded);

				assertEquals(val, decodedVal); // success!
			} catch (Throwable t) {
				e = t;
			}

			if (e != null) {
				System.out.println(String.format("Error occurred in iteration: %d, error: %s", i, e.getMessage()));
				throw e;
			}
		}
	}

    @Test
    public void testRLPObjectArr() {
        Object[] v1 = new Object[0];
        assertEquals("", charsToString(v1));

        Object[] v2 = new Object[]{'a'};
        assertEquals("a", charsToString(v2));

        char[] v3c = new char[]{'h', 'e', 'l', 'l', 'o'};
        Object[] v3 = new Object[v3c.length];
        {
            for (int i = 0; i < v3c.length; i++) {
                v3[i] = v3c[i];
            }
        }
        assertEquals("hello", charsToString(v3));
    }

    @Test
    public void testRLPDecodeEmptyObjectArray() {
        UnsignedBytesData encoded = new UnsignedBytesData("0xc1c0");
        DecodeResult tmp = RLPBoxed.decode(encoded.getData(), 0);
        Object[] res = ((Object[]) tmp.getDecoded());

        String t1 = charsToString(fromRawRlp(0, res));
        assertEquals("", t1);
    }

    @Test
    public void testRLPEncodeEmptyList() {
        UnsignedBytesData data = new UnsignedBytesData(new char[0]);
        char[] encoded = RLPBoxed.encode(new Object[]{data.getData()});
        String encodedHex = charsToHexString(encoded);

        assertEquals("c180", encodedHex);

        DecodeResult tmp = RLPBoxed.decode(encoded, 0);
        Object[] res = ((Object[]) tmp.getDecoded());

        String t1 = charsToString(fromRawRlp(0, res));
        assertEquals("", t1);
    }

    char[] fromRawRlp(int idx, Object[] raw) {
        if (raw[idx] instanceof String) {
            return ((String) raw[idx]).toCharArray();
        }
        return (char[]) raw[idx];
    }

	@Test
    public void testRLPEncodeDecode0_512() {
        for (int i = 0; i <= 0xFFFFFF; i++) {
			BigInteger val = BigInteger.valueOf(i);


            char[] encoded = RLPBoxed.encode(new Object[]{val, ""});
            DecodeResult res = RLPBoxed.decode(encoded, 0);
			Object[] dec = (Object[]) res.getDecoded();
			BigInteger bg;
			try {
                bg = fixBigintSignedByte(dec[0]);
			} catch (Throwable t) {
				System.err.println("Failed on number: " + i);
				throw t;
			}

			assertEquals("Failed on number: " + i, BigInteger.valueOf(i), bg);
			assertTrue(BigInteger.valueOf(i).equals(bg));

			String pl = (String) dec[1];
			assertEquals("", pl);
		}
	}

	@Test
	public void testUnsignedRLPEncodeDecode128() {
		for (int i = 1; i <= 0xFFFFFF; i++) {
			BigInteger val = BigInteger.valueOf(i);

			char[] encoded = RLPBoxed.encode(new Object[]{val});
			DecodeResult res = RLPBoxed.decode(encoded, 0);
			Object[] dec = (Object[]) res.getDecoded();
			BigInteger bg;
			try {
                bg = fixBigintSignedByte(dec[0]);
			} catch (Throwable t) {
				System.err.println("Failed on number: " + i);
				throw t;
			}

			assertEquals("Failed on number: " + i, BigInteger.valueOf(i), bg);
			assertTrue(BigInteger.valueOf(i).equals(bg));
		}
	}

	@Test
	public void testSome() {
		BigInteger nonce = new BigInteger("128");
		char[] encoded = RLPBoxed.encode(new Object[]{nonce});
		UnsignedBytesData bd = new UnsignedBytesData(encoded);

		assertEquals("c28180", bd.toHexString());
	}

    @Test
    public void testZeroNum() {
        BigInteger nonce = new BigInteger("0");
        char[] encoded = RLPBoxed.encode(new Object[]{nonce});
        UnsignedBytesData bd = new UnsignedBytesData(encoded);

        DecodeResult decoded = RLPBoxed.decode(encoded, 0);
        Object[] dec = (Object[]) decoded.getDecoded();

        BigInteger res = fixBigintSignedByte(dec[0]);
        assertEquals(BigInteger.ZERO, res);

        assertEquals("c180", bd.toHexString());
    }

    @Test
    public void test128Num() {
        BigInteger nonce = new BigInteger("128");
        char[] encoded = RLPBoxed.encode(new Object[]{nonce});
        UnsignedBytesData bd = new UnsignedBytesData(encoded);

        DecodeResult decoded = RLPBoxed.decode(encoded, 0);
        Object[] dec = (Object[]) decoded.getDecoded();

        BigInteger res = fixBigintSignedByte(dec[0]);
        assertEquals(nonce, res);
    }

}