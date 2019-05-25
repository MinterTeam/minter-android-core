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

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class PublicKey extends BytesData implements java.security.PublicKey {

    public PublicKey(Byte[] data) {
        super(data);
    }

    public PublicKey(byte[] data) {
        super(data);
    }

	public PublicKey(char[] data) {
		super(data);
	}

    public PublicKey(CharSequence hexData) {
        super(hexData);
    }

    public PublicKey(BytesData data) {
        super(data);
    }

    PublicKey() {
    }

    /**
     * Convert public key to minter address short public key
     *
     * @return last 20 bytes of sha3-hashed original public key
     */
    public MinterAddress toMinter() {
        if (this instanceof MinterAddress) {
            return (MinterAddress) this;
        }

        return new MinterAddress(this);
    }

    @Override
    public String getAlgorithm() {
        return "EC";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return new byte[0];
    }

    public boolean verify(PrivateKey privateKey) {
        PublicKey pk = privateKey.getPublicKey(false);
        return pk.equals(this);
    }

    @Override
    public PublicKey clone() {
        super.clone();
        PublicKey out = new PublicKey();
        out.mValid = mValid;
        out.mData = new byte[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

        return out;
    }
}
