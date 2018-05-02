package network.minter.mintercore.repo;

import android.support.annotation.NonNull;

import java.util.List;

import network.minter.mintercore.api.ApiService;
import network.minter.mintercore.api.TransactionEndpoint;
import network.minter.mintercore.models.DataResult;
import network.minter.mintercore.models.HistoryTransaction;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;
import static network.minter.mintercore.internal.helpers.CollectionsHelper.asMap;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionRepository extends DataRepository<TransactionEndpoint> {
    public TransactionRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get outgoing transactions by address
     *
     * @param fromAddress
     * @return
     */
    public Call<DataResult<List<HistoryTransaction>>> getTransactionsFrom(@NonNull String fromAddress) {
        return getService().getTransactionsFrom(asMap(
                "tx.from", checkNotNull(fromAddress, "Address required")
        ));
    }

    /**
     * Get incoming transactions by address
     *
     * @param toAddress
     * @return
     */
    public Call<DataResult<List<HistoryTransaction>>> getTransactionsTo(@NonNull String toAddress) {
        return getService().getTransactionsFrom(asMap(
                "tx.to", checkNotNull(toAddress, "Address required")
        ));
    }

    @NonNull
    @Override
    protected Class<TransactionEndpoint> getServiceClass() {
        return TransactionEndpoint.class;
    }
}
