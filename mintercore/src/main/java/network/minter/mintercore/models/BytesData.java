package network.minter.mintercore.models;

import android.support.annotation.NonNull;

import org.spongycastle.util.encoders.Hex;

import java.io.Serializable;
import java.util.Arrays;

import network.minter.ethereuman.crypto.HashUtil;
import network.minter.ethereuman.util.FastByteComparisons;
import network.minter.mintercore.helpers.BytesHelper;
import network.minter.mintercore.helpers.StringHelper;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesData implements Comparable<BytesData>, Serializable, Cloneable {
    protected byte[] mData;
    private boolean mValid = true;
    private int mHashCode = 0;

    @SuppressWarnings("CopyConstructorMissesField")
    public BytesData(BytesData data) {
        this(data.getData());
        mValid = true;
    }

    public BytesData(byte[] data) {
        if (data == null)
            throw new NullPointerException("Data must not be null");

        mData = data;
        mHashCode = Arrays.hashCode(data);
    }

    public BytesData(CharSequence hexData) {
        this(StringHelper.hexStringToBytes(hexData.toString()));
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    public boolean equals(Object other) {
        if (!(other instanceof BytesData))
            return false;
        byte[] otherData = ((BytesData) other).getData();
        return FastByteComparisons.compareTo(
                mData, 0, mData.length,
                otherData, 0, otherData.length) == 0;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new BytesData(getDataImmutable());
    }

    @Override
    public String toString() {
        return Hex.toHexString(mData);
    }

    @Override
    protected void finalize() throws Throwable {
        cleanup();
        super.finalize();
    }

    @Override
    public int compareTo(@NonNull BytesData o) {
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
     * Calculates sha3 hash from bytes
     *
     * @return
     */
    public byte[] sha3() {
        return HashUtil.sha3(getData());
    }

    /**
     * Calculates sha3 hash from bytes and set new data to this object
     *
     * @return current object
     */
    public BytesData sha3Mutable() {
        mData = sha3();
        return this;
    }

    /**
     * Calculates sha3 hash from bytes and returns raw mData
     *
     * @return
     */
    public BytesData sha3Data() {
        return new BytesData(sha3());
    }

    /**
     * Converts mData to hex string with uppercase option
     *
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
     *
     * @return hex string
     */
    public String toHexString() {
        return StringHelper.bytesToHexString(getData());
    }

    /**
     * Copies mData to new array and returns it.
     *
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
     *
     * @see #isValid()
     */
    public void cleanup() {
        for (int i = 0; i < size(); i++) {
            getData()[i] = '\0';
        }
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

    public byte[] takeFirst(int len) {
        if (len < 0) {
            throw new IllegalArgumentException("Length can't be less than 0");
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
        if (len >= size()) {
            throw new IllegalArgumentException("Length can't be more than mData size");
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


}
