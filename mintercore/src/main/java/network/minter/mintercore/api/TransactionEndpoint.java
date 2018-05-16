package network.minter.mintercore.api;

import java.util.List;
import java.util.Map;

import network.minter.mintercore.models.DataResult;
import network.minter.mintercore.models.HistoryTransaction;
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
public interface TransactionEndpoint {

    @GET("/api/transactions")
    Call<DataResult<List<HistoryTransaction>>> getTransactions(@Query("query") String urlEncodedQuery);

//    @GET("/api/getTransactions")
//    Call<DataResult<List<HistoryTransaction>>> getTransactionsTo(@Body Map<String, String> data);
}
