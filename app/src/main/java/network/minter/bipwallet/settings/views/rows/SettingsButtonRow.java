package network.minter.bipwallet.settings.views.rows;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.common.DeferredCall;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowContract;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SettingsButtonRow implements MultiRowContract.Row<SettingsButtonRow.ViewHolder> {

    private CharSequence mKey;
    private String mValue;
    private String mDefValue;
    private OnClickListener mListener;
    private boolean mInactive = false;
    private DeferredCall<ViewHolder> mDefer = DeferredCall.createWithSize(1);

    public SettingsButtonRow(CharSequence key, String value, String defValue, OnClickListener listener) {
        this(key, value, listener);
        mDefValue = defValue;
    }

    public SettingsButtonRow(CharSequence key, String value, OnClickListener listener) {
        mKey = key;
        mValue = value;
        mListener = listener;
    }

    public interface OnClickListener {
        void onClick(View view, View sharedView, String value);
    }

    public SettingsButtonRow setInactive(boolean inactive) {
        mInactive = inactive;
        return this;
    }

    public SettingsButtonRow setValue(String value, OnClickListener listener) {
        mValue = value;
        mListener = listener;
        mDefer.call(this::fill);

        return this;
    }

    public SettingsButtonRow setValue(String value) {
        mValue = value;
        mDefer.call(this::fill);
        return this;
    }

    @Override
    public int getItemView() {
        return R.layout.item_list_settings;
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
        fill(viewHolder);
        mDefer.attach(viewHolder);
    }

    @Override
    public void onUnbindViewHolder(@NonNull ViewHolder viewHolder) {
        mDefer.detach();
    }

    @NonNull
    @Override
    public Class<ViewHolder> getViewHolderClass() {
        return ViewHolder.class;
    }

    private void fill(ViewHolder vh) {
        if (mKey != null && mKey.length() > 0) {
            ViewCompat.setTransitionName(vh.key, "settings_field");
        }

        vh.key.setText(mKey);
        vh.value.setText(mValue);

        if (mDefValue == null) {
            if (mInactive) {
                vh.value.setTextColor(Wallet.app().res().getColor(R.color.textColorGrey));
            } else {
                vh.value.setTextColor(Wallet.app().res().getColor(R.color.textColorPrimary));
            }
        } else {
            if (mValue == null || mValue.isEmpty()) {
                vh.value.setTextColor(Wallet.app().res().getColor(R.color.textColorGrey));
                vh.value.setText(mDefValue);
            } else {
                vh.value.setTextColor(Wallet.app().res().getColor(R.color.textColorPrimary));
                vh.value.setText(mValue);
            }
        }

        vh.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(vh.itemView, vh.key, mValue);
            }
        });
    }

    public static class ViewHolder extends MultiRowAdapter.RowViewHolder {
        @BindView(R.id.item_key) TextView key;
        @BindView(R.id.item_value) TextView value;
        @BindView(R.id.item_icon) ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
