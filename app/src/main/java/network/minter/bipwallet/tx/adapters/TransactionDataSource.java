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

package network.minter.bipwallet.tx.adapters;

import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.helpers.DateHelper;
import network.minter.explorerapi.models.ExpResult;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.my.models.AddressInfoResult;
import network.minter.my.repo.InfoRepository;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionDataSource extends PageKeyedDataSource<Integer, TransactionItem> {

    private TransactionRepository mRepo;
    private InfoRepository mInfoRepo;
    private List<MinterAddress> mAddressList;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    private DateTime mLastDate;

    public TransactionDataSource(TransactionRepository repo, InfoRepository infoRepo, List<MinterAddress> addresses) {
        mRepo = repo;
        mInfoRepo = infoRepo;
        mAddressList = addresses;
    }

    public static ObservableSource<ExpResult<List<HistoryTransaction>>> mapAddressesInfo(InfoRepository infoRepo, ExpResult<List<HistoryTransaction>> items) {
        if (items.result == null || items.result.isEmpty()) {
            return Observable.just(items);
        }

        List<MinterAddress> toFetch = new ArrayList<>();
        final Map<MinterAddress, List<HistoryTransaction>> toFetchAddresses = new LinkedHashMap<>(items.result.size());
        for (HistoryTransaction tx : items.result) {
            final MinterAddress add;
            if (tx.isIncoming()) {
                add = tx.data.from;
            } else {
                add = tx.data.to;
            }

            if (!toFetch.contains(add)) {
                toFetch.add(add);
            }

            if (!toFetchAddresses.containsKey(add)) {
                toFetchAddresses.put(add, new ArrayList<>());
            }
            toFetchAddresses.get(add).add(tx);
        }

        return rxCall(infoRepo.getAddressesWithUserInfo(toFetch))
                .map(listInfoResult -> {
                    for (AddressInfoResult info : listInfoResult.data) {
                        for (HistoryTransaction t : toFetchAddresses.get(info.address)) {
                            t.setUsername(info.user.username).setAvatar(info.user.getAvatar().getUrl());
                        }
                    }

                    return items;
                });
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, TransactionItem> callback) {
        rxCall(mRepo.getTransactions(mAddressList, 1))
                .switchMap(items -> mapAddressesInfo(mInfoRepo, items))
                .map(this::groupByDate)
                .doOnSubscribe(d -> mDisposables.add(d))
                .subscribe(res -> {
                    callback.onResult(res.items, null, res.getMeta().currentPage + 1);
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TransactionItem> callback) {
        rxCall(mRepo.getTransactions(mAddressList, params.key))
                .switchMap(items -> mapAddressesInfo(mInfoRepo, items))
                .map(this::groupByDate)
                .doOnSubscribe(d -> mDisposables.add(d))
                .subscribe(res -> {
                    callback.onResult(res.items, params.key == 1 ? null : params.key - 1);
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TransactionItem> callback) {
        rxCall(mRepo.getTransactions(mAddressList, params.key))
                .switchMap(items -> mapAddressesInfo(mInfoRepo, items))
                .map(this::groupByDate)
                .doOnSubscribe(d -> mDisposables.add(d))
                .subscribe(res -> {
                    callback.onResult(res.items, params.key + 1 > res.getMeta().lastPage ? null : params.key + 1);
                });
    }

    @Override
    public void invalidate() {
        super.invalidate();
        mDisposables.dispose();
    }

    private String lastDay() {
        if (DateHelper.compareFlatDay(mLastDate, new DateTime())) {
            return Wallet.app().res().getString(R.string.today);
        }

        return mLastDate.toString(DateTimeFormat.forPattern("EEEE, dd MMMM"));
    }

    private DateTime dt(Date d) {
        return new DateTime(d);
    }

    private MetaTx groupByDate(ExpResult<List<HistoryTransaction>> res) {
        List<TransactionItem> out = new ArrayList<>();
        for (HistoryTransaction tx : res.result) {
            if (mLastDate == null) {
                mLastDate = new DateTime(tx.timestamp);
                out.add(new HeaderItem(lastDay()));
            } else if (!DateHelper.compareFlatDay(mLastDate, dt(tx.timestamp))) {
                mLastDate = dt(tx.timestamp);
                out.add(new HeaderItem(lastDay()));
            }

            out.add(new TxItem(tx));
        }

        final MetaTx metaTx = new MetaTx();
        metaTx.items = out;
        metaTx.meta = res.getMeta();

        return metaTx;
    }

    private final static class MetaTx {
        private List<TransactionItem> items;
        private ExpResult.Meta meta;

        ExpResult.Meta getMeta() {
            return meta;
        }
    }

    public static class Factory extends DataSource.Factory<Integer, TransactionItem> {
        private TransactionRepository mRepo;
        private List<MinterAddress> mAddressList;
        private InfoRepository mInfoRepo;

        public Factory(TransactionRepository repo, InfoRepository infoRepo, List<MinterAddress> addresses) {
            mRepo = repo;
            mInfoRepo = infoRepo;
            mAddressList = addresses;
        }

        @Override
        public DataSource<Integer, TransactionItem> create() {
            return new TransactionDataSource(mRepo, mInfoRepo, mAddressList);
        }
    }
}
