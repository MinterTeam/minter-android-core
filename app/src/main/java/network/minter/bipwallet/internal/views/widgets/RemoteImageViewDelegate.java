package network.minter.bipwallet.internal.views.widgets;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import java.lang.ref.WeakReference;

import network.minter.bipwallet.internal.common.annotations.Dp;
import timber.log.Timber;

import static network.minter.bipwallet.internal.Wallet.app;

/**
 * Wallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
final class RemoteImageViewDelegate implements RemoteImageView {
    private final WeakReference<ImageView> mImage;

    RemoteImageViewDelegate(ImageView imageView) {
        mImage = new WeakReference<>(imageView);
    }

    @Override
    public void setImageUrl(String url, @Dp float size) {
        setImageUrl(Uri.parse(url), size);
    }

    public void setImageUrl(Uri uri, @DimenRes int resId) {
        setImageUrl(uri, getContext().getResources().getDimension(resId));
    }

    public void setImageUrl(String url, @DimenRes int resId) {
        setImageUrl(Uri.parse(url), resId);
    }

    public void setImageUrl(Uri uri, @Dp float size) {
        app().image()
                .loadResize(uri, size)
                .into(mImage.get());
    }

    @Override
    public void setImageUrl(Uri uri) {
        app().image()
                .loadFit(uri)
                .into(mImage.get());
    }

    @Override
    public void setImageUrl(String url) {
        app().image()
                .loadFit(url)
                .into(mImage.get(), new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Timber.w("Unable to load image %s", url);
                    }
                });
    }

    @Override
    public void setImageUrl(RemoteImageContainer imageUrlContainer) {
        setImageUrl(imageUrlContainer.getImageUrl());
    }

    @Override
    public void setImageUrl(RemoteImageContainer imageUrlContainer, float size) {
        setImageUrl(imageUrlContainer.getImageUrl(), size);
    }

    @Override
    public void setImageUrl(RemoteImageContainer imageUrlContainer, int resId) {
        setImageUrl(imageUrlContainer.getImageUrl(), resId);
    }

    private Context getContext() {
        return mImage.get().getContext();
    }

}
