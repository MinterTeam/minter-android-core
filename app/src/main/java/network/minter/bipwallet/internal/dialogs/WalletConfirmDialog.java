package network.minter.bipwallet.internal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class WalletConfirmDialog extends WalletDialog {

    private final Builder mBuilder;
    @BindView(R.id.dialog_text) TextView text;
    @BindView(R.id.action_confirm) Button actionConfirm;
    @BindView(R.id.action_decline) Button actionDecline;

    public WalletConfirmDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_confirm_dialog);
        ButterKnife.bind(this);
        title.setText(mBuilder.mTitle);
        text.setText(mBuilder.mText);
        actionConfirm.setText(mBuilder.getPositiveTitle());
        actionConfirm.setOnClickListener(v -> {
            if (mBuilder.hasPositiveListener()) {
                mBuilder.getPositiveListener().onClick(WalletConfirmDialog.this, BUTTON_POSITIVE);
            } else {
                dismiss();
            }
        });

        actionDecline.setText(mBuilder.getNegativeTitle());
        actionDecline.setOnClickListener(v -> {
            if (mBuilder.hasNegativeListener()) {
                mBuilder.getNegativeListener().onClick(WalletConfirmDialog.this, BUTTON_NEGATIVE);
            } else {
                dismiss();
            }
        });
    }

    public static final class Builder extends WalletDialogBuilder<WalletConfirmDialog, WalletConfirmDialog.Builder> {
        private CharSequence mText;

        public Builder(Context context, CharSequence title) {
            super(context, title);
        }

        @Override
        public WalletConfirmDialog create() {
            return new WalletConfirmDialog(mContext, this);
        }

        public Builder setText(CharSequence text) {
            mText = text;
            return this;
        }

        public Builder setPositiveAction(CharSequence title) {
            return setPositiveAction(title, null);
        }

        public Builder setNegativeAction(CharSequence title) {
            return setNegativeAction(title, null);
        }

        public Builder setNegativeAction(CharSequence title, Dialog.OnClickListener listener) {
            return super.setAction(BUTTON_NEGATIVE, title, listener);
        }

        public Builder setPositiveAction(CharSequence title, OnClickListener listener) {
            return super.setAction(BUTTON_POSITIVE, title, listener);
        }
    }


}
