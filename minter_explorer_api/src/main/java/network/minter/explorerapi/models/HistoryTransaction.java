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

package network.minter.explorerapi.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.MinterHash;
import network.minter.mintercore.crypto.MinterPublicKey;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class HistoryTransaction implements Serializable {

    public MinterHash hash;
    public BigInteger nonce;
    public BigInteger block;
    public Date timestamp;
    public BigDecimal fee;
    public Type type;
    public Object data;
    public Status status;
    public String payload;
    public transient String username;
    public transient String avatarUrl;

    public enum Status {
        @SerializedName("success")
        Success,
        @SerializedName("error")
        Error,
    }

    public enum Type {
        @SerializedName("send")
        Send(TxSendCoinResult.class),
        @SerializedName("convert")
        ConvertCoin(TxConvertCoinResult.class),
        @SerializedName("createCoin")
        CreateCoin(TxCreateResult.class),
        @SerializedName("declareCandidacy")
        DeclareCandidacy(TxDeclareCandidacyResult.class),
        @SerializedName("delegate")
        Delegate(TxDelegateResult.class),
        @SerializedName("unbound")
        Unbound(TxUnboundResult.class),
        @SerializedName("redeemCheckData")
        RedeemCheck(TxRedeemCheckResult.class),
        @SerializedName("setCandidateOnData")
        SetCandidateOnline(TxSetCandidateOnlineOfflineResult.class),
        @SerializedName("setCandidateOffData")
        SetCandidateOffline(TxSetCandidateOnlineOfflineResult.class);

        Class<? extends TxResult> mCls;

        Type(Class<? extends TxResult> cls) {
            mCls = cls;
        }

        public Class<? extends TxResult> getCls() {
            return mCls;
        }
    }

    public boolean isIncoming(List<MinterAddress> addressList) {
        if (type != Type.Send) {
            return false;
        }

        return addressList.contains(this.<TxSendCoinResult>getData().to);
    }

    @SuppressWarnings("unchecked")
    public <T extends TxResult> T getData() {
        return (T) data;
    }

    public String getAvatar() {
        return avatarUrl == null ? "https://my.beta.minter.network/api/v1/avatar/by/user/1" : avatarUrl;
    }

    public HistoryTransaction setAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public HistoryTransaction setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryTransaction that = (HistoryTransaction) o;
        return block.equals(that.block) &&
                Objects.equals(fee, that.fee) &&
                Objects.equals(hash, that.hash) &&
                Objects.equals(nonce, that.nonce) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(type, that.type) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, nonce, block, timestamp, fee, type, data);
    }

    public interface TxResult extends Serializable {

    }

    public static class TxSendCoinResult implements TxResult {
        public MinterAddress from;
        public MinterAddress to;
        public String coin;
        public BigDecimal amount;
    }

    public static class TxCreateResult implements TxResult {
        public MinterAddress from;
        public String name;
        public String symbol;
        @SerializedName("initial_amount")
        public BigDecimal initialAmount;
        @SerializedName("initial_reserve")
        public BigDecimal initialReserve;
        @SerializedName("constant_reserve_ratio")
        public BigDecimal constantReserveRatio;
    }

    public static class TxConvertCoinResult implements TxResult {
        public MinterAddress from;
        @SerializedName("from_coin_symbol")
        public String fromCoin;
        @SerializedName("to_coin_symbol")
        public String toCoin;
        @SerializedName("value")
        public BigDecimal amount;
    }

    public static class TxDeclareCandidacyResult implements TxResult {
        public MinterAddress from;
        public MinterAddress address;
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
        public BigDecimal commission;
        public String coin;
        public BigDecimal stake;
    }

    public static class TxSetCandidateOnlineOfflineResult implements TxResult {
        public MinterAddress from;
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
    }

    public static class TxUnboundResult implements TxResult {
        public MinterAddress from;

    }

    public static class TxDelegateResult implements TxResult {
        public MinterAddress from;

    }

    public static class TxRedeemCheckResult implements TxResult {
        public MinterAddress from;

    }
}
