package network.minter.bipwallet.addresses.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import org.parceler.Parcels;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.addresses.AddressManageModule;
import network.minter.bipwallet.addresses.views.AddressItemPresenter;
import network.minter.bipwallet.internal.BaseMvpInjectActivity;
import network.minter.bipwallet.internal.dialogs.WalletConfirmDialog;
import network.minter.bipwallet.internal.dialogs.WalletProgressDialog;
import network.minter.bipwallet.internal.system.ActivityBuilder;
import network.minter.my.models.AddressData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressItemActivity extends BaseMvpInjectActivity implements AddressManageModule.AddressItemView {
    public final static String EXTRA_ADDRESS_DATA = "EXTRA_ADDRESS_DATA";

    @Inject Provider<AddressItemPresenter> presenterProvider;
    @InjectPresenter AddressItemPresenter presenter;

    @BindView(R.id.address) TextView address;
    @BindView(R.id.secured_value) TextView securedBy;
    @BindView(R.id.action_remove) Button actionDelete;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private WalletProgressDialog mProgress;

    @Override
    public void setAddress(String addressName) {
        address.setText(addressName);
    }

    @Override
    public void setSecuredBy(String securedByVal) {
        securedBy.setText(securedByVal);
    }

    @Override
    public void setOnClickDelete(View.OnClickListener listener) {
        actionDelete.setOnClickListener(listener);
    }

    @Override
    public void startRemoveDialog(CharSequence attention, CharSequence description, String yes, String no, Dialog.OnClickListener onYesListener) {
        new WalletConfirmDialog.Builder(this, attention)
                .setText(description)
                .setPositiveAction(yes, onYesListener)
                .setNegativeAction(no)
                .create()
                .show();
    }

    @Override
    public void finishWithResult(int resultCode) {
        setResult(resultCode);
        finish();
    }

    @Override
    public void showProgress(CharSequence text) {
        if (mProgress == null) {
            mProgress = new WalletProgressDialog.Builder(this, "Please, wait")
                    .setText(text)
                    .create();
        }

        if (!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

    @ProvidePresenter
    AddressItemPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_item);
        ButterKnife.bind(this);
        setupToolbar(toolbar);

        presenter.handleExtras(getIntent());
    }

    public static final class Builder extends ActivityBuilder {
        private final AddressData mData;

        public Builder(@NonNull Activity from, AddressData data) {
            super(from);
            mData = data;
        }

        public Builder(@NonNull Fragment from, AddressData data) {
            super(from);
            mData = data;
        }

        public Builder(@NonNull Service from, AddressData data) {
            super(from);
            mData = data;
        }

        @Override
        protected void onBeforeStart(Intent intent) {
            super.onBeforeStart(intent);
            intent.putExtra(EXTRA_ADDRESS_DATA, Parcels.wrap(mData));
        }

        @Override
        protected Class<?> getActivityClass() {
            return AddressItemActivity.class;
        }
    }
}