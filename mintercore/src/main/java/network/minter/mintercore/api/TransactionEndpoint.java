package network.minter.mintercore.api;

import java.util.List;
import java.util.Map;

import network.minter.mintercore.models.DataResult;
import network.minter.mintercore.models.HistoryTransaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface TransactionEndpoint {

    @POST("/api/getTransactions")
    Call<DataResult<List<HistoryTransaction>>> getTransactionsFrom(@Body Map<String, String> data);

    @POST("/api/getTransactions")
    Call<DataResult<List<HistoryTransaction>>> getTransactionsTo(@Body Map<String, String> data);
}
