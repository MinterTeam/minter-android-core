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

import org.parceler.Parcel;

import network.minter.core.MinterSDK;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class MinterAddress extends PublicKey implements RLPBoxed.FixedByteLength {
    public static final String ADDRESS_PATTERN = "^(" + MinterSDK.PREFIX_ADDRESS + "|" + MinterSDK.PREFIX_ADDRESS.toLowerCase() + ")?([a-fA-F0-9]{40})$";


    public MinterAddress(Byte[] data) {
        super(
                checkArgument(data.length == 20, data, "Minter public key must contains exact 20 bytes")
        );
    }

    public MinterAddress(byte[] data) {
        super(
                checkArgument(data.length == 20, data, "Minter public key must contains exact 20 bytes")
        );
    }

	public MinterAddress(char[] data) {
		super(data);
	}

    public MinterAddress(CharSequence hexData) {
        super(
                checkArgument(
                        hexData != null && hexData.toString().matches(ADDRESS_PATTERN),
                        hexData,
                        "Minter public key in hex format must contains 40 or 42 characters, where first 2 chars is a prefix: Mx"
                )
        );
    }

    public MinterAddress(MinterAddress data) {
        super(data.getData());
    }

    /**
     * Creates minter public key from raw public key
     *
     * @param data Raw public key extracted from private key
     */
    public MinterAddress(PublicKey data) {
        // don't change source public key
        super(
                new BytesData(data.dropFirst())
                        .sha3Mutable()
                        .takeLastMutable(20)
        );
    }

    MinterAddress() {
    }

    public static boolean testString(CharSequence input) {
        if (input == null || input.length() == 0) {
            return false;
        }

        return input.toString().matches(ADDRESS_PATTERN);
    }

    @Override
    public MinterAddress clone() {
        super.clone();
        MinterAddress out = new MinterAddress();
        out.mValid = mValid;
        out.mData = new char[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

        return out;
    }

    /**
     * Convert public key to minter address short public key and converts to hex string with prefix
     *
     * @return last 20 bytes with minter prefix of sha3-hashed original public key
     */
    public String toString() {
        return toHexString(MinterSDK.PREFIX_ADDRESS, false);
    }

    /**
     * @return Mxfe6001...61eE99 short address
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
