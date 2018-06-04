package network.minter.bipwallet.addresses.views;

import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import network.minter.bipwallet.addresses.AddressManageModule;
import network.minter.bipwallet.addresses.adapters.AddressListAdapter;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.my.repo.AddressRepository;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class AddressListPresenter extends MvpBasePresenter<AddressManageModule.AddressListView> {
    private final static int REQUEST_ADDRESS_ITEM = 100;
    @Inject AddressRepository addressRepo;
    private AddressListAdapter mAdapter;

    @Inject
    public AddressListPresenter() {
        mAdapter = new AddressListAdapter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADDRESS_ITEM) {
            mAdapter.clear();
//            if(resultCode == Activity.RESULT_OK) {
            loadAddresses();
//            }
        }
    }

    @Override
    public void attachView(AddressManageModule.AddressListView view) {
        super.attachView(view);

        loadAddresses();

        getViewState().setAdapter(mAdapter);
        mAdapter.setOnAddressClickListener((v, address) -> getViewState().startAddressItem(REQUEST_ADDRESS_ITEM, address));
    }

    private void loadAddresses() {
        safeSubscribeIoToUi(rxCall(addressRepo.getAddresses())).subscribe(res -> {
            mAdapter.setData(res.data);
            if (mAdapter.getItemCount() != 0) {
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
            } else {
                mAdapter.notifyItemRangeInserted(0, res.data.size());
            }

        });
    }

}
