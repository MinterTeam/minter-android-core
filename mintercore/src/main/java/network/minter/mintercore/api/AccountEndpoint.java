package network.minter.mintercore.api;

import java.util.Map;

import network.minter.mintercore.models.Balance;
import network.minter.mintercore.models.DataResult;
import network.minter.mintercore.models.TransactionSendResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface AccountEndpoint {

    @POST("/api/getBalance")
    Call<DataResult<Balance>> getBalance(@Body Map<String, String> data);

    @POST("/api/getTransactionCount")
    Call<DataResult<Long>> getTransactionCount(@Body Map<String, String> data);

    @POST("/api/sendTransaction")
    Call<TransactionSendResult> sendTransaction(@Body Map<String, String> data);


}
