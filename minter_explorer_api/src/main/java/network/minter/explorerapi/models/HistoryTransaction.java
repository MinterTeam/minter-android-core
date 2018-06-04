package network.minter.explorerapi.models;

import java.math.BigInteger;
import java.util.Date;

import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class HistoryTransaction {
    public BytesData hash;
    public BigInteger nonce;
    public long block;
    public Date timestamp;
    public double fee;
    public String type;
    public TxResult data;

    public static class TxResult {
        public MinterAddress from;
        public MinterAddress to;
        public String coin;
        public double amount;
    }

    //    public TxResult txResult;
    //    @SerializedName("tx_result")
    //    public BigInteger gasPrice;
//    public MinterAddress from;
//    public String payload; //@TODO what is it?

//    public <T extends Operation> T getData() {
//        return (T) data;
//    }

//    public static class TxResult {
//        public BigInteger gasWanted;
//        public BigInteger gasUsed;
//        public List<Tag> tags;
//        public Object fee; //@TODO
//    }
//
//    public static class Tag {
//        public String key;
//        public String value;
//    }
}
