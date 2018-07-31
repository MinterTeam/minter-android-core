/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
 * @link https://github.com/edwardstock
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

import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
}