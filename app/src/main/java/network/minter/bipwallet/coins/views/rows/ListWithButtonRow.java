package network.minter.bipwallet.coins.views.rows;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.views.list.NonScrollableLinearLayoutManager;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowContract;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ListWithButtonRow implements MultiRowContract.Row<ListWithButtonRow.ViewHolder> {
    private Builder mBuilder;

    private ListWithButtonRow(Builder builder) {
        mBuilder = builder;
    }

    @Override
    public int getItemView() {
        return R.layout.row_list_with_button;
    }

    @Override
    public int getRowPosition() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        if (mBuilder.mActionTitle != null) {
            viewHolder.action.setText(mBuilder.mActionTitle);
            viewHolder.action.setOnClickListener(mBuilder.mActionListener);
        }

        viewHolder.title.setText(mBuilder.mTitle);

        if (mBuilder.mAdapter == null) {
            viewHolder.action.setVisibility(View.GONE);
            viewHolder.emptyTitle.setVisibility(View.VISIBLE);
            if (mBuilder.mEmptyListTitle != null) {
                viewHolder.emptyTitle.setText(mBuilder.mEmptyListTitle);
            } else {
                viewHolder.emptyTitle.setText("No elements was found");
            }
        } else {
            viewHolder.action.setVisibility(View.VISIBLE);
            viewHolder.emptyTitle.setVisibility(View.GONE);
            viewHolder.list.setLayoutManager(new NonScrollableLinearLayoutManager(viewHolder.itemView.getContext()));
            viewHolder.list.setAdapter(mBuilder.mAdapter);
        }
    }

    @Override
    public void onUnbindViewHolder(@NonNull ViewHolder viewHolder) {

    }

    @NonNull
    @Override
    public Class<ViewHolder> getViewHolderClass() {
        return ViewHolder.class;
    }

    public static class ViewHolder extends MultiRowAdapter.RowViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.action) Button action;
        @BindView(R.id.list) RecyclerView list;
        @BindView(R.id.emptyTitle) TextView emptyTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static final class Builder {
        private RecyclerView.Adapter<?> mAdapter;
        private CharSequence mTitle;
        private CharSequence mActionTitle;
        private View.OnClickListener mActionListener;
        private CharSequence mEmptyListTitle;

        public Builder(CharSequence title) {
            mTitle = title;
        }

        public Builder setEmptyTitle(CharSequence title) {
            mEmptyListTitle = title;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setAdapter(RecyclerView.Adapter<?> adapter) {
            mAdapter = adapter;
            return this;
        }

        public Builder setAction(CharSequence name, View.OnClickListener listener) {
            mActionTitle = name;
            mActionListener = listener;
            return this;
        }

        public ListWithButtonRow build() {
            return new ListWithButtonRow(this);
        }
    }
}
