package network.minter.bipwallet.advanced.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.AdvancedModeModule;
import network.minter.bipwallet.advanced.views.AdvancedGeneratePresenter;
import network.minter.bipwallet.home.ui.HomeActivity;
import network.minter.bipwallet.internal.BaseMvpInjectActivity;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AdvancedGenerateActivity extends BaseMvpInjectActivity implements AdvancedModeModule.GenerateView {
    @Inject Provider<AdvancedGeneratePresenter> presenterProvider;
    @InjectPresenter AdvancedGeneratePresenter presenter;

    @BindView(R.id.actionCopy) View actionCopy;
    @BindView(R.id.saveSwitch) Switch saveSwitch;
    @BindView(R.id.actionLaunch) Button actionLaunch;
    @BindView(R.id.mnemonic) TextView mnemonicPhrase;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void setOnCopy(View.OnClickListener listener) {
        actionCopy.setOnClickListener(listener);
    }

    @Override
    public void setOnSwitchedConfirm(Switch.OnCheckedChangeListener listener) {
        saveSwitch.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setOnLaunch(View.OnClickListener listener) {
        actionLaunch.setOnClickListener(listener);
    }

    @Override
    public void setMnemonic(CharSequence phrase) {
        mnemonicPhrase.setText(phrase);
    }

    @Override
    public void setEnableLaunch(boolean enable) {
        actionLaunch.setEnabled(enable);
    }

    @Override
    public void setEnableCopy(boolean enable) {
        actionCopy.setEnabled(enable);
    }

    @Override
    public void startHome() {
        startActivityClearTop(this, HomeActivity.class);
        finish();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @ProvidePresenter
    AdvancedGeneratePresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_generate);
        ButterKnife.bind(this);

        setupToolbar(toolbar);
    }
}
