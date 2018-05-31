package network.minter.bipwallet.auth.views;

import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.atomic.AtomicBoolean;

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
import network.minter.my.models.AddressData;
import network.minter.my.models.LoginData;
import network.minter.my.repo.AddressRepository;
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
public class SigninPresenter extends MvpBasePresenter<AuthModule.SigninView> {
    @Inject AuthRepository authRepo;
    @Inject SecretLocalRepository secretRepo;
    @Inject AuthSession session;
    @Inject AddressRepository addressRepo;

    private LoginData mLoginData = new LoginData();
    private boolean mValid = false;

    @Inject
    public SigninPresenter() {
    }

    @Override
    public void attachView(AuthModule.SigninView view) {
        super.attachView(view);
        getViewState().setOnSubmit(this::onSubmit);
        getViewState().setOnTextChangedListener(new InputGroup.OnTextChangedListener() {
            @Override
            public void onTextChanged(EditText editText, boolean valid) {
                if (editText.getId() == R.id.inputUsername) {
                    mLoginData.username = editText.getText().toString();
                    if (!mLoginData.username.isEmpty() && mLoginData.username.charAt(0) == '@') {
                        mLoginData.username = mLoginData.username.substring(1);
                    }
                } else if (editText.getId() == R.id.inputPassword) {
                    mLoginData.password = editText.getText().toString();
                }
            }
        });
        getViewState().setOnFormValidateListener(valid -> {
            getViewState().setEnableSubmit(valid);
            mValid = valid;
        });
    }

    private void onSubmit(View view) {
        if (!mValid) {
            return;
        }

        getViewState().setEnableSubmit(false);

        getViewState().clearErrors();
        getViewState().showProgress();

        rxCall(authRepo.login(mLoginData))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(convertToErrorResult())
                .retryWhen(getErrorResolver())
                .subscribe(userResult -> {
                    if (userResult.isSuccess()) {
                        session.login(
                                userResult.data.token.accessToken,
                                userResult.data,
                                AuthSession.AuthType.Basic
                        );
                    } else {
                        getViewState().hideProgress();
                        getViewState().setResultError(userResult.error.message);
                        getViewState().setInputErrors(userResult.getError().getData());
                        getViewState().setEnableSubmit(true);
                        return;
                    }

                    rxCall(addressRepo.getAddressesWithEncrypted())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .onErrorResumeNext(convertToErrorResult())
                            .subscribe(addressResult -> {
                                getViewState().hideProgress();
                                getViewState().setEnableSubmit(true);
                                if (addressResult.isSuccess()) {
                                    for (AddressData addressData : addressResult.data) {
                                        if(addressData.encrypted != null) {
                                            secretRepo.add(addressData.encrypted.decryptBytes(mLoginData.password));
                                        }
                                    }

                                    getViewState().startHome();
                                } else {
                                    getViewState().setResultError(addressResult.getError().message);
                                    getViewState().setInputErrors(addressResult.getError().getData());
                                }


                            }, Wallet.Rx.errorHandler(getViewState()));


                }, Wallet.Rx.errorHandler(getViewState()));
    }
}
