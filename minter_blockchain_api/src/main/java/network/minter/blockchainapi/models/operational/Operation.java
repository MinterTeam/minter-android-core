package network.minter.blockchainapi.models.operational;

import android.support.annotation.NonNull;

import network.minter.mintercore.util.RLP;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class Operation {
    /**
     * Encodes all operation fields via RLP
     *
     * @return encoded byte[]
     * @see RLP
     */
    @NonNull
    protected abstract byte[] encodeRLP();
    protected abstract void decodeRLP(@NonNull byte[] rlpEncodedData);

    protected abstract <T extends Operation, B extends Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx);

    public abstract static class Builder<Op extends Operation> {
        private Transaction<Op> mTx;

        Builder(Transaction<Op> op) {
            mTx = op;
        }

        public Transaction<Op> save() {
            return mTx;
        }

        protected Transaction<Op> getTx() {
            return mTx;
        }
    }
}
