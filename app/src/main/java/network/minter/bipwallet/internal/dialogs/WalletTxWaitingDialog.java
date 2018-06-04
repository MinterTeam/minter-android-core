package network.minter.bipwallet.internal.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.helpers.Plurals;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class WalletTxWaitingDialog extends WalletDialog {

    private final Builder mBuilder;
    @BindView(R.id.tx_countdown) TextView countdown;
    @BindView(R.id.tx_fee) TextView fee;
    @BindView(R.id.action_express) Button actionExpress;
    private long mTicks = 0;
    private Disposable mCountdownDisposable;

    public WalletTxWaitingDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_tx_send_wait_dialog);
        ButterKnife.bind(this);
        title.setText(mBuilder.mTitle);


        actionExpress.setOnClickListener(v -> {
            if (mBuilder.hasPositiveListener()) {
                mBuilder.getPositiveListener().onClick(WalletTxWaitingDialog.this, BUTTON_POSITIVE);
            }
        });

        final Builder b = mBuilder;
        mCountdownDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    mTicks++;
                    countdown.setText(Plurals.countdown(b.mCountdownSeconds - mTicks));
                    if (mTicks >= b.mCountdownSeconds) {
                        mTicks = 0;
                        mCountdownDisposable.dispose();
                    }
                });
    }

    public static class Builder extends WalletDialogBuilder<WalletTxWaitingDialog, WalletTxWaitingDialog.Builder> {
        private long mCountdownSeconds = 60L;
        private CharSequence mFee; //todo ?

        public Builder(Context context, CharSequence title) {
            super(context, title);
        }

        public Builder setPositiveAction(CharSequence title, OnClickListener listener) {
            return super.setAction(BUTTON_POSITIVE, title, listener);
        }

        public Builder setCountdownSeconds(long seconds) {
            mCountdownSeconds = seconds;
            return this;
        }

        public WalletTxWaitingDialog create() {
            return new WalletTxWaitingDialog(mContext, this);
        }
    }


}
