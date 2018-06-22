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

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.views.widgets.BipCircleImageView;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.mintercore.crypto.MinterAddress;

import static network.minter.bipwallet.tx.adapters.TransactionItem.HEADER;
import static network.minter.bipwallet.tx.adapters.TransactionItem.TX;


/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TransactionListAdapter extends PagedListAdapter<TransactionItem, RecyclerView.ViewHolder> {
    private List<MinterAddress> mMyAddresses = new ArrayList<>();


    private final static DiffUtil.ItemCallback<TransactionItem> sDiffCallback = new DiffUtil.ItemCallback<TransactionItem>() {
        @Override
        public boolean areItemsTheSame(TransactionItem oldItem, TransactionItem newItem) {
            return oldItem.isSameOf(newItem);
        }

        @Override
        public boolean areContentsTheSame(TransactionItem oldItem, TransactionItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    private LayoutInflater mInflater;
    private int mExpandedPosition = -1;
    private OnExplorerOpenClickListener mOnExplorerOpenClickListener;
    private MutableLiveData<TransactionDataSource.LoadState> mLoadState;

    protected TransactionListAdapter(@NonNull AsyncDifferConfig<TransactionItem> config) {
        super(config);
    }

    public TransactionListAdapter(List<MinterAddress> addresses) {
        super(sDiffCallback);
        mMyAddresses = addresses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == TX) {
            View view = mInflater.inflate(R.layout.item_list_transaction_expandable, parent, false);
            return new TxViewHolder(view);
        } else if (viewType == HEADER) {
            View view = mInflater.inflate(R.layout.item_list_transaction_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == R.layout.item_list_transaction_progress) {
            View view = mInflater.inflate(R.layout.item_list_transaction_progress, parent, false);
            return new ProgressViewHolder(view);
        }

        throw new IllegalStateException(String.format("Invalid viewType %d", viewType));
    }

    @Override
    public int getItemViewType(int position) {
        if (hasProgressRow() && position == getItemCount() - 1) {
            return R.layout.item_list_transaction_progress;
        }

        return getItem(position).getViewType();
    }

    public void setOnExplorerOpenClickListener(OnExplorerOpenClickListener listener) {
        mOnExplorerOpenClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderItem item = ((HeaderItem) getItem(position));
            ((HeaderViewHolder) holder).header.setText(item.getHeader());
        } else if (holder instanceof TxViewHolder) {
            final TxItem txItem = ((TxItem) getItem(position));
            final HistoryTransaction item = txItem.getTx();
            final TxViewHolder h = ((TxViewHolder) holder);
            ((TxViewHolder) holder).avatar.setImageUrl(txItem.getAvatar());

            final String am;

            if (!item.isIncoming(mMyAddresses)) {
                if (txItem.getUsername() != null) {
                    h.title.setText(String.format("@%s", txItem.getUsername()));
                } else {
                    h.title.setText(item.data.to.toShortString());
                }

                am = String.format("- %s", item.data.amount.toPlainString());
                h.amount.setText(am);
                h.amount.setTextColor(Wallet.app().res().getColor(R.color.textColorPrimary));
            } else {
                if (txItem.getUsername() != null) {
                    h.title.setText(txItem.getUsername());
                } else {
                    h.title.setText(item.data.to.toShortString());
                }
                am = String.format("+ %s", item.data.amount.toPlainString());
                h.amount.setText(am);
                h.amount.setTextColor(Wallet.app().res().getColor(R.color.textColorGreen));
            }

            final DateTime dt = new DateTime(item.timestamp);
            h.fromValue.setText(item.data.from.toString());
            h.toValue.setText(item.data.to.toString());
            h.dateValue.setText(dt.toString(DateTimeFormat.forPattern("EEEE, dd MMMM")));
            h.subamount.setText(item.data.coin.toUpperCase());
            h.timeValue.setText(dt.toString(DateTimeFormat.forPattern("HH:mm:ssZ")));
            h.coinValue.setText(item.data.coin.toUpperCase());
            h.amountValue.setText(am);

            h.action.setOnClickListener(v -> {
                if (mOnExplorerOpenClickListener != null) {
                    mOnExplorerOpenClickListener.onClick(v, item);
                }
            });

            final boolean isExpanded = position == mExpandedPosition;
            h.detailsLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            h.detailsLayout.setActivated(isExpanded);
            h.itemView.setOnClickListener(v -> {
                int prevExp = mExpandedPosition;
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(((ViewGroup) h.itemView), new AutoTransition());
                notifyItemChanged(holder.getAdapterPosition());
                notifyItemChanged(prevExp);
            });
        }
    }

    public void setLoadState(MutableLiveData<TransactionDataSource.LoadState> loadState) {
        mLoadState = loadState;
    }

    private boolean hasProgressRow() {
        return mLoadState != null && mLoadState.getValue() != TransactionDataSource.LoadState.Loaded;
    }

    public interface OnExplorerOpenClickListener {
        void onClick(View view, HistoryTransaction tx);
    }

    public static final class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            header = ((TextView) ((ViewGroup) itemView).getChildAt(0));
        }
    }

    public static final class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static final class TxViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_avatar) BipCircleImageView avatar;
        @BindView(R.id.item_title) TextView title;
        @BindView(R.id.item_amount) TextView amount;
        @BindView(R.id.item_subamount) TextView subamount;
        @BindView(R.id.detail_from_value) TextView fromValue;
        @BindView(R.id.detail_to_value) TextView toValue;
        @BindView(R.id.detail_date_value) TextView dateValue;
        @BindView(R.id.detail_time_value) TextView timeValue;
        @BindView(R.id.detail_coin_value) TextView coinValue;
        @BindView(R.id.detail_amount_value) TextView amountValue;
        @BindView(R.id.action) Button action;
        @BindView(R.id.layout_details) ConstraintLayout detailsLayout;

        public TxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
