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

package network.minter.bipwallet.coins.repos;

import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.internal.Wallet;
import network.minter.explorerapi.models.AddressData;
import network.minter.explorerapi.repo.AddressRepository;
import network.minter.mintercore.crypto.MinterAddress;
import timber.log.Timber;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ExplorerBalanceFetcher implements ObservableOnSubscribe<AddressData> {
    @GuardedBy("mLock")
    private final Map<MinterAddress, List<AddressData.CoinBalance>> mOut = new HashMap<>();
    private final Object mLock = new Object();
    private final List<MinterAddress> mAddresses;
    private final CountDownLatch mWaiter;
    private final AddressRepository mAddressRepository;

    public ExplorerBalanceFetcher(AddressRepository addressRepository, @NonNull final List<MinterAddress> addresses) {
        mAddresses = addresses;
        mAddressRepository = addressRepository;
        mWaiter = new CountDownLatch(addresses.size());
    }

    @Override
    public void subscribe(ObservableEmitter<AddressData> emitter) throws Exception {
        if (mAddresses.isEmpty()) {
            Timber.w("No one address");
            emitter.onNext(null);
            emitter.onComplete();
            return;
        }

        final AddressData[] totalData = {null};

        for (MinterAddress address : mAddresses) {
            rxCall(mAddressRepository.getAddressData(address))
                    .subscribeOn(Schedulers.io())
                    .subscribe(res -> {
                        if (totalData[0] == null) {
                            totalData[0] = res.result;
                        }
                        synchronized (mLock) {
                            if (!mOut.containsKey(address)) {
                                mOut.put(address, new ArrayList<>());
                            }

                            mOut.get(address).addAll(res.result.coins.values());
                        }

                        mWaiter.countDown();
                    }, t -> {
                        Wallet.Rx.errorHandler().accept(t);
                        mWaiter.countDown();
                    });
        }

        mWaiter.await();
        final List<AddressData.CoinBalance> out = new ArrayList<>();
        for (List<AddressData.CoinBalance> items : mOut.values()) {
            out.addAll(items);
        }

        Map<String, AddressData.CoinBalance> aggregator = new LinkedHashMap<>();
        for (AddressData.CoinBalance balance : out) {
            if (!aggregator.containsKey(balance.coin)) {
                aggregator.put(balance.coin, balance);
            } else {
                aggregator.get(balance.coin).getBalance().add(balance.getBalance());
            }
        }

        AddressData data = new AddressData();
        data.coins = aggregator;
        if (totalData[0] != null) {
            data.txCount = totalData[0].txCount;
            data.bipTotal = totalData[0].bipTotal;
            data.usdTotal = totalData[0].usdTotal;
        } else {
            data.txCount = 0;
            data.bipTotal = new BigDecimal(0);
            data.usdTotal = new BigDecimal(0);
        }

        emitter.onNext(data);
        emitter.onComplete();
    }
}
