package network.minter.bipwallet.settings.views.rows;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.common.CallbackProvider;
import network.minter.bipwallet.internal.common.DeferredCall;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowContract;
import network.minter.bipwallet.internal.views.widgets.BipCircleImageView;
import network.minter.my.models.User;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ChangeAvatarRow implements MultiRowContract.Row<ChangeAvatarRow.ViewHolder> {
    private CallbackProvider<User.Avatar> mAvatar;
    private View.OnClickListener mListener;
    private DeferredCall<ViewHolder> mDefer = DeferredCall.createWithSize(1);

    public ChangeAvatarRow(CallbackProvider<User.Avatar> avatar, View.OnClickListener listener) {
        mAvatar = avatar;
        mListener = listener;
    }

    @Override
    public int getItemView() {
        return R.layout.row_settings_avatar;
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
        if (mAvatar != null && mAvatar.get() != null && mAvatar.get().src != null) {
            viewHolder.avatar.setImageUrl(mAvatar.get().src);
        }

        viewHolder.action.setOnClickListener(mListener);

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

    public void setAvatar(CallbackProvider<User.Avatar> avatar) {
        mAvatar = avatar;

        if(mAvatar != null) {
            mDefer.call(ctx -> ctx.avatar.setImageUrl(mAvatar.get().src));
        }
    }

    public static class ViewHolder extends MultiRowAdapter.RowViewHolder {
        @BindView(R.id.userAvatar) BipCircleImageView avatar;
        @BindView(R.id.action) Button action;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
