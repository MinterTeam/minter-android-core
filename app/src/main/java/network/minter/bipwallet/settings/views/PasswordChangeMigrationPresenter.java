/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.bipwallet.settings.views;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.models.SecretData;
import network.minter.bipwallet.advanced.repo.SecretStorage;
import network.minter.bipwallet.internal.dialogs.WalletConfirmDialog;
import network.minter.bipwallet.internal.dialogs.WalletProgressDialog;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.views.migration.MigrationException;
import network.minter.mintercore.crypto.EncryptedString;
import network.minter.mintercore.crypto.HashUtil;
import network.minter.my.models.MyAddressData;
import network.minter.my.models.MyResult;
import network.minter.my.repo.MyAddressRepository;
import network.minter.my.repo.MyProfileRepository;
import timber.log.Timber;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCallMy;
import static network.minter.bipwallet.settings.views.migration.MigrationException.STEP_1_UPDATE_PASSWORD;
import static network.minter.bipwallet.settings.views.migration.MigrationException.STEP_2_GET_REMOTE_ADDRESS_LIST;
import static network.minter.bipwallet.settings.views.migration.MigrationException.STEP_3_RE_ENCRYPT_REMOTE_DATA;
import static network.minter.bipwallet.settings.views.migration.MigrationException.STEP_4_UPDATE_ENCRYPTED_DATA_REMOTE;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class PasswordChangeMigrationPresenter extends MvpBasePresenter<SettingsTabModule.PasswordChangeMigrationView> {

    @Inject MyProfileRepository profileRepo;
    @Inject MyAddressRepository addressRepo;
    @Inject SecretStorage secretStorage;

    private String mOldPassword;
    private String mNewPassword;
    private WeakReference<WalletProgressDialog> mProgressDialog;
    private int mProgress = 0;
    private SparseArray<PublishSubject<Object>> mRetryHandlers = new SparseArray<>(4);
    private Queue<SecretData> mMigrationQueue;

    @Inject
    public PasswordChangeMigrationPresenter() {
        mRetryHandlers.put(STEP_1_UPDATE_PASSWORD, PublishSubject.create());
        mRetryHandlers.put(STEP_2_GET_REMOTE_ADDRESS_LIST, PublishSubject.create());
        mRetryHandlers.put(STEP_3_RE_ENCRYPT_REMOTE_DATA, PublishSubject.create());
        mRetryHandlers.put(STEP_4_UPDATE_ENCRYPTED_DATA_REMOTE, PublishSubject.create());
    }

    @Override
    public void attachView(SettingsTabModule.PasswordChangeMigrationView view) {
        super.attachView(view);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mMigrationQueue = secretStorage.getOrCreateMigrationQueue();
        getViewState().setFormValidateListener(this::onFormValidate);
        getViewState().setTextChangedListener(this::onTextChanged);
        getViewState().setOnClickSubmit(this::onSubmit);
    }

    private Function<Observable<Throwable>, ObservableSource<?>> migrationStepFailed(@MigrationException.MigrationStep int step) {
        return throwableObservable -> throwableObservable.switchMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            onError(throwable, mRetryHandlers.get(step));
            return mRetryHandlers.get(step);
        });
    }

    private void log(int step, String message) {
        Timber.tag("EncryptedMigration").d("[%d] %s", step, message);
    }

    private void onSubmit(View view) {
        getViewState().startDialog(ctx -> {
            WalletProgressDialog dialog = new WalletProgressDialog.Builder(ctx, "Changing password")
                    .setText("Please wait, we are changing password and re-encrypting all your secret data. \n\nDO NOT close app while this process does not finished. It may corrupt encrypted data!")
                    .create();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            mProgressDialog = new WeakReference<>(dialog);

            log(1, "Updating password");
            rxCallMy(profileRepo.updateField("password", HashUtil.sha256HexDouble(mNewPassword)))
                    .subscribeOn(Schedulers.io())
                    .retryWhen(migrationStepFailed(STEP_1_UPDATE_PASSWORD))
                    // getting my minter addresses with id's
                    .switchMap(res -> {
                        log(2, "Get remote address list");
                        secretStorage.setEncryptionKey(mNewPassword);
                        return rxCallMy(addressRepo.getAddresses());
                    })
                    .retryWhen(migrationStepFailed(STEP_2_GET_REMOTE_ADDRESS_LIST))
                    // comparing local and remote addresses and get id to update on server
                    .switchMap(res -> Observable.create((ObservableOnSubscribe<List<Observable<MyResult<Object>>>>) emitter -> {
                        log(3, "Encrypt data");
                        setProgress(0);
                        mProgress = 1;
                        resetProgressNonIndeterminate(res.data.size() * 2);

                        final List<MyAddressData> addresses = new ArrayList<>(res.data);
                        final List<Observable<MyResult<Object>>> reEncryptedAddresses = new ArrayList<>(addresses.size());
                        for (SecretData dataLocal : secretStorage.getSecrets().values()) {
                            if (dataLocal == null) {
                                emitter.onError(new RuntimeException("Unable to get secret from local storage"));
                                return;
                            }

                            MyAddressData dataRemote = null;
                            for (MyAddressData item : addresses) {
                                if (dataLocal.getMinterAddress().equals(item.address)) {
                                    dataRemote = item;
                                    break;
                                }
                            }
                            if (dataRemote == null) {
                                emitter.onError(new RuntimeException(String.format("Can't find remote address %s", dataLocal.getMinterAddress())));
                                return;
                            }

//                            if(true) {
//                                emitter.onError(new RuntimeException("Test fail"));
//                                return;
//                            }
//61220abfa10d67fb976b983b7292bb69d72275a78ae1ac99056caf04caa4e463 0bea2b1f68fecde452eb363247841f5a846f7a7f7fe89cf712fe9792526e7928
                            dataRemote.encrypted = new EncryptedString(dataLocal.getSeedPhrase(), secretStorage.getEncryptionKey());
                            reEncryptedAddresses.add(rxCallMy(addressRepo.updateAddress(dataRemote)).delay(1, TimeUnit.SECONDS));

                            Thread.sleep(1000);
                            setProgress(mProgress);
                            mProgress++;
                        }

                        emitter.onNext(reEncryptedAddresses);
                        emitter.onComplete();
                    }))
                    .retryWhen(migrationStepFailed(STEP_3_RE_ENCRYPT_REMOTE_DATA))
                    // concat list updates and updated it
                    .switchMap(Observable::concat)
                    .retryWhen(migrationStepFailed(STEP_4_UPDATE_ENCRYPTED_DATA_REMOTE))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                        log(4, "Send data");
                        mProgress++;
                        setProgress(mProgress);
                    }, Timber::e, this::onMigrationSuccess);


            return mProgressDialog.get();
        });
    }

    private void onMigrationSuccess() {
        callOnProgressDialog(d -> {
            d.dismiss();
            getViewState().startDialog(ctx2 -> new WalletConfirmDialog.Builder(ctx2, "Success!")
                    .setText("Password successfully migrated!")
                    .setPositiveAction("Ok", (d2, w2) -> {
                        d2.dismiss();
                        getViewState().finish();
                    })
                    .create());
        });
    }

    private void setProgress(final int progress) {
        callOnProgressDialog(d -> d.setProgress(progress));
    }

    private void callOnProgressDialog(Consumer<WalletProgressDialog> cb) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mProgressDialog == null) {
                return;
            }

            final WalletProgressDialog d = mProgressDialog.get();
            if (d == null) {
                return;
            }

            try {
                cb.accept(d);
            } catch (Exception e) {
                Timber.e(e);
            }
        });
    }

    private void resetProgressNonIndeterminate(final int max) {
        callOnProgressDialog(d -> {
            d.setMax(max);
            d.setIndeterminate(false);
            d.setProgress(0);
        });
    }

    private void onError(Throwable throwable, final PublishSubject<Object> errorRetry) {
        Timber.w(throwable);
        log(1, "Updating password (OLD)");
        secretStorage.setEncryptionKey(mOldPassword);
        safeSubscribeIoToUi(rxCallMy(profileRepo.updateField("password", HashUtil.sha256HexDouble(mOldPassword))))
                .subscribe(res -> {
                    // revert old password
                    showErrorDialog(throwable, errorRetry);
                }, t -> {
                    showErrorDialog(t, mRetryHandlers.get(STEP_1_UPDATE_PASSWORD));
                });
    }

    private void showErrorDialog(Throwable throwable, PublishSubject<Object> errorRetry) {
        String message = String.format("Unable to migrate secret data: %s", throwable.getMessage());
        if (throwable instanceof MigrationException) {
            MigrationException ex = ((MigrationException) throwable);
            switch (ex.getStep()) {
                case STEP_1_UPDATE_PASSWORD:
                    message = String.format("Unable to update password. %s", throwable.getMessage());
                    break;
                case STEP_2_GET_REMOTE_ADDRESS_LIST:
                    message = String.format("Unable to resolve MyMinter addresses. %s", throwable.getMessage());
                    break;
                case STEP_3_RE_ENCRYPT_REMOTE_DATA:
                    message = String.format("Unable to encrypt data with new password. %s", throwable.getMessage());
                    break;
                case STEP_4_UPDATE_ENCRYPTED_DATA_REMOTE:
                    message = String.format("Unable to upload newly encrypted data. %s", throwable.getMessage());
                    break;
            }
        }

        final String finalMessage = message;
        getViewState().startDialog(ctx -> new WalletConfirmDialog.Builder(ctx, "Error")
                .setText(finalMessage)
                .setPositiveAction("Try again", (d, w) -> {
                    errorRetry.onNext(new Object());
                    d.dismiss();
                    getViewState().startDialog(ctx2 -> {
                        WalletProgressDialog dialog = new WalletProgressDialog.Builder(ctx, "Changing password")
                                .setText("Please wait, we are changing password and re-encrypting all your secret data. \n\nDO NOT close app while this process does not finished. It may corrupt encrypted data!")
                                .create();
                        dialog.setCancelable(false);
                        mProgressDialog = new WeakReference<>(dialog);
                        return mProgressDialog.get();
                    });
                })
                .setNegativeAction("Cancel", (d, w) -> {
                    d.dismiss();
                })
                .create());
    }

    private void onTextChanged(EditText editText, boolean valid) {
        switch (editText.getId()) {
            case R.id.input_password_old:
                mOldPassword = editText.getText().toString();
                break;
            case R.id.input_password_new_repeat:
                mNewPassword = editText.getText().toString();
                break;
        }
    }

    private void onFormValidate(boolean valid) {
        getViewState().setEnableSubmit(valid);
    }
}
