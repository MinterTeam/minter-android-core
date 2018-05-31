package network.minter.blockchainapi.api;

import java.util.List;

import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.HistoryTransaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface TransactionEndpoint {

    @GET("/api/transactions")
    Call<BCResult<List<HistoryTransaction>>> getTransactions(@Query("query") String urlEncodedQuery);
}
