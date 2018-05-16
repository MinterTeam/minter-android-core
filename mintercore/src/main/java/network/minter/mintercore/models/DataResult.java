package network.minter.mintercore.models;

import com.google.gson.annotations.SerializedName;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class DataResult<Result> {
    public ResultCode code = ResultCode.Success;
    @SerializedName("result")
    public Result result;
    @SerializedName("log")
    public String message;

    public enum ResultCode {
        Unknown(-1),
        @SerializedName("0") Success(0),
        @SerializedName("1") DecodeError(1),
        @SerializedName("2") InsufficientFunds(2),
        @SerializedName("3") UnknownTransactionType(3),
        @SerializedName("4") WrongNonce(4),
        @SerializedName("5") CoinNotExists(5),
        @SerializedName("6") CoinAlreadyExists(6),
        @SerializedName("7") WrongCrr(7),
        @SerializedName("8") CrossConvert(8),
        @SerializedName("9") CandidateExists(9),
        @SerializedName("10") WrongCommission(10),
        @SerializedName("11") CandidateNotFound(11);

        int resVal;

        ResultCode(int v) {
            resVal = v;
        }

        public int getValue() {
            return resVal;
        }

    }

    public boolean isSuccess() {
        return code == ResultCode.Success;
    }
}
