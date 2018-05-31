package network.minter.my.api;

import network.minter.my.models.MyResult;
import network.minter.my.models.LoginData;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.RegisterData;
import network.minter.my.models.User;
import network.minter.my.models.UsernameData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface AuthEndpoint {
    @POST("/api/v1/login")
    Call<MyResult<User>> login(@Body LoginData data);

    @POST("/api/v1/register")
    Call<MyResult<ProfileRequestResult>> register(@Body RegisterData data);

    @GET("/api/v1/username/{username}")
    Call<MyResult<UsernameData>> checkUsernameAvailability(@Path("username") String username);

}
