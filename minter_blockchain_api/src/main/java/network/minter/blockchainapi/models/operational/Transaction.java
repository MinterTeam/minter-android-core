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

package network.minter.blockchainapi.models.operational;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.mintercore.MinterSDK;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.NativeSecp256k1;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;
import timber.log.Timber;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class Transaction<OperationData extends Operation> {
    public final static BigInteger VALUE_MUL = new BigInteger("1000000000000000000", 10);
    public final static BigDecimal VALUE_MUL_DEC = new BigDecimal("1000000000000000000");
    private BigInteger mNonce;
    private BigInteger mGasPrice = new BigInteger("1000000000", 10);
    private OperationType mType = OperationType.SendCoin;
    private OperationData mOperationData;
    private BigInteger mV = new BigInteger("1");
    private BigInteger mR = new BigInteger("0");
    private BigInteger mS = new BigInteger("0");

    private Transaction(BigInteger nonce) {
        mNonce = nonce;
    }

    private Transaction() {
    }

    public static <T extends Operation> Transaction<T> fromEncoded(String base64encoded, Class<T> type) {
        checkArgument(base64encoded != null && base64encoded.length() > 0, "Base64 encoded transaction is empty");
        checkArgument(type != null, "Class of transaction type must be set");
        final BytesData bd = new BytesData(Base64.decode(base64encoded, Base64.DEFAULT));
        final DecodeResult rlp = RLP.decode(bd.getData(), 0);
        final Object[] decoded = (Object[]) rlp.getDecoded();

        Transaction<T> transaction = new Transaction<>();
        transaction.decodeRLP(decoded);

        checkArgument(transaction.mType.getOpClass().equals(type),
                      "Passed transaction class does not matches with incoming data transaction type. Given: %s, expected: %s",
                      type.getName(),
                      transaction.mType.getOpClass().getName()
        );

        try {
            transaction.mOperationData = (T) transaction.mType.getOpClass().newInstance();
            transaction.mOperationData.decodeRLP(transaction.fromRawRlp(3, decoded));
        } catch (InstantiationException | IllegalAccessException e) {
            Timber.e(e, "Unable to decode transaction");
            return null;
        }

        return transaction;
    }

    public static TxSendCoin.Builder newSendTransaction(BigInteger nonce, BigInteger gasPrice) {
        Transaction<TxSendCoin> tx = new Builder<TxSendCoin>(nonce)
                .setGasPrice(gasPrice)
                .setType(OperationType.SendCoin)
                .build();

        return new TxSendCoin().new Builder(tx);
    }

    public BigInteger getNonce() {
        return mNonce;
    }

    public BigInteger getGasPrice() {
        return mGasPrice;
    }

    public OperationType getType() {
        return mType;
    }

    public TransactionSign sign(@NonNull final PrivateKey privateKey) {
        final BytesData rawTxData = new BytesData(encode(true));
        final BytesData hash = rawTxData.sha3Data();

        NativeSecp256k1.RecoverableSignature signature;

        long ctx = NativeSecp256k1.contextCreate();
        try {
            signature = NativeSecp256k1.signRecoverableSerialized(ctx, hash.getData(), privateKey.getData());
        } finally {
            // DON'T forget cleanup to avoid leaks
            NativeSecp256k1.contextCleanup(ctx);
        }

        if (signature == null) {
            return null;
        }

        mV = new BigInteger(signature.v);
        mR = new BigInteger(signature.r);
        mS = new BigInteger(signature.s);

        Timber.i(signature.toString());

        return new TransactionSign(MinterSDK.PREFIX_ADDRESS + new BytesData(encode(false)).toHexString());
    }

    public OperationData getData() {
        return mOperationData;
    }

    Transaction<OperationData> setData(OperationData operationData) {
        mOperationData = operationData;
        return this;
    }

    byte[] fromRawRlp(int idx, Object[] raw) {
        return (byte[]) raw[idx];
    }

    /**
     * Object[] contains exact 7 byte[]
     *
     * @param raw
     */
    void decodeRLP(Object[] raw) {
        mNonce = new BigInteger(fromRawRlp(0, raw));
        mGasPrice = new BigInteger(fromRawRlp(1, raw));
        mType = OperationType.findByValue(new BigInteger(fromRawRlp(2, raw)));
        mV = new BigInteger(fromRawRlp(4, raw));
        mR = new BigInteger(fromRawRlp(5, raw));
        mS = new BigInteger(fromRawRlp(6, raw));
    }

    byte[] encode(boolean forSignature) {
        final byte[] data = mOperationData.encodeRLP();
        if (forSignature) {
            return RLP.encode(new Object[]{
                    mNonce, mGasPrice, mType.getValue(),
                    data
            });
        }

        return RLP.encode(new Object[]{
                mNonce, mGasPrice, mType.getValue(),
                data,
                mV, mR, mS
        });
    }

    public static class Builder<Op extends Operation> {
        private final Transaction<Op> mTx;

        public Builder(BigInteger nonce) {
            mTx = new Transaction<>(nonce);
        }

        public Builder<Op> setNonce(BigInteger nonce) {
            mTx.mNonce = nonce;
            return this;
        }

        public Builder<Op> setGasPrice(BigInteger gasPrice) {
            mTx.mGasPrice = gasPrice;
            return this;
        }

        Builder<Op> setType(OperationType type) {
            mTx.mType = type;
            return this;
        }

        Transaction<Op> build() {
            return mTx;
        }


    }

}
