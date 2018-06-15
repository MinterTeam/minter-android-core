
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

package network.minter.bipwallet.coins.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.repo.SecretStorage;
import network.minter.bipwallet.coins.CoinsTabModule;
import network.minter.bipwallet.coins.repos.ExplorerBalanceFetcher;
import network.minter.bipwallet.coins.views.rows.ListWithButtonRow;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.internal.views.list.SimpleRecyclerAdapter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.internal.views.widgets.BipCircleImageView;
import network.minter.bipwallet.tx.adapters.TransactionDataSource;
import network.minter.blockchainapi.repo.AccountRepository;
import network.minter.explorerapi.models.AddressData;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.explorerapi.repo.AddressRepository;
import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.my.repo.InfoRepository;
import timber.log.Timber;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;
import static network.minter.bipwallet.internal.helpers.Plurals.bips;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class CoinsTabPresenter extends MvpBasePresenter<CoinsTabModule.CoinsTabView> {

    @Inject AuthSession session;
    @Inject TransactionRepository repo;
    @Inject SecretStorage secretRepo;
    @Inject AccountRepository accountRepo;
    @Inject AddressRepository addressRepo;
    @Inject InfoRepository infoRepo;
    private MultiRowAdapter mAdapter;
    private SimpleRecyclerAdapter<HistoryTransaction, ItemViewHolder> mTransactionsAdapter;
    private SimpleRecyclerAdapter<AddressData.CoinBalance, ItemViewHolder> mCoinsAdapter;
    private ListWithButtonRow mTransactionsRow, mCoinsRow;

    @Inject
    public CoinsTabPresenter() {
        mAdapter = new MultiRowAdapter();
        mTransactionsAdapter = new SimpleRecyclerAdapter.Builder<HistoryTransaction, ItemViewHolder>()
                .setCreator(R.layout.item_list_with_image, ItemViewHolder.class)
                .setBinder((itemViewHolder, item, position) -> {
                    final String addrString;
                    if (item.isIncoming()) {
                        addrString = item.data.from.toShortString();
                        itemViewHolder.amount.setText(String.format("+ %s", item.data.amount.toPlainString()));
                        itemViewHolder.amount.setTextColor(Wallet.app().res().getColor(R.color.textColorGreen));
                    } else {
                        addrString = item.data.to.toShortString();
                        itemViewHolder.amount.setText(String.valueOf(item.data.amount.toPlainString().replace("-", "- ")));
                        itemViewHolder.amount.setTextColor(Wallet.app().res().getColor(R.color.textColorPrimary));
                    }

                    if (item.username != null) {
                        itemViewHolder.title.setText(String.format("@%s", item.username));
                    } else {
                        itemViewHolder.title.setText(addrString);
                    }

                    itemViewHolder.avatar.setImageUrl(item.getAvatar());
                    itemViewHolder.subname.setText(item.data.coin.toUpperCase());
                }).build();

        mCoinsAdapter = new SimpleRecyclerAdapter.Builder<AddressData.CoinBalance, ItemViewHolder>()
                .setCreator(R.layout.item_list_with_image, ItemViewHolder.class)
                .setBinder((itemViewHolder, item, position) -> {
                    itemViewHolder.title.setText(item.coin.toUpperCase());
                    itemViewHolder.amount.setText(item.getBalance().toPlainString());
                    itemViewHolder.avatar.setImageUrl(session.getUser().getData().getAvatar().getUrl());
                    itemViewHolder.subname.setVisibility(View.GONE);
                }).build();
    }

    @Override
    public void attachView(CoinsTabModule.CoinsTabView view) {
        super.attachView(view);


        safeSubscribeIoToUi(rxCall(repo.getTransactions(secretRepo.getAddresses())))
                .switchMap(items -> TransactionDataSource.mapAddressesInfo(infoRepo, items))
                .retryWhen(getErrorResolver())
                .subscribe(res -> {
                    int cnt = mTransactionsAdapter.getItemCount();

                    mTransactionsAdapter.setItems(Stream.of(res.result).limit(5).toList());
                    if (cnt == 0) {
                        mTransactionsAdapter.notifyItemRangeInserted(0, mTransactionsAdapter.getItemCount());
                    } else {
                        mTransactionsAdapter.notifyItemRangeChanged(0, mTransactionsAdapter.getItemCount());
                    }

                    cnt = mTransactionsAdapter.getItemCount();
                    if (cnt == 0) {
                        mTransactionsRow.setStatus(ListWithButtonRow.Status.Empty);
                    } else {
                        mTransactionsRow.setStatus(ListWithButtonRow.Status.Normal);
                    }
                }, t -> mTransactionsRow.setError(t.getMessage()));

        Observable.create(new ExplorerBalanceFetcher(addressRepo, secretRepo.getAddresses()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {

                    if (res == null || res.coins.isEmpty()) {
                        mCoinsRow.setStatus(ListWithButtonRow.Status.Empty);
                        return;
                    }

                    int cnt = mCoinsAdapter.getItemCount();

                    mCoinsAdapter.setItems(res.coins.values());
                    if (cnt > 0 && res.coins.size() == 0) {
                        mCoinsAdapter.notifyItemRangeRemoved(0, cnt);
                    } else if (mCoinsAdapter.getItemCount() == 0) {
                        mCoinsAdapter.notifyItemInserted(res.coins.size());
                    } else {
                        mCoinsAdapter.notifyDataSetChanged();
                    }

                    Timber.d("Coins count: %d", res.coins.size());

                    final StringHelper.DecimalFraction num = StringHelper.splitDecimalFractions(res.bipTotal);
                    getViewState().setBalance(num.intPart, num.fractionalPart, bips(num.intPart));

                    mCoinsRow.setStatus(ListWithButtonRow.Status.Normal);

                }, t -> {
                    mCoinsRow.setError(t.getMessage());
                });

        getViewState().setAdapter(mAdapter);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setAvatar(session.getUser().getData().getAvatar().getUrl());
        mTransactionsRow = new ListWithButtonRow.Builder("Latest transactions")
                .setAction("All transactions", this::onClickStartTransactionList)
                .setAdapter(mTransactionsAdapter)
                .setEmptyTitle("You have no transactions")
                .build();

        mCoinsRow = new ListWithButtonRow.Builder("My Coins")
                .setAction("Convert", this::onClickConvertCoins)
                .setAdapter(mCoinsAdapter)
                .setEmptyTitle("You have no one address. Nothing to show.")
                .build();


        mAdapter.addRow(mTransactionsRow);
        mAdapter.addRow(mCoinsRow);
    }

    private void onClickConvertCoins(View view) {

    }

    private void onClickStartTransactionList(View view) {
        getViewState().startTransactionList();
    }

    private boolean isMyAddress(MinterAddress address) {
        for (MinterAddress add : secretRepo.getAddresses()) {
            if (add.equals(address)) {
                return true;
            }
        }

        return false;
    }

    public final static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_avatar) BipCircleImageView avatar;
        @BindView(R.id.item_title) TextView title;
        @BindView(R.id.item_amount) TextView amount;
        @BindView(R.id.item_subamount) TextView subname;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
