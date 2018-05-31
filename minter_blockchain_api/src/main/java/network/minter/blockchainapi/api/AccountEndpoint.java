package network.minter.blockchainapi.api;

import java.util.Map;

import network.minter.blockchainapi.models.Balance;
import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.TransactionSendResult;
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
    Call<BCResult<Balance>> getBalance(@Query("address") String address);

    @GET("/api/transactionCount/{address}")
    Call<BCResult<Long>> getTransactionCount(@Query("address") String address);

    @POST("/api/sendTransaction")
    Call<TransactionSendResult> sendTransaction(@Body Map<String, String> data);


}
