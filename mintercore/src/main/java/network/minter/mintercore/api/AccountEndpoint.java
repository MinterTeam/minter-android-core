package network.minter.mintercore.api;

import java.util.Map;

import network.minter.mintercore.models.Balance;
import network.minter.mintercore.models.DataResult;
import network.minter.mintercore.models.TransactionSendResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface AccountEndpoint {

    @GET("/api/balance/{address}")
    Call<DataResult<Balance>> getBalance(@Query("address") String address);

    @GET("/api/transactionCount/{address}")
    Call<DataResult<Long>> getTransactionCount(@Query("address") String address);

    @POST("/api/sendTransaction")
    Call<TransactionSendResult> sendTransaction(@Body Map<String, String> data);


}
