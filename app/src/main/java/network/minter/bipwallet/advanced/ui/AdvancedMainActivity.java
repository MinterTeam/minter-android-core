package network.minter.bipwallet.advanced.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.AdvancedModeModule;
import network.minter.bipwallet.advanced.views.AdvancedMainPresenter;
import network.minter.bipwallet.internal.BaseMvpInjectActivity;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AdvancedMainActivity extends BaseMvpInjectActivity implements AdvancedModeModule.MainView {

    @BindView(R.id.actionGenerate) Button actionGenerate;
    @BindView(R.id.actionActivate) Button actionActivate;
    @BindView(R.id.seedInput) TextInputEditText seedInput;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject Provider<AdvancedMainPresenter> presenterProvider;
    @InjectPresenter AdvancedMainPresenter presenter;

    @Override
    public void setOnGenerate(View.OnClickListener listener) {
        actionGenerate.setOnClickListener(listener);
    }

    @Override
    public void setMnemonicTextChangedListener(TextWatcher textWatcher) {
        seedInput.addTextChangedListener(textWatcher);
    }

    @Override
    public void setOnActivateMnemonic(View.OnClickListener listener) {
        actionActivate.setOnClickListener(listener);
    }

    @Override
    public void startGenerate() {
        startActivity(new Intent(this, AdvancedGenerateActivity.class));
    }

    @Override
    public void setError(CharSequence errorMessage) {
        seedInput.setError(errorMessage);
    }

    @ProvidePresenter
    AdvancedMainPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_main);
        ButterKnife.bind(this);
        presenter.handleExtras(getIntent());
        setupToolbar(toolbar);
    }
}
