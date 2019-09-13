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

import java.io.Serializable;
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

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class UnsignedBytesData implements Comparable<UnsignedBytesData>, Serializable, Cloneable {
	protected char[] mData;
	boolean mValid = true;
	int mHashCode = 0;

	/**
	 * Mutable constructor, be carefully
	 *
	 * @param buffer
	 */
	public UnsignedBytesData(ByteBuffer buffer) {
		this(buffer.asCharBuffer().array());
		mValid = true;
	}

	public UnsignedBytesData(BytesData signedBuff) {
		this(signedBuff.getData());
	}

	/**
	 * @param buffer
	 * @param immutable copy data or not
	 */
	public UnsignedBytesData(ByteBuffer buffer, boolean immutable) {
		this(immutable ? copyAllBytes(buffer.asCharBuffer().array()) : buffer.asCharBuffer().array());
	}

	/**
	 * Mutable constructor, be carefully
	 *
	 * @param data
	 */
	@SuppressWarnings("CopyConstructorMissesField")
	public UnsignedBytesData(UnsignedBytesData data) {
		this(data.getData());
		mValid = true;
	}

	/**
	 * @param data
	 * @param immutable copy data or not
	 */
	@SuppressWarnings("CopyConstructorMissesField")
	public UnsignedBytesData(UnsignedBytesData data, boolean immutable) {
		this(immutable ? data.getDataImmutable() : data.getData());
	}

	/**
	 * @param data
	 * @param immutable copy data or not
	 */
	public UnsignedBytesData(Character[] data, boolean immutable) {
		this(immutable ? copyAllBytes(data) : nativeChars(data));
	}

	/**
	 * Mutable constructor, be carefully
	 *
	 * @param data
	 */
	public UnsignedBytesData(Byte[] data) {
		if (data == null)
			throw new NullPointerException("Data must not be null");

		mData = nativeBytesToChars(data);
		mHashCode = Arrays.hashCode(data);
	}

	/**
	 * @param data
	 * @param immutable copy data or not
	 */
	public UnsignedBytesData(char[] data, boolean immutable) {
		this(immutable ? copyAllBytes(data) : data);
	}

	/**
	 * @param data
	 * @param immutable copy data or not
	 */
	public UnsignedBytesData(byte[] data, boolean immutable) {
		this(immutable ? copyAllBytes(data) : data);
	}

	/**
	 * 2d array will be flatten into 1d
	 *
	 * @param dataArray
	 */
	public UnsignedBytesData(byte[]... dataArray) {
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
		mHashCode = Arrays.hashCode(out);
	}

	/**
	 * 2d array will be flatten into 1d
	 *
	 * @param dataArray
	 */
	public UnsignedBytesData(char[]... dataArray) {
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
		mHashCode = Arrays.hashCode(out);
	}

	/**
	 * Mutable constructor, be carefully
	 *
	 * @param data
	 */
	public UnsignedBytesData(byte[] data) {
		if (data == null)
			throw new NullPointerException("Data must not be null");

		mData = bytesToChars(data);
		mHashCode = Arrays.hashCode(data);
	}

	/**
	 * Mutable constructor, be carefully
	 *
	 * @param data
	 */
	public UnsignedBytesData(char[] data) {
		if (data == null)
			throw new NullPointerException("Data must not be null");

		mData = data;
		mHashCode = Arrays.hashCode(data);
	}

	/**
	 * Mutable constructor, be carefully
	 *
	 * @param data
	 */
	public UnsignedBytesData(int[] data) {
		if (data == null)
			throw new NullPointerException("Data must not be null");

		mData = intsToChars(data);
		mHashCode = Arrays.hashCode(data);
	}

	public UnsignedBytesData(CharSequence hexData) {
		this(StringHelper.hexStringToInts(hexData.toString()));
	}

	UnsignedBytesData() {
	}

	@Override
	public int hashCode() {
		return mHashCode;
	}

	public boolean equals(Object other) {
		if (!mValid) {
			return false;
		}

		if (!(other instanceof UnsignedBytesData) && !(other instanceof char[]) &&
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
			otherData = ((UnsignedBytesData) other).getData();
		}

        if (mData.length == otherData.length) {
            return true;
        }

		return FastByteComparisons.equal(mData, otherData);
	}

	@Override
	public String toString() {
		return StringHelper.charsToHexString(mData);
	}

	@Override
	public int compareTo(@Nonnull UnsignedBytesData o) {
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
	 *
	 * @return hash bytes
	 */
	public char[] sha256() {
		return bytesToChars(HashUtil.sha256(charsToBytes(getData())));
	}

	/**
	 * Mutable sha256 hashing
	 *
	 * @return self instance
	 */
	public UnsignedBytesData sha256Mutable() {
		mData = sha256();
		return this;
	}

	/**
	 * Calculates sha3 hash from bytes
	 *
	 * @return
	 */
	public char[] sha3() {
		return bytesToChars(HashUtil.sha3(charsToBytes(getDataImmutable())));
	}

	/**
	 * Calculates sha3 hash from bytes and set new data to this object
	 *
	 * @return current object
	 */
	public UnsignedBytesData sha3Mutable() {
		mData = sha3();
		return this;
	}

	/**
	 * Calculates sha3 hash from bytes and returns raw mData
	 *
	 * @return
	 */
	public UnsignedBytesData sha3Data() {
		return new UnsignedBytesData(sha3());
	}

	/**
	 * Converts mData to hex string with uppercase option
	 *
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
	 *
	 * @return hex string
	 */
	public String toHexString() {
		return StringHelper.charsToHexString(getData());
	}

	/**
	 * Copies mData to new array and returns it.
	 *
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
	 *
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
	 *
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

	public UnsignedBytesData takeLastMutable(int len) {
		mData = (takeLast(len));
		return this;
	}

	public UnsignedBytesData takeFirstMutable(int len) {
		mData = takeFirst(len);
		return this;
	}

	public char[] dropLast() {
		char[] out = new char[size() - 1];
		System.arraycopy(getData(), 0, out, 0, size() - 1);
		return out;
	}

	public UnsignedBytesData dropLastMutable() {
		mData = dropLast();
		return this;
	}

	public char[] dropFirst() {
		char[] out = new char[size() - 1];
		System.arraycopy(getData(), 1, out, 0, size() - 1);
		return out;
	}

	public UnsignedBytesData dropFirstMutable() {
		mData = dropFirst();
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

	public UnsignedBytesData lpadMutable(int size) {
		mData = BytesHelper.lpad(size, getData());
		return this;
	}

	@Override
	public UnsignedBytesData clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		UnsignedBytesData out = new UnsignedBytesData();
		out.mValid = mValid;
		out.mData = new char[mData.length];
		System.arraycopy(mData, 0, out.mData, 0, mData.length);

		return out;
	}
}
