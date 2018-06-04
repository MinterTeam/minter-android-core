package network.minter.bipwallet.auth.views;

import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.repo.SecretLocalRepository;
import network.minter.bipwallet.auth.AuthModule;
import network.minter.bipwallet.auth.ui.InputGroup;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.di.annotations.ActivityScope;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.mintercore.bip39.MnemonicResult;
import network.minter.mintercore.bip39.NativeBip39;
import network.minter.mintercore.crypto.EncryptedString;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.my.models.AddressData;
import network.minter.my.models.LoginData;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.RegisterData;
import network.minter.my.repo.AuthRepository;

import static network.minter.bipwallet.internal.ReactiveAdapter.convertToErrorResult;
import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@ActivityScope
@InjectViewState
public class RegisterPresenter extends MvpBasePresenter<AuthModule.RegisterView> {
    @Inject AuthRepository authRepo;
    @Inject SecretLocalRepository secretRepo;
    @Inject AuthSession session;

    private RegisterData mRegisterData;
    private boolean mValid;

    @Inject
    public RegisterPresenter() {
        mRegisterData = new RegisterData();
    }

    @Override
    public void attachView(AuthModule.RegisterView view) {
        super.attachView(view);

        getViewState().setOnSubmit(this::onSubmit);
        getViewState().setOnTextChangedListener(new InputGroup.OnTextChangedListener() {
            @Override
            public void onTextChanged(EditText editText, boolean valid) {
                final String val = editText.getText().toString();
                switch (editText.getId()) {
                    case R.id.inputUsername:
                        mRegisterData.username = val;
                        if(!mRegisterData.username.isEmpty() && mRegisterData.username.charAt(0) == '@') {
                            mRegisterData.username = mRegisterData.username.substring(1);
                        }
                        break;
                    case R.id.inputPasswordRepeat:
                        mRegisterData.password = val;
                        break;
                    case R.id.inputEmail:
                        mRegisterData.email = val;
                        break;
                    case R.id.inputPhone:
                        mRegisterData.phone = val;
                        break;
                }

                getViewState().validate(true);
            }
        });
        getViewState().setOnFormValidateListener(valid -> {
            getViewState().setEnableSubmit(valid);
            mValid = valid;
        });
    }

    private void onSubmit(View view) {
        if(!mValid) {
            return;
        }

        getViewState().clearErrors();
        getViewState().showProgress();

        final SecureRandom mRandom = new SecureRandom();
        final MnemonicResult mnemonicResult = NativeBip39.encodeBytes(mRandom.generateSeed(16));
        MinterAddress address = secretRepo.add(mnemonicResult);
        mRegisterData.language = Locale.getDefault().toString();
        mRegisterData.mainAddress = new AddressData();
        mRegisterData.mainAddress.address = address;
        mRegisterData.mainAddress.isMain = true;
        mRegisterData.mainAddress.isServerSecured = true;
        try {
            mRegisterData.mainAddress.encrypted = new EncryptedString(StringHelper.bytesToHexString(mnemonicResult.toSeed()), mRegisterData.password);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | UnsupportedEncodingException e) {
            getViewState().onError(e);
            secretRepo.destroy();
            return;
        }

        rxCall(authRepo.register(mRegisterData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(convertToErrorResult())
                .subscribe(userResult -> {
                    if(!userResult.isSuccess()) {
                        secretRepo.destroy();
                        getViewState().hideProgress();
                        getViewState().setResultError(userResult.error.message);
                        getViewState().setInputErrors(userResult.getError().getData());
                        return;
                    }

                    if(userResult.data.confirmations != null && !userResult.data.confirmations.isEmpty()) {
                        ProfileRequestResult.Confirmation confirmation = userResult.data.confirmations.get(0);
                        if(confirmation.type != null) {
                            getViewState().startConfirmation(confirmation.endpoint);
                            return;
                        }
                    }

                    final LoginData loginData = new LoginData();
                    loginData.username = mRegisterData.username;
                    loginData.password = mRegisterData.password;
                    safeSubscribeIoToUi(rxCall(authRepo.login(loginData)))
                            .subscribe(loginResult -> {
                                getViewState().hideProgress();
                                if(!loginResult.isSuccess()) {
                                    getViewState().setResultError(loginResult.error.message);
                                    getViewState().setInputErrors(loginResult.getError().getData());
                                    return;
                                }

                                session.login(loginResult.data.token.accessToken, loginResult.data, AuthSession.AuthType.Basic);
                                getViewState().startHome();
                            });

                }, Wallet.Rx.errorHandler(getViewState()));
    }
}
