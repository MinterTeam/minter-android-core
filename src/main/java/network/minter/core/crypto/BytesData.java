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
import org.spongycastle.util.encoders.Hex;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.annotation.Nonnull;

import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.FastByteComparisons;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.BytesHelper.charsToBytes;
import static network.minter.core.internal.helpers.BytesHelper.copyAllBytes;
import static network.minter.core.internal.helpers.BytesHelper.nativeBytes;
import static network.minter.core.internal.helpers.StringHelper.bytesToString;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class BytesData implements Comparable<BytesData>, Serializable, Cloneable {
    protected byte[] mData;
    boolean mValid = true;
    int mHashCode = 0;

    /**
     * Mutable constructor, be carefully
     * @param buffer
     */
    public BytesData(ByteBuffer buffer) {
        this(buffer.array());
        mValid = true;
    }

    /**
     * @param buffer
     * @param immutable copy data or not
     */
    public BytesData(ByteBuffer buffer, boolean immutable) {
        this(immutable ? copyAllBytes(buffer.array()) : buffer.array());
    }

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    @SuppressWarnings("CopyConstructorMissesField")
    public BytesData(BytesData data) {
        this(data.getData());
        mValid = true;
    }

    /**
     * @param data
     * @param immutable copy data or not
     */
    @SuppressWarnings("CopyConstructorMissesField")
    public BytesData(BytesData data, boolean immutable) {
        this(immutable ? data.getDataImmutable() : data.getData());
    }

    /**
     * @param data
     * @param immutable copy data or not
     */
    public BytesData(Byte[] data, boolean immutable) {
        this(immutable ? copyAllBytes(data) : nativeBytes(data));
    }

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    public BytesData(Byte[] data) {
        if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = nativeBytes(data);
        mHashCode = Arrays.hashCode(data);
    }

    /**
     * @param data
     * @param immutable copy data or not
     */
    public BytesData(byte[] data, boolean immutable) {
        this(immutable ? copyAllBytes(data) : data);
    }

    /**
     * 2d array will be flatten into 1d
     * @param dataArray
     */
    public BytesData(byte[]... dataArray) {
        checkArgument(dataArray != null, "Data array can't be null");

        int len = 0;
        for (byte[] sub : dataArray) {
            len += sub.length;
        }
        byte[] out = new byte[len];
        int offset = 0;
        for (byte[] sub : dataArray) {
            System.arraycopy(sub, 0, out, offset, sub.length);
            offset += sub.length;
        }

        mData = out;
        mHashCode = Arrays.hashCode(out);
    }

	public BytesData(char[] data) {
		if (data == null)
			throw new NullPointerException("Data must not be null");

		mData = charsToBytes(data);
		mHashCode = Arrays.hashCode(data);
	}

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    public BytesData(byte[] data) {
        if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = data;
        mHashCode = Arrays.hashCode(data);
    }

    public BytesData(CharSequence hexData) {
        this(StringHelper.hexStringToBytes(hexData.toString()));
    }

    BytesData() {
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    public boolean equals(Object other) {
        if (!mValid) {
            return false;
        }

        if (!(other instanceof BytesData) && !(other instanceof byte[]) && !(other instanceof Byte[]) && !(other instanceof ByteBuffer)) {
            return false;
        }

        final byte[] otherData;
        if (other instanceof byte[]) {
            otherData = ((byte[]) other);
        } else if (other instanceof Byte[]) {
            otherData = nativeBytes(((Byte[]) other));
        } else if (other instanceof ByteBuffer) {
            otherData = ((ByteBuffer) other).array();
        } else {
            otherData = ((BytesData) other).getData();
        }

        return FastByteComparisons.equal(mData, otherData);
    }

    @Override
    public String toString() {
        return Hex.toHexString(mData);
    }

    public String toStringASCII() {
        return bytesToString(mData);
    }

    @Override
    public int compareTo(@Nonnull BytesData o) {
        return FastByteComparisons.compareTo(
                mData, 0, mData.length,
                o.getData(), 0, o.getData().length);
    }

    public byte[] getData() {
        return mData;
    }

    /**
     * @return Size of mData
     */
    public int size() {
        return getData().length;
    }

    /**
     * Calculate simple sha256 hash
     * @return hash bytes
     */
    public byte[] sha256() {
        return HashUtil.sha256(getData());
    }

    /**
     * Mutable sha256 hashing
     * @return self instance
     */
    public BytesData sha256Mutable() {
        mData = sha256();
        return this;
    }

    /**
     * Calculates sha3 hash from bytes
     * @return
     */
    public byte[] sha3() {
        return HashUtil.sha3(getDataImmutable());
    }

    /**
     * Calculates sha3 hash from bytes and set new data to this object
     * @return current object
     */
    public BytesData sha3Mutable() {
        mData = sha3();
        return this;
    }

    /**
     * Calculates sha3 hash from bytes and returns raw mData
     * @return
     */
    public BytesData sha3Data() {
        return new BytesData(sha3());
    }

    /**
     * Converts mData to hex string with uppercase option
     * @param uppercase Transform string to uppercase
     * @return hex string
     */
    public String toHexString(boolean uppercase) {
        return StringHelper.bytesToHexString(getData(), uppercase);
    }

    public String toHexString(String prefix) {
        return prefix + toHexString();
    }

    public String toHexString(String prefix, boolean uppercase) {
        return prefix + toHexString(uppercase);
    }

    /**
     * Converts mData to hex lowercase string
     * @return hex string
     */
    public String toHexString() {
        return StringHelper.bytesToHexString(getData());
    }

    /**
     * Copies mData to new array and returns it.
     * @return new byte[]
     */
    public byte[] getDataImmutable() {
        final byte[] out = new byte[size()];
        System.arraycopy(getData(), 0, out, 0, size());
        return out;
    }

    /**
     * Fills mData with zeros
     * Don't use without method
     * @see #isValid()
     */
    public void cleanup() {
        if (mData == null || size() == 0) {
            mValid = false;
            return;
        }

        for (int i = 0; i < size(); i++) {
            getData()[i] = '\0';
        }
        mData = null;
        mValid = false;
    }

    /**
     * Check mData is in valid state
     * @return false is method #cleanup() called, otherwise true
     */
    public boolean isValid() {
        return mValid;
    }

    public byte[] takeFirst(int len) {
        if (len < 0) {
            throw new IndexOutOfBoundsException("Length can't be less than 0");
        }
        if (getData().length < len) {
            throw new IndexOutOfBoundsException(String.format("Data size less than %d: actual %d", len, size()));
        }

        final byte[] out = new byte[len];
        System.arraycopy(getData(), 0, out, 0, len);
        return out;
    }

    public BytesData takeLastMutable(int len) {
        mData = takeLast(len);
        return this;
    }

    public BytesData takeFirstMutable(int len) {
        mData = takeFirst(len);
        return this;
    }

    public byte[] dropLast() {
        byte[] out = new byte[size() - 1];
        System.arraycopy(getData(), 0, out, 0, size() - 1);
        return out;
    }

    public BytesData dropLastMutable() {
        mData = dropLast();
        return this;
    }

    public byte[] dropFirst() {
        byte[] out = new byte[size() - 1];
        System.arraycopy(getData(), 1, out, 0, size() - 1);
        return out;
    }

    public BytesData dropFirstMutable() {
        mData = dropFirst();
        return this;
    }

    public byte[] takeLast(int len) {
        if (len < 0) {
            throw new IndexOutOfBoundsException("Length can't be less than 0");
        }
        if (len >= size()) {
            throw new IndexOutOfBoundsException("Length can't be more than mData size");
        }

        if (len == 0) {
            return new byte[0];
        }

        final byte[] out = new byte[len];
        System.arraycopy(getData(), size() - len, out, 0, len);

        return out;
    }

    public byte[] lpad(int size) {
        return BytesHelper.lpad(size, getData());
    }

    public BytesData lpadMutable(int size) {
        mData = BytesHelper.lpad(size, getData());
        return this;
    }

    @Override
    public BytesData clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        BytesData out = new BytesData();
        out.mValid = mValid;
        out.mData = new byte[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

        return out;
    }
}
