package network.minter.blockchainapi.models.operational;

import android.support.annotation.NonNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxConvertCoin extends Operation {
    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return new byte[0];
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {

    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(
            Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxConvertCoin>) rawTx);
    }


    public final class Builder extends Operation.Builder<TxConvertCoin> {

        Builder(Transaction<TxConvertCoin> op) {
            super(op);
        }
    }
}
