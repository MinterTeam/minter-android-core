package network.minter.my.api;

import java.util.List;

import network.minter.my.models.AddressData;
import network.minter.my.models.MyResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface AddressEndpoint {

    /**
     * Get current user address list
     * @return
     */
    @GET("/api/v1/addresses")
    Call<MyResult<List<AddressData>>> getAddresses();

    /**
     * Get current user address list with encrypted data
     * @return
     */
    @GET("/api/v1/addresses/encrypted")
    Call<MyResult<List<AddressData>>> getAddressesWithEncrypted();

    /**
     * Get single address information
     * @param addressId
     * @return
     */
    @GET("/api/v1/addresses/{id}")
    Call<MyResult<AddressData>> getAddress(@Path("id") int addressId);

    /**
     * Get single address information with encrypted data
     * @param addressId
     * @return
     */
    @GET("/api/v1/addresses/{id}/encrypted")
    Call<MyResult<AddressData>> getAddressWithEncrypted(@Path("id") int addressId);

    /**
     * Create new address
     * @see AddressData
     * @param data
     * @return
     */
    @POST("/api/v1/addresses")
    Call<MyResult<Void>> addAddress(@Body AddressData data);

    /**
     * Update existent address
     * @param addressId
     * @see AddressData#id
     * @param data
     * @see AddressData
     * @return
     */
    @PUT("/api/v1/addresses/{id}")
    Call<MyResult<Void>> updateAddress(@Path("id") int addressId, @Body AddressData data);

    /**
     * Delete existent address by it id
     * @param addressId
     * @return
     */
    @DELETE("/api/v1/addresses/{id}")
    Call<MyResult<Void>> deleteAddress(@Path("id") long addressId);






}
