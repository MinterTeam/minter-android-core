package network.minter.explorerapi.repo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.explorerapi.api.TransactionEndpoint;
import network.minter.explorerapi.models.ExpResult;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionRepository extends DataRepository<TransactionEndpoint> {
    public TransactionRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address) {
        Map<String, String> query = new HashMap<>();
        query.put("address", address.toString());

        return getService().getTransactions(query);
    }

    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses) {
        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getService().getTransactions(out);
    }

    @NonNull
    @Override
    protected Class<TransactionEndpoint> getServiceClass() {
        return TransactionEndpoint.class;
    }
}
