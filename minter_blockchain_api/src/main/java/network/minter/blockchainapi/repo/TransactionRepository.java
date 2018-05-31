package network.minter.blockchainapi.repo;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.blockchainapi.MinterBlockChainApi;
import network.minter.mintercore.MinterApi;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.blockchainapi.api.TransactionEndpoint;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.HistoryTransaction;
import network.minter.blockchainapi.models.operational.TxSendCoin;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

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
     * Get transactions by query
     *
     * @param query
     * @return
     * @see TQuery
     */
    public Call<BCResult<List<HistoryTransaction>>> getTransactions(@NonNull TQuery query) {
        return getService().getTransactions(checkNotNull(query, "Query required").build());
    }

    @Override
    protected void configureService(ApiService.Builder apiBuilder) {
        super.configureService(apiBuilder);
        apiBuilder.registerTypeAdapter(HistoryTransaction.class, new HistoryTransactionDeserializer());
    }

    @NonNull
    @Override
    protected Class<TransactionEndpoint> getServiceClass() {
        return TransactionEndpoint.class;
    }

    public static final class HistoryTransactionDeserializer implements JsonDeserializer<HistoryTransaction> {

        @Override
        public HistoryTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonNull() || !json.isJsonObject()) {
                return null;
            }

            final Gson gson = MinterBlockChainApi.getInstance().getGsonBuilder().create();

            HistoryTransaction out = gson.fromJson(json, HistoryTransaction.class);
            switch (out.type) {
                case SendCoin:
                    out.data = gson.fromJson(json.getAsJsonObject().get("data").getAsJsonObject(), TxSendCoin.class);
                    break;

                default:
                    out.data = null;
            }

            return out;
        }
    }

    public static class TQuery {
        private Map<String, String> mData = new HashMap<>();

        public TQuery setFrom(MinterAddress from) {
            return setFrom(from.toString());
        }

        public TQuery setTo(MinterAddress to) {
            return setTo(to.toString());
        }

        public TQuery setTo(String to) {
            mData.put("tx.to", normalizeAddress(to));
            return this;
        }

        public TQuery setFrom(String from) {
            mData.put("tx.from", normalizeAddress(from));
            return this;
        }

        public String build() {
            StringBuilder out = new StringBuilder();

            int i = 0;
            for (Map.Entry<String, String> v : mData.entrySet()) {
                out.append(v.getKey()).append("=").append("'").append(v.getValue()).append("'");
                if (i + 1 < mData.size()) {
                    out.append('&');
                }
            }

            return out.toString();
        }

        private String normalizeAddress(String in) {
            final String prefix = in.substring(0, 2);
            if (prefix.equals("Mx") || prefix.equals("mx") || prefix.equals("0x")) {
                return in.substring(2);
            }

            return in;
        }


    }
}
