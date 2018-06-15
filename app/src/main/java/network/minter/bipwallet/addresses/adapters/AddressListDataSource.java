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

package network.minter.bipwallet.addresses.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import network.minter.my.models.AddressData;
import network.minter.my.repo.AddressRepository;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressListDataSource extends PageKeyedDataSource<Integer, AddressData> {
    private final AddressRepository mRepo;
    private final MutableLiveData<NetworkState> mNetworkState;

    public enum NetworkState {
        Loading,
        Loaded,
        Failed,
    }

    private AddressListDataSource(AddressRepository repo, MutableLiveData<NetworkState> networkState) {
        mRepo = repo;
        mNetworkState = networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, AddressData> callback) {
        mNetworkState.postValue(NetworkState.Loading);
        rxCall(mRepo.getAddresses(1))
                .subscribe(res -> {
                    mNetworkState.postValue(NetworkState.Loaded);
                    callback.onResult(res.data, 0, res.getMeta().total, null, 2);
                }, t -> {
                    mNetworkState.postValue(NetworkState.Failed);
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, AddressData> callback) {
        mNetworkState.postValue(NetworkState.Loading);
        rxCall(mRepo.getAddresses(params.key))
                .subscribe(res -> {
                    mNetworkState.postValue(NetworkState.Loaded);
                    callback.onResult(res.data, params.key == 1 ? null : params.key - 1);
                }, t -> {
                    mNetworkState.postValue(NetworkState.Failed);
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, AddressData> callback) {
        mNetworkState.postValue(NetworkState.Loading);
        rxCall(mRepo.getAddresses(params.key))
                .subscribe(res -> {
                    mNetworkState.postValue(NetworkState.Loaded);
                    callback.onResult(res.data, params.key > res.getMeta().lastPage ? null : params.key + 1);
                }, t -> {
                    mNetworkState.postValue(NetworkState.Failed);
                });
    }

    public static class Factory extends DataSource.Factory<Integer, AddressData> {
        private final AddressRepository mAddressRepository;
        private final MutableLiveData<NetworkState> mNetworkState;

        public Factory(AddressRepository addressRepository) {
            this(addressRepository, new MutableLiveData<>());
        }

        public Factory(AddressRepository addressRepository, MutableLiveData<NetworkState> networkState) {
            mAddressRepository = addressRepository;
            mNetworkState = networkState;
        }


        @Override
        public DataSource<Integer, AddressData> create() {
            return new AddressListDataSource(mAddressRepository, mNetworkState);
        }
    }
}
