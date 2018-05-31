package network.minter.bipwallet.auth;

import android.net.Uri;
import android.view.View;

import com.arellomobile.mvp.MvpView;

import java.util.List;
import java.util.Map;

import dagger.Module;
import network.minter.bipwallet.auth.ui.InputGroup;
import network.minter.bipwallet.internal.mvp.ErrorViewWithRetry;
import network.minter.bipwallet.internal.mvp.ProgressView;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Module
public class AuthModule {

    public interface AuthView extends MvpView {
        void setOnCreateWallet(View.OnClickListener listener);
        void setOnAdvancedMode(View.OnClickListener listener);
        void setOnSignin(View.OnClickListener listener);
        void setOnHelp(View.OnClickListener listener);
        void startAdvancedMode();
        void startRegister();
        void startSignIn();
        void startHelp();
    }

    public interface SigninView extends MvpView, ProgressView, ErrorViewWithRetry {
        void setOnTextChangedListener(InputGroup.OnTextChangedListener listener);
        void setOnSubmit(View.OnClickListener listener);
        void setOnFormValidateListener(InputGroup.OnFormValidateListener listener);
        void setEnableSubmit(boolean enable);
        void startHome();
        void setResultError(CharSequence error);
        void setInputError(String fieldName, String message);
        void clearErrors();
        void setInputErrors(Map<String, List<String>> fieldsErrors);
    }

    public interface RegisterView extends MvpView, ProgressView, ErrorViewWithRetry {
        void setOnTextChangedListener(InputGroup.OnTextChangedListener listener);
        void setOnSubmit(View.OnClickListener listener);
        void setOnFormValidateListener(InputGroup.OnFormValidateListener listener);
        void setEnableSubmit(boolean enable);
        void startHome();
        void validate(boolean withError);
        void setInputError(String fieldName, String message);
        void setResultError(CharSequence error);
        void clearErrors();
        void setInputErrors(Map<String, List<String>> data);
        void startConfirmation(Uri endpoint);
    }
}
