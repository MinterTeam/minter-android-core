package network.minter.blockchainapi.models.operational;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;
import network.minter.mintercore.MinterApi;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.helpers.BytesHelper;
import network.minter.mintercore.internal.helpers.StringHelper;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

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

        /**
         * You MUST multiply this rawValue on {@code Transaction#VALUE_MUL} by yourself
         * @param rawValue
         * @param radix
         * @return
         */
        public Builder setRawValue(String rawValue, int radix) {
            return setValue(new BigInteger(rawValue, radix));
        }

        /**
         *
         * @param decimalValue Floating point string value. Precision up to 18 digits: 0.101010101010101010
         * @return
         */
        public Builder setValue(@NonNull final CharSequence decimalValue) {
            checkNotNull(decimalValue);
            return setValue(new BigDecimal(decimalValue.toString()));
        }

        /**
         * Precision up to 18 digits
         * @see Transaction#VALUE_MUL
         * @param value
         * @return
         */
        public Builder setValue(BigDecimal value) {
            TxSendCoin.this.value = value.setScale(18, BigDecimal.ROUND_UP).multiply(new BigDecimal(Transaction.VALUE_MUL)).toBigInteger();
            return this;
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
