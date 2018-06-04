package network.minter.bipwallet.internal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import network.minter.bipwallet.R;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class WalletDialog extends Dialog {

    @BindView(R.id.title) TextView title;

    protected WalletDialog(@NonNull Context context) {
        super(context, R.style.Wallet_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);

            getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public interface WithPositiveAction<T> {
        T setPositiveAction(CharSequence title, Dialog.OnClickListener listener);
    }
}
