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

package network.minter.core.crypto;

import network.minter.core.MinterSDK;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class MinterCheck extends PublicKey {
    public static final String PATTERN = "^(" + MinterSDK.PREFIX_CHECK + "|" + MinterSDK.PREFIX_CHECK.toLowerCase() + ")?([a-fA-F0-9]+)$";

    public MinterCheck(Byte[] data) {
        super(
                checkArgument(data.length > 0, data, "Minter check data can't be empty")
        );
    }

    public MinterCheck(byte[] data) {
        super(
                checkArgument(data.length > 0, data, "Minter check data can't be empty")
        );
    }

	public MinterCheck(char[] data) {
		super(checkArgument(data.length > 0, data, "Minter check data can't be empty"));
	}

    public MinterCheck(CharSequence hexData) {
        super(
                checkArgument(
                        hexData != null && hexData.toString().toLowerCase().matches(PATTERN),
                        hexData,
                        "Minter check has invalid format and must contains 'Mc' at the beginning"
                )
        );
    }

    public MinterCheck(MinterCheck data) {
        super(data.getData());
    }

    MinterCheck() {
    }

    @Override
    public MinterCheck clone() {
        super.clone();
        MinterCheck out = new MinterCheck();
        out.mValid = mValid;
        out.mData = new char[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

        return out;
    }

    /**
     * Convert bytes to minter short check and converts to hex string with prefix
     */
    public String toString() {
        return toHexString(MinterSDK.PREFIX_CHECK, false);
    }

    /**
     * @return Mcfe6001...61eE99 short check
     */
    public String toShortString() {
        final String in = toString();
        String firstPart = in.substring(0, 8);
        String lastPart = in.substring(in.length() - 6);

        return firstPart + "..." + lastPart;
    }

    public boolean equals(String other) {
        return other.equals(toString());
    }
}
