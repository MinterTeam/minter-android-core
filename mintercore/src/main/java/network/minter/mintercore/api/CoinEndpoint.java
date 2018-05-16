package network.minter.mintercore.api;

import java.util.Map;

import network.minter.mintercore.models.Coin;
import network.minter.mintercore.models.DataResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface CoinEndpoint {

    @GET("/api/coinInfo/{symbol}")
    Call<DataResult<Coin>> getCoinInformation(@Query("symbol") String coin);

    @GET("/api/estimateCoinExchangeReturn")
    Call<DataResult<Double>> estimateCoinExchangeReturn(@QueryMap Map<String, String> data);

}
