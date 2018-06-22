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

import java.util.List;

import network.minter.my.models.AddressInfoResult;
import network.minter.my.models.MyResult;
import network.minter.my.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface MyInfoEndpoint {

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

