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
        @SerializedName("0")
        Success(0),
        @SerializedName("1")
        Unknown(1),
        @SerializedName("200")
        CoinNotFound(200),
        @SerializedName("300")
        InsufficientFundsForTransaction(300),
        @SerializedName("400")
        NonceTooLow(400),
        @SerializedName("401")
        NonceToHigh(401),
        @SerializedName("500")
        IncorrectSignature(500),
        @SerializedName("600")
        IncorrectTransactionData(600),
        @SerializedName("900")
        UnknownError(900);

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
