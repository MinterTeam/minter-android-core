package network.minter.bipwallet.coins.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.repo.SecretLocalRepository;
import network.minter.bipwallet.coins.CoinsTabModule;
import network.minter.bipwallet.coins.views.rows.ListWithButtonRow;
import network.minter.bipwallet.internal.ReactiveAdapter;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.internal.views.list.SimpleRecyclerAdapter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.internal.views.widgets.BipCircleImageView;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class CoinsTabPresenter extends MvpBasePresenter<CoinsTabModule.CoinsTabView> {

    @Inject TransactionRepository repo;
    @Inject SecretLocalRepository secretRepo;
    private MultiRowAdapter mAdapter;
    private SimpleRecyclerAdapter<HistoryTransaction, ItemViewHolder> mTransactionsAdapter;
//    private SimpleRecyclerAdapter<HistoryTransaction, ItemViewHolder> mTransactionsAdapter;

    private boolean isMyAddress(MinterAddress address) {
        for(MinterAddress add: secretRepo.getAddresses()) {
            if(add.equals(address)) {
                return true;
            }
        }

        return false;
    }

    @Inject
    public CoinsTabPresenter() {
        mAdapter = new MultiRowAdapter();
        mTransactionsAdapter = new SimpleRecyclerAdapter.Builder<HistoryTransaction, ItemViewHolder>()
                .setCreator(R.layout.item_list_with_image, ItemViewHolder.class)
                .setBinder((itemViewHolder, item, position) -> {
                    if(isMyAddress(item.data.to)) {
                        itemViewHolder.amount.setText("+ " + String.valueOf(item.data.amount));
                        itemViewHolder.amount.setTextColor(Wallet.app().res().getColor(R.color.textColorGreen));
                    } else {
                        itemViewHolder.amount.setText("- "+String.valueOf(item.data.amount * -1.0));
                        itemViewHolder.amount.setTextColor(Wallet.app().res().getColor(R.color.textColorPrimary));
                    }

                    itemViewHolder.title.setText(item.data.from.toString());
                    itemViewHolder.avatar.setImageResource(R.drawable.img_avatar_default);
                }).build();
    }

    @Override
    public void attachView(CoinsTabModule.CoinsTabView view) {
        super.attachView(view);

        ReactiveAdapter.rxCall(repo.getTransactions(secretRepo.getAddresses()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(getErrorResolver())
                .subscribe(res -> {
                    mTransactionsAdapter.setItems(Stream.of(res.result).limit(5).toList());
                    mTransactionsAdapter.notifyDataSetChanged();
                }, Wallet.Rx.errorHandler(getViewState()));

        getViewState().setOnAvatarClick(v->{

        });

        getViewState().setAdapter(mAdapter);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mAdapter.addRow(
                new ListWithButtonRow.Builder("Latest transactions")
                        .setAction("All transactions", null)
                        .setAdapter(mTransactionsAdapter)
                        .setEmptyTitle("No transactions found")
                        .build()
        );
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
