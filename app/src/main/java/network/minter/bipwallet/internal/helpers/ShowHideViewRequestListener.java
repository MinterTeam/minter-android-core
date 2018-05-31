package network.minter.bipwallet.internal.helpers;

import android.support.annotation.Nullable;
import android.view.View;

import com.squareup.picasso.Callback;

/**
 * Stars. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public class ShowHideViewRequestListener<R> implements Callback {
    private final View mView;
    private Callback mPicassoCallback;

    public ShowHideViewRequestListener(final View view) {
        mView = view;
        if (mView != null) {
            mView.setVisibility(View.VISIBLE);
        }
    }

    public ShowHideViewRequestListener(final View view, Callback callback) {
        this(view);
        mPicassoCallback = callback;
    }

    @Override
    public void onSuccess() {
        hideView();
        if (mPicassoCallback != null)
            mPicassoCallback.onSuccess();
    }

    @Override
    public void onError() {
        hideView();
        if (mPicassoCallback != null) mPicassoCallback.onError();
    }

    private void hideView() {
        if (mView != null) mView.setVisibility(View.GONE);
    }
}
