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

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.annotation.Nonnull;

import network.minter.core.internal.helpers.BytesHelper;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.FastByteComparisons;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.BytesHelper.bytesToChars;
import static network.minter.core.internal.helpers.BytesHelper.charsToBytes;
import static network.minter.core.internal.helpers.BytesHelper.copyAllBytes;
import static network.minter.core.internal.helpers.BytesHelper.intsToChars;
import static network.minter.core.internal.helpers.BytesHelper.nativeBytesToChars;
import static network.minter.core.internal.helpers.BytesHelper.nativeChars;
import static network.minter.core.internal.helpers.StringHelper.charsToString;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesData implements Comparable<BytesData>, Serializable, Cloneable {
    protected char[] mData;
    boolean mValid = true;
    int mHashCode = 0;

    public BytesData(int dataSize) {
        this(new char[dataSize]);
    }

    /**
     * Mutable constructor, be carefully
     * @param buffer
     */
    public BytesData(ByteBuffer buffer) {
        this(buffer.asCharBuffer().array());
        mValid = true;
    }

    /**
     * @param buffer
     * @param immutable copy data or not
     */
    public BytesData(ByteBuffer buffer, boolean immutable) {
        this(immutable ? copyAllBytes(buffer.asCharBuffer().array()) : buffer.asCharBuffer().array());
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
    public BytesData(Character[] data, boolean immutable) {
        this(immutable ? copyAllBytes(data) : nativeChars(data));
    }

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    public BytesData(Byte[] data) {
        if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = nativeBytesToChars(data);
        calcHash();
    }

    /**
     * @param data
     * @param immutable copy data or not
     */
    public BytesData(char[] data, boolean immutable) {
        this(immutable ? copyAllBytes(data) : data);
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
        char[] out = new char[len];
        int offset = 0;
        for (byte[] sub : dataArray) {
            for (int i = 0, c = 0; c < sub.length; c++, i++) {
                out[c + offset] = (char) (sub[i] & 0xFF);
            }
            offset += sub.length;
        }

        mData = out;
        calcHash();
    }

    /**
     * 2d array will be flatten into 1d
     * @param dataArray
     */
    public BytesData(char[]... dataArray) {
        checkArgument(dataArray != null, "Data array can't be null");

        int len = 0;
        for (char[] sub : dataArray) {
            len += sub.length;
        }
        char[] out = new char[len];
        int offset = 0;
        for (char[] sub : dataArray) {
            for (int i = 0, c = 0; c < sub.length; c++, i++) {
                out[c + offset] = (char) (sub[i] & 0xFF);
            }
            offset += sub.length;
        }

        mData = out;
        calcHash();
    }

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    public BytesData(byte[] data) {
		if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = bytesToChars(data);
        calcHash();
    }

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    public BytesData(char[] data) {
        if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = data;
        calcHash();
    }

    /**
     * Mutable constructor, be carefully
     * @param data
     */
    public BytesData(int[] data) {
        if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = intsToChars(data);
        mHashCode = Arrays.hashCode(data);
    }

    public BytesData(CharSequence hexData) {
        this(StringHelper.hexStringToInts(hexData.toString().toLowerCase()));
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

        if (!(other instanceof BytesData) && !(other instanceof char[]) &&
                !(other instanceof byte[]) && !(other instanceof int[]) &&
                !(other instanceof Character[]) && !(other instanceof ByteBuffer)) {
            return false;
        }

        char[] otherData;
        if (other instanceof byte[]) {
            otherData = bytesToChars((byte[]) other);
        } else if (other instanceof char[]) {
            otherData = (char[]) other;
        } else if (other instanceof int[]) {
            otherData = intsToChars((int[]) other);
        } else if (other instanceof Character[]) {
            otherData = nativeChars(((Character[]) other));
        } else if (other instanceof ByteBuffer) {
            byte[] src = ((ByteBuffer) other).array();
            otherData = bytesToChars(src);
        } else {
            otherData = ((BytesData) other).getData();
        }

        if (mData.length != otherData.length) {
            return false;
        }

        return FastByteComparisons.equal(mData, otherData);
    }

    @Override
    public String toString() {
        return StringHelper.charsToHexString(mData);
    }

    public String toStringASCII() {
        return charsToString(mData);
    }

    @Override
    public int compareTo(@Nonnull BytesData o) {
        // @TODO MAKE
        return equals(o) ? 0 : 1;
//        if(getData().length != o.getData().length) {
//            return size() - o.size();
//        }
//        return FastByteComparisons.compareTo(
//                getData(), 0, mData.length,
//                o.getData(), 0, o.getData().length);
    }

    public char[] getData() {
        return mData;
    }

    public String stringValue() {
        return new String(getData());
    }

    public byte[] getBytes() {
        return charsToBytes(getData());
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
    public char[] sha256() {
        return bytesToChars(HashUtil.sha256(charsToBytes(getData())));
    }

    /**
     * Mutable sha256 hashing
     * @return self instance
     */
    public BytesData sha256Mutable() {
        mData = sha256();
        calcHash();
        return this;
    }

    /**
     * Calculates sha3 hash from bytes
     * @return
     */
    public char[] sha3() {
        return bytesToChars(HashUtil.sha3(charsToBytes(getDataImmutable())));
    }

    /**
     * Calculates sha3 hash from bytes and set new data to this object
     * @return current object
     */
    public BytesData sha3Mutable() {
        mData = sha3();
        calcHash();
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
        return StringHelper.charsToHexString(getData(), uppercase);
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
        return StringHelper.charsToHexString(getData());
    }

    /**
     * Copies mData to new array and returns it.
     * @return new byte[]
     */
    public char[] getDataImmutable() {
        final char[] out = new char[size()];
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

    public char[] takeFirst(int len) {
        if (len < 0) {
            throw new IndexOutOfBoundsException("Length can't be less than 0");
        }
        if (getData().length < len) {
            throw new IndexOutOfBoundsException(String.format("Data size less than %d: actual %d", len, size()));
        }

        final char[] out = new char[len];
        System.arraycopy(getData(), 0, out, 0, len);
        return out;
    }

    public BytesData takeLastMutable(int len) {
        mData = (takeLast(len));
        calcHash();
        return this;
    }

    public BytesData takeFirstMutable(int len) {
        mData = takeFirst(len);
        calcHash();
        return this;
    }

    public char[] dropLast() {
        char[] out = new char[size() - 1];
        System.arraycopy(getData(), 0, out, 0, size() - 1);
        return out;
    }

    public BytesData dropLastMutable() {
        mData = dropLast();
        calcHash();
        return this;
    }

    public char[] dropFirst() {
        char[] out = new char[size() - 1];
        System.arraycopy(getData(), 1, out, 0, size() - 1);
        return out;
    }

    public BytesData dropFirstMutable() {
        mData = dropFirst();
        calcHash();
        return this;
    }

    public char[] takeLast(int len) {
        if (len < 0) {
            throw new IndexOutOfBoundsException("Length can't be less than 0");
        }
        if (len >= size()) {
            throw new IndexOutOfBoundsException("Length can't be more than mData size");
        }

        if (len == 0) {
            return new char[0];
        }

        final char[] out = new char[len];
        System.arraycopy(getData(), size() - len, out, 0, len);

        return out;
    }

    public char[] lpad(int size) {
        return BytesHelper.lpad(size, getData());
    }

    public byte[] lpadBytes(int size) {
        return charsToBytes(BytesHelper.lpad(size, getData()));
    }

    public BytesData lpadMutable(int size) {
        mData = BytesHelper.lpad(size, getData());
        calcHash();
        return this;
    }

    public int write(int pos, char val) {
        checkArgument(pos >= 0 && pos < size(), "Position of out bounds");
        mData[pos] = val;

        return 1;
    }

    public int write(int pos, short val) {
        checkArgument(pos >= 0 && pos + 1 < size(), "Value can't be fit into passed position");
        mData[pos] = (char) (val >>> 8);
        mData[pos + 1] = (char) (val & 0xFF);
        calcHash();
        return 2;
    }

    public int write(int pos, int val) {
        checkArgument(pos >= 0 && pos + 3 < size(), "Value can't be fit into passed position");
        mData[pos] = (char) (val >>> 24);
        mData[pos + 1] = (char) (val >>> 16);
        mData[pos + 2] = (char) (val >>> 8);
        mData[pos + 3] = (char) (val & 0xFF);
        calcHash();
        return val;
    }

    public int write(int pos, long val) {
        checkArgument(pos >= 0 && pos + 3 < size(), "Value can't be fit into passed position");
        mData[pos] = (char) (val >>> 56);
        mData[pos + 1] = (char) (val >>> 48);
        mData[pos + 2] = (char) (val >>> 40);
        mData[pos + 3] = (char) (val >>> 32);
        mData[pos + 4] = (char) (val >>> 24);
        mData[pos + 5] = (char) (val >>> 16);
        mData[pos + 6] = (char) (val >>> 8);
        mData[pos + 7] = (char) (val & 0xFF);
        calcHash();

        return 8;
    }

    public int write(int pos, BigInteger val) {
        final char[] numVal = bytesToChars(val.toByteArray());
        checkArgument(pos >= 0 && pos + numVal.length <= size(), "Value can't fitted into passed position");
        System.arraycopy(numVal, 0, mData, pos, numVal.length);
        return numVal.length;
    }

    public int write(int pos, char[] data) {
        checkArgument(pos >= 0 && pos + data.length <= size(), "Value can't fitter into passed position");
        System.arraycopy(data, 0, mData, pos, data.length);
        return data.length;
    }

    public int write(int pos, byte[] data) {
        checkArgument(pos >= 0 && pos + data.length <= size(), "Value can't fitter into passed position");
        System.arraycopy(bytesToChars(data), 0, mData, pos, data.length);
        return data.length;
    }

    public int write(int pos, BytesData data) {
        return write(pos, data.getData());
    }

    public BytesData takeRangeMutable(int from, int to) {
        final char[] data = takeRange(from, to);
        memzero();
        mData = data;
        calcHash();
        return this;
    }

    public void memzero() {
        Arrays.fill(mData, (char) 0);
    }

    public byte[] takeByteRange(int from, int to) {
        return charsToBytes(takeRange(from, to));
    }

    public char[] takeRange(int from, int to) {
        checkArgument(from >= 0, "From can't be < 0");
        checkArgument(from <= to, "From can't be > To");
        checkArgument(to <= size(), "To can't be >= size()");

        final char[] out = new char[to - from];
        System.arraycopy(
                getData(), //src
                from, // src pos
                out, // dst
                0, // dst pos
                to - from // read len
        );
        return out;
    }

    public BytesData takeRangeLengthMutable(int from, int length) {
        final char[] data = takeRangeLength(from, length);
        memzero();
        mData = data;
        calcHash();
        return this;
    }

    public byte[] takeByteRangeLength(int from, int length) {
        return charsToBytes(takeRangeLength(from, length));
    }

    public char[] takeRangeLength(int from, int length) {
        checkArgument(from + length <= size(), "Length out of bound data size");
        final char[] out = new char[length];
        System.arraycopy(
                getData(), //src
                from, // src pos
                out, // dst
                0, // dst pos
                length // read len
        );
        return out;
    }

    public BytesData takeRangeFromMutable(int from) {
        final char[] data = takeRangeFrom(from);
        memzero();
        mData = data;
        calcHash();
        return this;
    }

    public char[] takeByteRangeFrom(int from) {
        return takeRange(from, size());
    }

    public char[] takeRangeFrom(int from) {
        return takeRange(from, size());
    }

    public BytesData takeRangeToMutable(int to) {
        final char[] data = takeRangeTo(to);
        memzero();
        mData = data;
        calcHash();
        return this;
    }

    public byte[] takeByteRangeTo(int to) {
        return charsToBytes(takeRangeTo(to));
    }

    public char[] takeRangeTo(int to) {
        return takeRange(0, to);
    }

    public BigInteger toUShortBigInt(int readFrom2bytes) {
        final char[] numData = takeRangeLength(readFrom2bytes, 2);
        return new BigInteger(charsToBytes(numData));
    }

    public BigInteger toUIntBigInt(int readFrom4bytes) {
        final char[] numData = takeRangeLength(readFrom4bytes, 4);
        return new BigInteger(charsToBytes(numData));
    }

    public BigInteger toULongBigInt(int readFrom8bytes) {
        final char[] numData = takeRangeLength(readFrom8bytes, 8);
        return new BigInteger(charsToBytes(numData));
    }

    public char at(int pos) {
        checkArgument(pos >= 0 && pos < size(), "Position out of bounds");
        return getData()[pos];
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
        out.mData = new char[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

		return out;
	}

	private void calcHash() {
        mHashCode = Arrays.hashCode(mData);
    }
}
