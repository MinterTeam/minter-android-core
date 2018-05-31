package network.minter.bipwallet.advanced;

import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.arellomobile.mvp.MvpView;

import dagger.Module;
import network.minter.bipwallet.internal.mvp.ProgressView;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Module
public class AdvancedModeModule {

    public interface MainView extends MvpView {
        void setOnGenerate(View.OnClickListener listener);
        void setMnemonicTextChangedListener(TextWatcher textWatcher);
        void setOnActivateMnemonic(View.OnClickListener listener);
        void startGenerate();
        void setError(CharSequence errorMessage);
    }

    public interface GenerateView extends MvpView, ProgressView {
        void setOnCopy(View.OnClickListener listener);
        void setOnSwitchedConfirm(Switch.OnCheckedChangeListener listener);
        void setOnLaunch(View.OnClickListener listener);
        void setMnemonic(CharSequence phrase);
        void setEnableLaunch(boolean enable);
        void setEnableCopy(boolean enable);
        void startHome();
    }
}
