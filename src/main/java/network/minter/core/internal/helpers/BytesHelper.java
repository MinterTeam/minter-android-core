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

package network.minter.core.internal.helpers;

import java.math.BigInteger;

import network.minter.core.util.DecodeResult;

import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesHelper {
    public static byte[] lpad(int size, byte[] input) {
        if (input.length == size) {
            return input;
        }

        if (input.length > size) {
            final byte[] out = new byte[size];
            System.arraycopy(input, 0, out, 0, size);
            return out;
        }

        int offset = size - input.length;
        byte[] out = new byte[size];

        for (int i = 0, s = 0; i < size; i++) {
            if (i < offset) {
                out[i] = (byte) 0;
            } else {
                out[i] = input[s++];
            }
        }

        return out;
    }

    public static byte[] dropFirstZeroes(byte[] input) {
        int targetLen = input.length;
        int i = 0;
        while (input[i] == 0x00) {
            i++;
            targetLen--;
        }

        byte[] target = new byte[targetLen];
        for (int c = i, n = 0; c < input.length; c++, n++) {
            target[n] = input[c];
        }

        return target;
    }

    public static BigInteger fixBigintSignedByte(BigInteger input) {
        return fixBigintSignedByte(input.toByteArray());
    }

    public static BigInteger fixBigintSignedByte(DecodeResult input) {
        checkNotNull(input);
        return fixBigintSignedByte(((byte[]) input.getDecoded()));
    }

    public static BigInteger fixBigintSignedByte(byte[] bigintBytes) {
        if (bigintBytes == null || bigintBytes.length == 0) {
            return BigInteger.ZERO;
        }

        return new BigInteger(1, bigintBytes);
    }

    public static byte[] copyAllBytes(Byte[] data) {
        return copyAllBytes(nativeBytes(data));
    }

    public static byte[] copyAllBytes(byte[] data) {
        final byte[] out = new byte[data.length];
        System.arraycopy(data, 0, out, 0, data.length);
        return out;
    }

    public static byte[] nativeBytes(Byte[] src) {
        if (src == null || src.length == 0) {
            return new byte[0];
        }

        final byte[] out = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            out[i] = src[i];
        }

        return out;
    }

    public static boolean equals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        if (a[0] != b[0] || a[a.length - 1] != b[b.length - 1]) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }
}
