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

package network.minter.core.internal.helpers;

import org.spongycastle.util.BigIntegers;

import java.math.BigInteger;

import network.minter.core.crypto.BytesData;
import network.minter.core.util.DecodeResult;

import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.util.RLPBoxed.toChars;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesHelper {
	public static char[] lpad(int size, char[] input) {
		if (input.length == size) {
			return input;
		}

		if (input.length > size) {
			final char[] out = new char[size];
			System.arraycopy(input, 0, out, 0, size);
			return out;
		}

		int offset = size - input.length;
		char[] out = new char[size];

		for (int i = 0, s = 0; i < size; i++) {
			if (i < offset) {
				out[i] = (byte) 0;
			} else {
				out[i] = input[s++];
			}
		}

		return out;
	}

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

	public static char[] bytesToChars(byte[] in) {
		if (in == null || in.length == 0) {
			return new char[0];
		}

		char[] out = new char[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = (char) (in[i] & 0xFF);
		}

		return out;
	}

	public static byte[] charsToBytes(char[] in) {
		if (in == null || in.length == 0) {
			return new byte[0];
		}

		byte[] out = new byte[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = (byte) (in[i] & 0xFF);
		}

		return out;
	}

	public static byte[] intsToBytes(int[] in) {
		if (in == null || in.length == 0) {
			return new byte[0];
		}

		byte[] out = new byte[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = (byte) in[i];
		}

		return out;
	}

	public static char[] intsToChars(int[] in) {
		if (in == null || in.length == 0) {
			return new char[0];
		}

		char[] out = new char[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = (char) (in[i] & 0xFF);
		}

		return out;
	}

	public static byte[] bigintToBytes(BigInteger num) {
		if (num == null) {
			return new byte[0];
		}

        byte[] biArr;
        if (num.equals(new BigInteger("128"))) {
            biArr = new byte[]{(byte) 0x80};
        } else {
            biArr = BigIntegers.asUnsignedByteArray(num);
        }

        return biArr;
    }

    public static char[] addLeadingZeroes(char[] input, int mandatoryLen) {
        if (input.length == mandatoryLen) {
            return input;
        }
        BytesData out = new BytesData(mandatoryLen);
        int i = 0;
        for (; i < (mandatoryLen - input.length); i++) {
            out.write(i, (byte) 0x00);
        }
        out.write(i, input);
        return out.getData();
    }

    public static byte[] addLeadingZeroes(byte[] input, int mandatoryLen) {
        if (input.length == mandatoryLen) {
            return input;
        }
        BytesData out = new BytesData(mandatoryLen);
        int i = 0;
        for (; i < (mandatoryLen - input.length); i++) {
            out.write(i, (byte) 0x00);
        }
        out.write(i, input);
        return out.getBytes();
    }

    public static char[] dropLeadingZeroes(char[] input) {
        if (input == null || input.length == 0) {
            return input;
        }

        int targetLen = input.length;
        int i = 0;
        while (i < input.length && input[i] == 0x00) {
            i++;
			targetLen--;
		}

		char[] target = new char[targetLen];
		for (int c = i, n = 0; c < input.length; c++, n++) {
			target[n] = input[c];
		}

		return target;
	}

	public static byte[] dropLeadingZeroes(byte[] input) {
		if (input == null || input.length == 0) {
			return input;
		}
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

	public static BigInteger fixBigintSignedByte(Object bigintBytes) {
		if (bigintBytes instanceof byte[]) {
			return fixBigintSignedByte((byte[]) bigintBytes);
		} else if (bigintBytes instanceof char[]) {
			return fixBigintSignedByte((char[]) bigintBytes);
		} else if (bigintBytes instanceof String) {
            if (bigintBytes.equals("")) {
                return fixBigintSignedByte(new byte[]{0x00});
            }
			// fuckn hack! we can't have leading zero byte for single byte value, but BigInteger generates for value 128 - 2 bytes value = byte[]{0, -127}
			// Also we have problem with RLP decoding from simple single byte[1] value that equals = byte[]{0x80}, as it interprets as EMPTY value
            // @see RLPBoxed.decode(): 442 line
			//
			// second way - move all encodings to native code, we would have unsigned byte
			// third way - custom bigint value with integer backend or rework RLP decoder
			return fixBigintSignedByte(new byte[]{0x00, (byte) 0x80});
        } else if (bigintBytes instanceof Object[]) {
            return fixBigintSignedByte(new byte[]{0x00});
        }

		throw new RuntimeException("Unsupported RLP object: " + bigintBytes.getClass().getName());
	}

	public static BigInteger fixBigintSignedByte(char[] bigintBytes) {
		if (bigintBytes == null || bigintBytes.length == 0) {
			return BigInteger.ZERO;
		}

		return new BigInteger(1, charsToBytes(bigintBytes));
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

	public static char[] copyAllBytes(Character[] data) {
		final char[] out = new char[data.length];
		System.arraycopy(data, 0, out, 0, data.length);
		return out;
	}


	public static char[] copyAllBytes(char[] data) {
		final char[] out = new char[data.length];
		System.arraycopy(data, 0, out, 0, data.length);
		return out;
	}

    public static byte[] copyAllBytes(byte[] data) {
        final byte[] out = new byte[data.length];
        System.arraycopy(data, 0, out, 0, data.length);
        return out;
    }

	public static char[] nativeChars(Character[] src) {
		if (src == null || src.length == 0) {
			return new char[0];
		}

		final char[] out = new char[src.length];
		for (int i = 0; i < src.length; i++) {
			out[i] = src[i];
		}

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

	public static char[] nativeBytesToChars(Byte[] src) {
		if (src == null || src.length == 0) {
			return new char[0];
		}

		final char[] out = new char[src.length];
		for (int i = 0; i < src.length; i++) {
			out[i] = (char) (src[i].byteValue() & 0xFF);
		}

		return out;
	}

	public static boolean equals(char[] a, char[] b) {
        if (a.length == 0 && b.length == 0) {
            return true;
        }

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

    public static boolean nullBytes(Object input) {
        if (input instanceof char[]) {
            return nullBytes(toChars(input));
        } else if (input instanceof String) {
            return nullBytes(toChars(input));
        }

        return false;
    }

    public static boolean nullBytes(char[] input) {
        for (char i : input) {
            if (i != (char) 0x00) {
                return false;
            }
        }

        return true;
    }
}
