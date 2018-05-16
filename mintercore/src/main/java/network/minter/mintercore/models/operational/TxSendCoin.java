package network.minter.mintercore.models.operational;

import android.support.annotation.NonNull;

import java.math.BigInteger;

import network.minter.ethereuman.util.DecodeResult;
import network.minter.ethereuman.util.RLP;
import network.minter.mintercore.MinterApi;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.helpers.BytesHelper;
import network.minter.mintercore.helpers.StringHelper;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxSendCoin extends Operation {
    String coin = MinterApi.DEFAULT_COIN;
    MinterAddress to;
    BigInteger value;

    TxSendCoin() {
    }

    public long getValue() {
        return value.divide(new BigInteger(String.valueOf(Transaction.VALUE_MUL))).longValue();
    }

    public BigInteger getValueBigInteger() {
        return value;
    }

    public MinterAddress getTo() {
        return new MinterAddress(to);
    }

    public String getCoin() {
        return coin.replace("\0", "");
    }

    byte[] fromRawRlp(int idx, Object[] raw) {
        return (byte[]) raw[idx];
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        byte[] to = this.to.getData();
        to = BytesHelper.lpad(20, to);

        return RLP.encode(new Object[]{coin, to, value});
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        coin = StringHelper.bytesToString(fromRawRlp(0, decoded));
        to = new MinterAddress(fromRawRlp(1, decoded));
        value = new BigInteger(fromRawRlp(2, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(
            Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxSendCoin>) rawTx);
    }

    public final class Builder extends Operation.Builder<TxSendCoin> {
        Builder(Transaction<TxSendCoin> op) {
            super(op);
        }

        public Builder setCoin(final String coin) {
            TxSendCoin.this.coin = StringHelper.strrpad(10, coin);
            return this;
        }

        public Builder setTo(MinterAddress publicKey) {
            to = publicKey;
            return this;
        }

        public Builder setTo(String address) {
            return setTo(new MinterAddress(address));
        }

        public Builder setValue(long value) {
            return setValue(new BigInteger(String.valueOf(value), 10));
        }

        public Builder setValue(String value, int radix) {
            return setValue(new BigInteger(value, radix));
        }

        public Builder setValue(Double value) {
            return setValue(new BigInteger(String.valueOf((long) (value * Transaction.VALUE_MUL))));
        }

        public Transaction<TxSendCoin> build() {
            getTx().setData(TxSendCoin.this);
            return getTx();
        }

        private Builder setValue(BigInteger value) {
            TxSendCoin.this.value = value.multiply(new BigInteger(Transaction.VALUE_MUL.toString(), 10));
            return this;
        }

    }


}
