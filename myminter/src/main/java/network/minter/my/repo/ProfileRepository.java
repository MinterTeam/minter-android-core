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

import java.util.Map;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.mintercore.internal.helpers.CollectionsHelper;
import network.minter.my.api.MyProfileEndpoint;
import network.minter.my.models.MyResult;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.User;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ProfileRepository extends DataRepository<MyProfileEndpoint> {
    public ProfileRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<MyResult<User.Data>> getProfile() {
        return getService().getProfile();
    }

    public Call<MyResult<ProfileRequestResult>> updateProfile(@NonNull User.Data data) {
        checkNotNull(data);
        return getService().updateProfile(data);
    }

    public Call<MyResult<ProfileRequestResult>> updateField(String field, String value) {
        Map<String, String> data = CollectionsHelper.asMap(field, value);
        return getService().updateProfile(data);
    }

    public Call<MyResult<User.Avatar>> updateAvatar(String b64) {
        return getService().updateAvatarBase64(b64);
    }

    @NonNull
    @Override
    protected Class<MyProfileEndpoint> getServiceClass() {
        return MyProfileEndpoint.class;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }
}
