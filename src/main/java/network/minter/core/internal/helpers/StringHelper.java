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

import java.math.BigDecimal;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.UnsignedBytesData;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class StringHelper {
    // 20 byte address with or without prefix
    public static final String HEX_NUM_PATTERN = "^(0x)?([a-fA-F0-9]{2,})$";
    private final static char[] hexArray = "0123456789ABCDEF".toLowerCase().toCharArray();

    public static String strlpad(int size, String input) {
        int offset = size - input.length();
        char[] inBytes = input.toCharArray();
        char[] out = new char[size];

        for (int i = 0, s = 0; i < size; i++) {
            if (i < offset) {
                out[i] = '\0';
            } else {
                out[i] = inBytes[s++];
            }
        }

        return new String(out);
    }

    public static String strrpad(int size, String input) {
        int offset = size - input.length();
        char[] inBytes = input.toCharArray();
        char[] out = new char[size];

        for (int i = 0, s = 0; i < size; i++) {
            if (i < input.length()) {
                out[i] = inBytes[i];
            } else {
                out[i] = '\0';
            }
        }

        return new String(out);
    }

	public static String charsToHexString(final char[] data) {
		return charsToHexString(data, false);
	}

    public static String bytesToHexString(final byte[] data) {
        return bytesToHexString(data, false);
    }

	public static String charsToHexString(final char[] data, boolean uppercase) {
		if (data == null || data.length == 0) {
			return "";
		}

		int size = data.length;
		char[] hexChars = new char[data.length * 2];
		for (int j = 0; j < size; j++) {
			int v = data[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		if (uppercase) {
			return new String(hexChars).toUpperCase();
		}

		return new String(hexChars);
	}

    public static String bytesToHexString(final byte[] data, boolean uppercase) {
        if (data == null || data.length == 0) {
            return "";
        }

        int size = data.length;
        char[] hexChars = new char[data.length * 2];
        for (int j = 0; j < size; j++) {
            int v = data[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        if (uppercase) {
            return new String(hexChars).toUpperCase();
        }

        return new String(hexChars);
    }

	public static int[] hexStringToInts(final String s) {
		if (s == null || s.length() == 0) {
			return new int[0];
		}

		String in = s
				.replace(MinterSDK.PREFIX_ADDRESS, "")
				.replace(MinterSDK.PREFIX_ADDRESS.toLowerCase(), "")
				.replace(MinterSDK.PREFIX_CHECK, "")
				.replace(MinterSDK.PREFIX_CHECK.toLowerCase(), "")
				.replace(MinterSDK.PREFIX_PUBLIC_KEY, "")
				.replace(MinterSDK.PREFIX_PUBLIC_KEY.toLowerCase(), "")
				.replace(MinterSDK.PREFIX_TX, "")
				.replace(MinterSDK.PREFIX_TX.toLowerCase(), "")
				.replace("0x", "");

		int len = in.length();
		int[] data = new int[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = ((Character.digit(in.charAt(i), 16) << 4)
					+ Character.digit(in.charAt(i + 1), 16)
			);
		}
		return data;
	}

    public static byte[] hexStringToBytes(final String s) {
        if (s == null || s.length() == 0) {
            return new byte[0];
        }

        String in = s
                .replace(MinterSDK.PREFIX_ADDRESS, "")
                .replace(MinterSDK.PREFIX_ADDRESS.toLowerCase(), "")
                .replace(MinterSDK.PREFIX_CHECK, "")
                .replace(MinterSDK.PREFIX_CHECK.toLowerCase(), "")
                .replace(MinterSDK.PREFIX_PUBLIC_KEY, "")
                .replace(MinterSDK.PREFIX_PUBLIC_KEY.toLowerCase(), "")
                .replace(MinterSDK.PREFIX_TX, "")
                .replace(MinterSDK.PREFIX_TX.toLowerCase(), "")
                .replace("0x", "");

        int len = in.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(in.charAt(i), 16) << 4)
                    + Character.digit(in.charAt(i + 1), 16));
        }
        return data;
    }

	public static char[] hexStringToChars(final String s) {
		if (s == null || s.length() == 0) {
			return new char[0];
		}

		String in = s
				.replace(MinterSDK.PREFIX_ADDRESS, "")
				.replace(MinterSDK.PREFIX_ADDRESS.toLowerCase(), "")
				.replace(MinterSDK.PREFIX_CHECK, "")
				.replace(MinterSDK.PREFIX_CHECK.toLowerCase(), "")
				.replace(MinterSDK.PREFIX_PUBLIC_KEY, "")
				.replace(MinterSDK.PREFIX_PUBLIC_KEY.toLowerCase(), "")
				.replace(MinterSDK.PREFIX_TX, "")
				.replace(MinterSDK.PREFIX_TX.toLowerCase(), "")
				.replace("0x", "");

		int len = in.length();
		char[] data = new char[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (char) ((Character.digit(in.charAt(i), 16) << 4)
					+ Character.digit(in.charAt(i + 1), 16)
			);
		}
		return data;
	}

    public static String bytesToString(String hexString) {
        return bytesToString(hexStringToBytes(hexString));
    }

	public static String charsToString(String hexString) {
		return charsToString(hexStringToChars(hexString));
	}

	public static String charsToString(char[] data) {
		return charsToString(data, data.length);
	}

    public static String bytesToString(byte[] data) {
        return bytesToString(data, data.length);
    }

	public static String charsToString(char[] data, int readLength) {
		if (data.length < readLength) {
			throw new ArrayIndexOutOfBoundsException(
					"Read length less than array size: " + String.valueOf(readLength) + " of " +
							String.valueOf(data.length));
		}
		return new String(data);
	}

    public static String bytesToString(byte[] data, int readLength) {
        if (data.length < readLength) {
            throw new ArrayIndexOutOfBoundsException("Read length less than array size: " + String.valueOf(readLength) + " of " + String.valueOf(data.length));
        }
        final char[] out = new char[readLength];
        for (int i = 0; i < readLength; i++) {
            out[i] = (char) data[i];
        }

        return new String(out);
    }

    /**
     * @param num
     * @return always 2 elements
     */
    public static DecimalStringFraction splitDecimalStringFractions(BigDecimal num) {
        final String sNum = num.toPlainString();
        final String[] fractions = sNum.split("\\.");
        final String[] out = new String[2];
        // just in case
        if (fractions.length == 0) {
            out[0] = "0";
            out[1] = "0";
        } else if (fractions.length == 1) {
            out[0] = fractions[0];
            out[1] = "0";
        } else {
            out[0] = fractions[0];
            out[1] = fractions[1];
        }

        return new DecimalStringFraction(out[0], out[1]);
    }

    /**
     * @param num
     * @return always 2 elements
     */
    public static DecimalFraction splitDecimalFractions(BigDecimal num) {
        final String sNum = num.toPlainString();
        final String[] fractions = sNum.split("\\.");
        final long[] out = new long[2];
        // just in case
        if (fractions.length == 0) {
            out[0] = 0L;
            out[1] = 0L;
        } else if (fractions.length == 1) {
            out[0] = Long.parseLong(fractions[0]);
            out[1] = 0L;
        } else {
            out[0] = Long.parseLong(fractions[0]);
            out[1] = Long.parseLong(fractions[1]);
        }

        return new DecimalFraction(out[0], out[1]);
    }

    public static boolean testHex(String s) {
        return s.matches(HEX_NUM_PATTERN);
    }

    public static class DecimalStringFraction {
        public String intPart;
        public String fractionalPart;

        public DecimalStringFraction(String intPart, String fractionalPart) {
            this.intPart = intPart;
            this.fractionalPart = fractionalPart;
        }
    }

    public static class DecimalFraction {
        public long intPart;
        public long fractionalPart;

        public DecimalFraction(long intPart, long fractionalPart) {
            this.intPart = intPart;
            this.fractionalPart = fractionalPart;
        }
    }
}
