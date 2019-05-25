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

package network.minter.core.crypto;

import network.minter.core.MinterSDK;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterPublicKey extends PublicKey {
    public static final String PUB_KEY_PATTERN = "^(" + MinterSDK.PREFIX_PUBLIC_KEY + "|" + MinterSDK.PREFIX_PUBLIC_KEY.toLowerCase() + ")?([a-fA-F0-9]{64})$";

    public MinterPublicKey(byte[] data) {
        super(data);
    }

    public MinterPublicKey(CharSequence hexData) {
        super(checkArgument(
                hexData != null && hexData.toString().matches(PUB_KEY_PATTERN),
                hexData,
                "Invalid public key format"));
    }

	public MinterPublicKey(char[] data) {
		super(data);
	}

    public MinterPublicKey(BytesData data) {
        super(data);
    }

    /**
     * @return Mpfe6001...61eE99 short pub key
     */
    public String toShortString() {
        final String in = toString();
        String firstPart = in.substring(0, 8);
        String lastPart = in.substring(in.length() - 6, in.length());

        return firstPart + "..." + lastPart;
    }

    @Override
    public String toString() {
        return toHexString(MinterSDK.PREFIX_PUBLIC_KEY, false);
    }
}
