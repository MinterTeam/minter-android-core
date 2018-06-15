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

package network.minter.bipwallet.tx.views;

import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import network.minter.bipwallet.advanced.repo.SecretStorage;
import network.minter.bipwallet.coins.CoinsTabModule;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.tx.adapters.TransactionDataSource;
import network.minter.bipwallet.tx.adapters.TransactionItem;
import network.minter.bipwallet.tx.adapters.TransactionListAdapter;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.my.repo.InfoRepository;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class TransactionListPresenter extends MvpBasePresenter<CoinsTabModule.TransactionListView> {

    @Inject TransactionRepository transactionRepo;
    @Inject SecretStorage secretRepo;
    @Inject InfoRepository infoRepo;

    private TransactionListAdapter mAdapter;
    private TransactionDataSource.Factory mSourceFactory;
    private Disposable mListDisposable;
    private RxPagedListBuilder<Integer, TransactionItem> listBuilder;
    private int mLastPosition = 0;

    @Inject
    public TransactionListPresenter() {
        mAdapter = new TransactionListAdapter();
    }

    @Override
    public void attachView(CoinsTabModule.TransactionListView view) {
        super.attachView(view);
        getViewState().setAdapter(mAdapter);
        getViewState().setOnRefreshListener(this::onRefresh);
        getViewState().scrollTo(mLastPosition);
    }

    public void onScrolledTo(int position) {
        mLastPosition = position;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mAdapter.setOnExplorerOpenClickListener(this::onExplorerClick);
        mSourceFactory = new TransactionDataSource.Factory(transactionRepo, infoRepo, secretRepo.getAddresses());
        PagedList.Config cfg = new PagedList.Config.Builder()
                .setPageSize(50)
                .setEnablePlaceholders(false)
                .build();

        getViewState().showProgress();
        listBuilder = new RxPagedListBuilder<>(mSourceFactory, cfg);
        refresh();

        unsubscribeOnDestroy(mListDisposable);
    }

    private void onRefresh() {
        getViewState().showRefreshProgress();
        mListDisposable.dispose();
        getViewState().scrollTo(0);
        refresh();
    }

    private void refresh() {
        mListDisposable = listBuilder.buildObservable()
                .subscribe(res -> {
                    getViewState().hideProgress();
                    getViewState().hideRefreshProgress();
                    mAdapter.submitList(res);
                });
    }

    private void onExplorerClick(View view, HistoryTransaction historyTransaction) {
        // @TODO prefix for hash: MinterSDK.PREFIX_TX
        getViewState().startExplorer(historyTransaction.hash.toHexString("Mx"));
    }
}
