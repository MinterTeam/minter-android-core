package network.minter.explorerapi.api;

import java.util.List;
import java.util.Map;

import network.minter.explorerapi.models.ExpResult;
import network.minter.explorerapi.models.HistoryTransaction;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface TransactionEndpoint {

    @GET("/api/v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@QueryMap Map<String, String> query);

    @GET("/api/v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@Query("addresses[]") List<String> addresses);
}
