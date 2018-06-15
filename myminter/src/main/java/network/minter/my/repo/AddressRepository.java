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

import java.util.List;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.my.api.AddressEndpoint;
import network.minter.my.models.AddressData;
import network.minter.my.models.MyResult;
import retrofit2.Call;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressRepository extends DataRepository<AddressEndpoint> {
    public AddressRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<MyResult<List<AddressData>>> getAddresses() {
        return getService().getAddresses();
    }

    public Call<MyResult<List<AddressData>>> getAddresses(int page) {
        return getService().getAddresses(page);
    }

    public Call<MyResult<List<AddressData>>> getAddressesWithEncrypted() {
        return getService().getAddressesWithEncrypted();
    }

    public Call<MyResult<Object>> delete(AddressData address) {
        return getService().deleteAddress(address.id);
    }

    public Call<MyResult<Object>> addAddress(AddressData data) {
        return getService().addAddress(data);
    }

    public Call<MyResult<Object>> setAddressMain(boolean isMain, AddressData data) {
        data.isMain = isMain;
        return getService().updateAddress(data.id, data);
    }


    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @NonNull
    @Override
    protected Class<AddressEndpoint> getServiceClass() {
        return AddressEndpoint.class;
    }

}
