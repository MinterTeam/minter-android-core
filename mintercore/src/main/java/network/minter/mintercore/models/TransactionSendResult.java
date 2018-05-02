package network.minter.mintercore.models;

import com.google.gson.annotations.SerializedName;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionSendResult extends DataResult<Void> {
    @SerializedName("tx_hash")
    public String txHash;
}
