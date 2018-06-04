package network.minter.bipwallet.internal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class WalletDialogBuilder<D extends WalletDialog, B extends WalletDialogBuilder> {
    protected final Context mContext;
    protected final CharSequence mTitle;

    protected CharSequence mPositiveTitle, mNegativeTitle, mNeutralTitle;
    protected Dialog.OnClickListener mPositiveListener, mNegativeListener, mNeutralListener;

    public WalletDialogBuilder(Context context, CharSequence title) {
        mContext = context;
        mTitle = title;
    }

    public abstract D create();

    protected CharSequence getPositiveTitle() {
        return mPositiveTitle;
    }

    protected CharSequence getNegativeTitle() {
        return mNegativeTitle;
    }

    protected CharSequence getNeutralTitle() {
        return mNeutralTitle;
    }

    protected Dialog.OnClickListener getPositiveListener() {
        return mPositiveListener;
    }

    protected boolean hasPositiveListener() {
        return mPositiveListener != null;
    }

    protected boolean hasNegativeListener() {
        return mNegativeListener != null;
    }

    protected boolean hasNeutralListener() {
        return mNeutralListener != null;
    }

    protected Dialog.OnClickListener getNegativeListener() {
        return mNegativeListener;
    }

    protected Dialog.OnClickListener getNeutralListener() {
        return mNeutralListener;
    }

    protected B setAction(int whichBtn, CharSequence title, Dialog.OnClickListener listener) {
        switch (whichBtn) {
            case DialogInterface.BUTTON_POSITIVE:
                mPositiveTitle = title;
                mPositiveListener = listener;
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mNegativeTitle = title;
                mNegativeListener = listener;
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mNeutralTitle = title;
                mNeutralListener = listener;
                break;
        }

        return (B) this;
    }

    protected CharSequence getActionTitle(int whichBtn) {
        switch (whichBtn) {
            case DialogInterface.BUTTON_POSITIVE:
                return mPositiveTitle;
            case DialogInterface.BUTTON_NEGATIVE:
                return mNegativeTitle;
            case DialogInterface.BUTTON_NEUTRAL:
                return mNeutralTitle;
        }

        return null;
    }

    protected Dialog.OnClickListener getActionListener(int whichBtn) {
        switch (whichBtn) {
            case DialogInterface.BUTTON_POSITIVE:
                return mPositiveListener;
            case DialogInterface.BUTTON_NEGATIVE:
                return mNegativeListener;
            case DialogInterface.BUTTON_NEUTRAL:
                return mNeutralListener;
        }

        return null;
    }

}
