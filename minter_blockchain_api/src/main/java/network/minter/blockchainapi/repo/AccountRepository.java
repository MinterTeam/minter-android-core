package network.minter.blockchainapi.repo;

import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import network.minter.blockchainapi.api.AccountEndpoint;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.blockchainapi.models.Balance;
import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.TransactionSendResult;
import network.minter.blockchainapi.models.operational.TransactionSign;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;
import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;
import static network.minter.mintercore.internal.helpers.CollectionsHelper.asMap;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AccountRepository extends DataRepository<AccountEndpoint> {
    public AccountRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<BCResult<Balance>> getBalance(@NonNull MinterAddress key) {
        checkNotNull(key, "Public key required!");
        return getBalance(key.toString());
    }

    /**
     * Returns balance result data for specified address
     *
     * @param address
     * @return
     */
    public Call<BCResult<Balance>> getBalance(@NonNull String address) {
        return getService().getBalance(checkNotNull(address, "Address required!"));
    }

    public Call<BCResult<Long>> getTransactionCount(@NonNull MinterAddress key) {
        checkNotNull(key, "Public key required!");
        return getTransactionCount(key.toString());
    }

    /**
     * Returns the number of transactions sent from an address
     *
     * @param address fq address
     * @return Prepared request with transaction count result
     */
    public Call<BCResult<Long>> getTransactionCount(@NonNull String address) {
        return getService().getTransactionCount(checkNotNull(address, "Address required!"));
    }

    /**
     * SendCoin transaction
     *
     * @param transactionSign Raw signed TX
     * @return Prepared request
     * @see TransactionSendResult
     */
    public Call<TransactionSendResult> sendTransaction(@NonNull TransactionSign transactionSign) {
        return getService().sendTransaction(
                asMap("transaction", transactionSign.getTxSign())
        );
    }

    @Override
    protected void configureService(ApiService.Builder apiBuilder) {
        super.configureService(apiBuilder);
        apiBuilder.registerTypeAdapter(Balance.class, new CoinBalanceDeserializer());
    }

    @NonNull
    @Override
    protected Class<AccountEndpoint> getServiceClass() {
        return AccountEndpoint.class;
    }

    public static class CoinBalanceDeserializer implements JsonDeserializer<Balance> {
        @Override
        public Balance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            }

            Balance balance = new Balance();

            JsonObject o = json.getAsJsonObject();

            final Map<String, Balance.CoinBalance> out = new HashMap<>();
            for (String key : o.keySet()) {
                final String uKey = key.toUpperCase();
                final Balance.CoinBalance b = new Balance.CoinBalance();
                b.balance = new BigInteger(o.get(uKey).getAsString());
                b.coin = uKey;
                out.put(uKey, b);
            }

            balance.coins = out;

            return balance;
        }
    }


}
