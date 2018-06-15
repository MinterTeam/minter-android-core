/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.my.api;

import java.util.Map;

import network.minter.my.models.MyResult;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface ProfileEndpoint {

    /**
     * Get current user profile data
     */
    @GET("/api/v1/profile")
    Call<MyResult<User.Data>> getProfile();

    /**
     * Update current user profile
     */
    @PUT("/api/v1/profile")
    Call<MyResult<ProfileRequestResult>> updateProfile(@Body User.Data data);

    /**
     * Update current user profile with specified map
     */
    @PUT("/api/v1/profile")
    @Headers({"Content-Type: application/json"})
    Call<MyResult<ProfileRequestResult>> updateProfile(@Body Map<String, String> data);

    /**
     * Get current user avatar url
     */
    @GET("/api/v1/profile/avatar")
    Call<MyResult<User.Avatar>> getAvatar();

    /**
     * Upload current user avatar (multipart data)
     * Supported formats: png, jpg
     * Maximum size: 500 kilobytes
     * Field name for multipart: avatar
     */
    @Multipart
    @POST("/api/v1/profile/avatar")
    Call<MyResult<User.Avatar>> updateAvatarMultipart(@Part MultipartBody.Part file);

    /**
     * Upload current user avatar (base64 encoded image)
     * Supported formats: png, jpg
     * Maximum size: 500 kilobytes
     */
    @FormUrlEncoded
    @POST("/api/v1/profile/avatar")
    Call<MyResult<User.Avatar>> updateAvatarBase64(@Field("avatarBase64") String file);

    /**
     * Delete avatar
     */
    @DELETE("/api/v1/profile/avatar")
    Call<MyResult<Void>> deleteAvatar();

    /**
     * 2-factor auth confirmation
     * @param id Confirmation hash
     * @param code Confirmation code
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/profile/confirm/{id}")
    Call<MyResult<Void>> confirmProfile(@Path("id") String id, @Field("code") String code);
}
