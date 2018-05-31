package network.minter.blockchainapi.api;

import java.util.Map;

import network.minter.blockchainapi.models.Coin;
import network.minter.blockchainapi.models.BCResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface CoinEndpoint {

    @GET("/api/coinInfo/{symbol}")
    Call<BCResult<Coin>> getCoinInformation(@Query("symbol") String coin);

    @GET("/api/estimateCoinExchangeReturn")
    Call<BCResult<Double>> estimateCoinExchangeReturn(@QueryMap Map<String, String> data);

}
