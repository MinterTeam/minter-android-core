package network.minter.bipwallet.internal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.views.widgets.BipCircleImageView;

import static network.minter.bipwallet.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class WalletTxStartDialog extends WalletDialog {

    @BindView(R.id.dialog_amount) TextView amount;
    @BindView(R.id.recipient_avatar) BipCircleImageView avatar;
    @BindView(R.id.tx_recipient_name) TextView recipientName;
    @BindView(R.id.action_confirm) Button actionConfirm;
    @BindView(R.id.action_decline) Button actionDecline;

    private Builder mBuilder;

    public WalletTxStartDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_tx_send_start_dialog);
        ButterKnife.bind(this);
        title.setText(mBuilder.mTitle);
        amount.setText(mBuilder.mAmount.toPlainString());
        if (mBuilder.mAvatarUrl != null) {
            avatar.setImageUrl(mBuilder.mAvatarUrl);
        }

        recipientName.setText(mBuilder.mRecipientName);
        actionConfirm.setText(mBuilder.getPositiveTitle());
        actionConfirm.setOnClickListener(v -> {
            if (mBuilder.hasPositiveListener()) {
                mBuilder.getPositiveListener().onClick(WalletTxStartDialog.this, BUTTON_POSITIVE);
            }
        });

        actionDecline.setText(mBuilder.getNegativeTitle());
        actionDecline.setOnClickListener(v -> {
            if (mBuilder.hasNegativeListener()) {
                mBuilder.getNegativeListener().onClick(WalletTxStartDialog.this, BUTTON_NEGATIVE);
            }
        });
    }

    public static final class Builder extends WalletDialogBuilder<WalletTxStartDialog, WalletTxStartDialog.Builder> {
        private BigDecimal mAmount;
        private String mAvatarUrl;
        private String mRecipientName;

        public Builder(Context context, CharSequence title) {
            super(context, title);
        }


        public Builder setAmount(String decimalString) {
            return setAmount(new BigDecimal(decimalString).setScale(18, BigDecimal.ROUND_UNNECESSARY));
        }

        public Builder setAmount(BigDecimal amount) {
            mAmount = amount;
            return this;
        }

        public Builder setAvatarUrl(String avatarUrl) {
            mAvatarUrl = avatarUrl;
            return this;
        }

        public Builder setRecipientName(String recipientName) {
            mRecipientName = recipientName;
            return this;
        }

        public WalletTxStartDialog.Builder setNegativeAction(CharSequence title, Dialog.OnClickListener listener) {
            return super.setAction(BUTTON_NEGATIVE, title, listener);
        }

        public WalletTxStartDialog.Builder setPositiveAction(CharSequence title, OnClickListener listener) {
            return super.setAction(BUTTON_POSITIVE, title, listener);
        }


        public WalletTxStartDialog create() {
            checkNotNull(mRecipientName, "Recipient name required");
            checkNotNull(mAmount, "Amount required");
            return new WalletTxStartDialog(mContext, this);
        }
    }


}
