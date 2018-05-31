package network.minter.blockchainapi.repo;

import android.support.annotation.NonNull;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.blockchainapi.api.CoinEndpoint;
import network.minter.blockchainapi.models.Coin;
import network.minter.blockchainapi.models.BCResult;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;
import static network.minter.mintercore.internal.helpers.CollectionsHelper.asMap;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class CoinRepository extends DataRepository<CoinEndpoint> {
    public CoinRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Returns full coin information
     *
     * @param symbol Coin name (for example: MNT)
     * @return Full coin info
     */
    public Call<BCResult<Coin>> getCoinInfo(@NonNull String symbol) {
        return getService().getCoinInformation(checkNotNull(symbol, "Symbol required"));
    }

    /**
     * @param fromCoin Source coin
     * @param toCoin   Target coin
     * @param amount   Amount of exchange
     * @return
     */
    public Call<BCResult<Double>> estimateCoinExchangeReturn(@NonNull String fromCoin, @NonNull String toCoin,
                                                             double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        return getService().estimateCoinExchangeReturn(asMap(
                "from_coin", checkNotNull(fromCoin, "Source coin required"),
                "to_coin", checkNotNull(toCoin, "Target coin required"),
                "amount", String.valueOf(amount)
        ));
    }

    @NonNull
    @Override
    protected Class<CoinEndpoint> getServiceClass() {
        return CoinEndpoint.class;
    }
}
