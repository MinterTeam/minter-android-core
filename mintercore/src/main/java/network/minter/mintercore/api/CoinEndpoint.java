package network.minter.mintercore.api;

import java.util.Map;

import network.minter.mintercore.models.Coin;
import network.minter.mintercore.models.DataResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface CoinEndpoint {

    @POST("/api/getCoinInfo")
    Call<DataResult<Coin>> getCoinInformation(@Body Map<String, String> data);

    @POST("/api/estimateCoinExchangeReturn")
    Call<DataResult<Double>> estimateCoinExchangeReturn(@Body Map<String, String> data);

}
