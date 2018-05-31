package network.minter.bipwallet.advanced.views;

import android.text.Editable;
import android.text.TextWatcher;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import network.minter.bipwallet.advanced.AdvancedModeModule;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.mintercore.bip39.NativeBip39;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class AdvancedMainPresenter extends MvpBasePresenter<AdvancedModeModule.MainView> {

    private String mPhrase;

    @Inject
    public AdvancedMainPresenter() {

    }

    @Override
    public void attachView(AdvancedModeModule.MainView view) {
        super.attachView(view);

        getViewState().setMnemonicTextChangedListener(onTextChanged());
        getViewState().setOnActivateMnemonic(v -> {
            if(mPhrase == null || mPhrase.isEmpty()) {
                getViewState().setError("Empty phrase");
                return;
            }

            boolean valid = NativeBip39.validateMnemonic(mPhrase, NativeBip39.LANG_DEFAULT);
            if(!valid) {
                getViewState().setError("Phrase is not valid");
                return;
            }



        });

        getViewState().setOnGenerate(v -> getViewState().startGenerate());
    }

    private TextWatcher onTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String res = s.toString();
                if (res.isEmpty()) {
                    getViewState().setError(null);
                }

                mPhrase = res;


            }
        };
    }

}
