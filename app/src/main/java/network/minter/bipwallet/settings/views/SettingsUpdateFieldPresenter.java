package network.minter.bipwallet.settings.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import network.minter.bipwallet.home.HomeScope;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.ui.SettingsUpdateFieldDialog;
import network.minter.my.repo.ProfileRepository;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@HomeScope
@InjectViewState
public class SettingsUpdateFieldPresenter extends MvpBasePresenter<SettingsTabModule.SettingsUpdateFieldView> {

    @Inject ProfileRepository profileRepo;
    private CharSequence mLabel;
    private String mField;
    private String mValue;

    @Inject
    public SettingsUpdateFieldPresenter() {
    }

    @Override
    public void handleExtras(Bundle bundle) {
        super.handleExtras(bundle);

        mLabel = bundle.getCharSequence(SettingsUpdateFieldDialog.ARG_LABEL);
        mField = bundle.getString(SettingsUpdateFieldDialog.ARG_FIELD_NAME);
        mValue = bundle.getString(SettingsUpdateFieldDialog.ARG_VALUE, null);

        getViewState().setLabel(mLabel);
        getViewState().setValue(mValue);

        getViewState().setOnSubmit(this::onSubmit);

        getViewState().setOnTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mValue = s.toString();
            }
        });
    }

    private void onSubmit(View view) {

    }
}
