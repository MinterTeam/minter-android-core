package network.minter.explorerapi.models;

import com.google.gson.annotations.SerializedName;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ExpResult<Result> {
    @SerializedName("data")
    public Result result;
    public Object links;
    public Object meta;
}
