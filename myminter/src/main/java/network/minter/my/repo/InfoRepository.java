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

package network.minter.my.repo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.my.api.InfoEndpoint;
import network.minter.my.models.AddressInfoResult;
import network.minter.my.models.MyResult;
import network.minter.my.models.User;
import retrofit2.Call;

/**
 * MyMinter API SDK. May 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class InfoRepository extends DataRepository<InfoEndpoint> {
    public InfoRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<MyResult<AddressInfoResult>> getAddressWithUserInfo(String address) {
        return getService().getAddressWithUserInfo(address);
    }

    public Call<MyResult<AddressInfoResult>> getAddressWithUserInfo(MinterAddress address) {
        return getAddressWithUserInfo(address.toString());
    }

    public Call<MyResult<List<AddressInfoResult>>> getAddressesWithUserInfoByStrings(List<String> addresses) {
        return getService().getAddressesWithUserInfo(addresses);
    }

    public Call<MyResult<List<AddressInfoResult>>> getAddressesWithUserInfo(List<MinterAddress> addresses) {
        final List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getAddressesWithUserInfoByStrings(out);
    }

    public Call<MyResult<User.Data>> getUserInfoByUsername(String username) {
        return getService().getUserInfoByUsername(username);
    }

    public Call<MyResult<User.Data>> getUserInfoByUser(User user) {
        return getUserInfoByUser(user.data);
    }

    public Call<MyResult<User.Data>> getUserInfoByUser(User.Data userData) {
        return getUserInfoByUsername(userData.username);
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @NonNull
    @Override
    protected Class<InfoEndpoint> getServiceClass() {
        return InfoEndpoint.class;
    }

}
