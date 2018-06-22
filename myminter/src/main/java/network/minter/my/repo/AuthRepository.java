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

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.my.api.MyAuthEndpoint;
import network.minter.my.models.LoginData;
import network.minter.my.models.MyResult;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.RegisterData;
import network.minter.my.models.User;
import network.minter.my.models.UsernameData;
import retrofit2.Call;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AuthRepository extends DataRepository<MyAuthEndpoint> {
    public AuthRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    @NonNull
    @Override
    protected Class<MyAuthEndpoint> getServiceClass() {
        return MyAuthEndpoint.class;
    }

    public Call<MyResult<User>> login(LoginData loginData) {
        return getService().login(loginData);
    }

    public Call<MyResult<ProfileRequestResult>> register(RegisterData registerData) {
        return getService().register(registerData);
    }

    public Call<MyResult<UsernameData>> checkUsernameAvailability(String username) {
        return getService().checkUsernameAvailability(username);
    }
}
