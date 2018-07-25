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
}
