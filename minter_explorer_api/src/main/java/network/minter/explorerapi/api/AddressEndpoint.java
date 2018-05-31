package network.minter.explorerapi.api;

import network.minter.explorerapi.models.AddressData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface AddressEndpoint {
    @GET("/api/v1/address/{address}")
    Call<AddressData> getAddressData(@Path("address") String address);
}
