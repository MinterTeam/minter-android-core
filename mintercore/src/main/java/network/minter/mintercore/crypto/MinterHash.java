/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
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

package network.minter.mintercore.crypto;

import org.parceler.Parcel;

import network.minter.mintercore.MinterSDK;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class MinterHash extends PublicKey {
    public MinterHash(byte[] data) {
        super(
                checkArgument(data.length == 20, data, "Minter hash must contains exact 20 bytes")
        );
    }

    public MinterHash(CharSequence hexData) {
        super(
                checkArgument(
                        hexData != null && (hexData.length() == 40 || (hexData.length() == 42 && hexData.subSequence(0, 2).equals(MinterSDK.PREFIX_TX))),
                        hexData,
                        "Minter hash in hex format must contains 40 or 42 characters, where first 2 chars is a prefix \"Mt\""
                )
        );
    }

    public MinterHash(MinterHash data) {
        super(data.getData());
    }

    /**
     * Creates minter public key from raw public key
     *
     * @param data Raw public key extracted from private key
     */
    public MinterHash(PublicKey data) {
        // don't change source public key
        super(
                new BytesData(data.dropFirst())
                        .sha3Mutable()
                        .takeLastMutable(20)
        );
    }

    MinterHash() {
    }

    @Override
    public MinterHash clone() {
        super.clone();
        MinterHash out = new MinterHash();
        out.mValid = mValid;
        out.mData = new byte[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

        return out;
    }

    /**
     * Convert bytes to minter short hash and converts to hex string with prefix
     */
    public String toString() {
        return toHexString(MinterSDK.PREFIX_TX, false);
    }

    /**
     * @return Mtfe6001...61eE99 short address
     */
    public String toShortString() {
        final String in = toString();
        String firstPart = in.substring(0, 8);
        String lastPart = in.substring(in.length() - 6, in.length());

        return firstPart + "..." + lastPart;
    }

    public boolean equals(String other) {
        return other.equals(toString());
    }
}
