package network.minter.bipwallet.addresses.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.addresses.AddressManageModule;
import network.minter.bipwallet.addresses.ui.AddressItemActivity;
import network.minter.bipwallet.internal.helpers.IntentHelper;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.my.models.AddressData;
import network.minter.my.repo.AddressRepository;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class AddressItemPresenter extends MvpBasePresenter<AddressManageModule.AddressItemView> {
    @Inject AddressRepository addressRepo;

    private AddressData mAddress;

    @Inject
    public AddressItemPresenter() {
    }

    @Override
    public void handleExtras(Intent intent) {
        super.handleExtras(intent);
        mAddress = IntentHelper.getParcelExtraOrError(intent, AddressItemActivity.EXTRA_ADDRESS_DATA, "AddressData required");
    }

    @Override
    public void attachView(AddressManageModule.AddressItemView view) {
        super.attachView(view);

        getViewState().setAddress(mAddress.address.toString());
        getViewState().setSecuredBy(mAddress.isServerSecured ? "Bip Wallet" : "You");
        getViewState().setOnClickDelete(this::onClickDelete);
    }

    private void onClickDelete(View view) {
        getViewState().startRemoveDialog("Attention", "Once you have to deleted your address, it can't be restored. Are you sure to proceed?", "Yes", "No", this::onClickDeleteAddress);
    }

    private void onClickDeleteAddress(DialogInterface dialogInterface, int which) {
        getViewState().showProgress("Deleting in progress");
        rxCall(addressRepo.delete(mAddress))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    getViewState().hideProgress();
                    getViewState().finishWithResult(Activity.RESULT_FIRST_USER);
                });

    }
}
