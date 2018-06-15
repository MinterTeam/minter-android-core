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

package network.minter.bipwallet.tx.models;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import network.minter.blockchainapi.models.HistoryTransaction;
import network.minter.blockchainapi.models.operational.OperationType;
import network.minter.blockchainapi.models.operational.TxSendCoin;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class HistoryTransactionAdapted {
    public BytesData hash;
    public long block;
    public OperationType type;
    public MinterAddress from;
    public MinterAddress to;
    public BigDecimal amount;
    public String coin;
    public BigInteger nonce;
    public BigInteger gasPrice;

    public HistoryTransactionAdapted(HistoryTransaction bcTransaction) {
        this.hash = bcTransaction.hash;
        this.block = bcTransaction.height;
        this.type = bcTransaction.type;
        this.from = bcTransaction.from;
        this.nonce = bcTransaction.nonce;
        this.gasPrice = bcTransaction.gasPrice;
        final TxSendCoin txData = bcTransaction.getData();
        this.to = txData.getTo();
        //@TODO
//        this.amount = txData.getValue()
        this.coin = txData.getCoin();
    }

    public HistoryTransactionAdapted(network.minter.explorerapi.models.HistoryTransaction exTransaction) {
        this.hash = exTransaction.hash;
        this.block = exTransaction.block;
        this.amount = exTransaction.data.amount;
        this.from = exTransaction.data.from;
        this.to = exTransaction.data.to;
        this.coin = exTransaction.data.coin;


//        this.type = OperationType.valueOf(exTransaction.type);
    }

    HistoryTransactionAdapted() {
    }

    @Override
    public int hashCode() {

        return Objects.hash(hash, block, type, from, to, amount, coin, nonce, gasPrice);
    }

    public static class TxResult {
        public BigInteger gasWanted;
        public BigInteger gasUsed;
        public List<HistoryTransaction.Tag> tags;
        public Object fee; //@TODO
    }

    public static class Tag {
        public String key;
        public String value;
    }
}
