package network.minter.bipwallet.advanced.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;

import java.security.SecureRandom;

import javax.inject.Inject;

import network.minter.bipwallet.advanced.AdvancedModeModule;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.advanced.repo.SecretLocalRepository;
import network.minter.mintercore.bip39.MnemonicResult;
import network.minter.mintercore.bip39.NativeBip39;
import network.minter.my.models.User;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class AdvancedGeneratePresenter extends MvpBasePresenter<AdvancedModeModule.GenerateView> {

    @Inject Context context;
    @Inject SecretLocalRepository repo;
    @Inject AuthSession session;

    private SecureRandom mRandom = new SecureRandom();
    private MnemonicResult mMnemonicResult;
    
    @Inject
    public AdvancedGeneratePresenter() {
        
    }

    @Override
    public void attachView(AdvancedModeModule.GenerateView view) {
        super.attachView(view);

        getViewState().setEnableLaunch(false);

        mMnemonicResult = NativeBip39.encodeBytes(mRandom.generateSeed(16));

        getViewState().setMnemonic(mMnemonicResult.getMnemonic());

        getViewState().setEnableCopy(true);
        getViewState().setOnCopy(v->{
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("", mMnemonicResult.getMnemonic());
            clipboard.setPrimaryClip(data);
            Toast.makeText(v.getContext(), "Copied", Toast.LENGTH_LONG).show();
        });

        getViewState().setOnSwitchedConfirm((buttonView, isChecked) -> getViewState().setEnableLaunch(isChecked));

        getViewState().setOnLaunch(v->{
            getViewState().showProgress();

            repo.add(mMnemonicResult);
            session.login(
                    AuthSession.AUTH_TOKEN_ADVANCED,
                    new User(AuthSession.AUTH_TOKEN_ADVANCED),
                    AuthSession.AuthType.Advanced
            );

            getViewState().startHome();
        });
    }
}
