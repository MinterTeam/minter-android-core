package network.minter.bipwallet.internal.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class WalletProgressDialog extends WalletDialog {
    @BindView(R.id.dialog_text) TextView text;
    private Builder mBuilder;

    protected WalletProgressDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_progress_dialog);
        ButterKnife.bind(this);

        if (mBuilder.mText == null) {
            text.setText(R.string.progress_wait);
        } else {
            text.setText(mBuilder.mText);
        }
    }

    public static final class Builder extends WalletDialogBuilder<WalletProgressDialog, Builder> {
        private CharSequence mText;

        public Builder(Context context, CharSequence title) {
            super(context, title);
        }

        public Builder setText(CharSequence text) {
            mText = text;
            return this;
        }

        public Builder setText(@StringRes int textRes) {
            return setText(mContext.getResources().getString(textRes));
        }

        @Override
        public WalletProgressDialog create() {
            return new WalletProgressDialog(mContext, this);
        }
    }
}
