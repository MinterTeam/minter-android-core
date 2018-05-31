package network.minter.my.api;

import java.util.List;

import network.minter.my.models.MyResult;
import network.minter.my.models.User;
import network.minter.my.models.AddressInfoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface InfoEndpoint {

    /**
     * Get address and related user information
     * @param address
     * @return
     */
    @GET("/api/v1/info/by/address/{address}")
    Call<MyResult<AddressInfoResult>> getAddressWithUserInfo(@Path("address") String address);

    /**
     * Get addresses and relates user information
     * @param addresses
     * @return
     */
    @GET("/api/v1/info/by/addresses")
    Call<MyResult<List<AddressInfoResult>>> getAddressesWithUserInfo(@Query("addresses[]") List<String> addresses);

    /**
     * Get user information by his username
     * @param username
     * @return
     */
    @GET("/api/v1/info/by/username/{username}")
    Call<MyResult<User.Data>> getUserInfoByUsername(@Path("username") String username);

}

