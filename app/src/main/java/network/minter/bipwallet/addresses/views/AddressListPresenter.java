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

package network.minter.bipwallet.addresses.views;

import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.content.Intent;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import network.minter.bipwallet.addresses.AddressManageModule;
import network.minter.bipwallet.addresses.adapters.AddressListAdapter;
import network.minter.bipwallet.addresses.adapters.AddressListDataSource;
import network.minter.bipwallet.advanced.models.SecretData;
import network.minter.bipwallet.advanced.repo.SecretStorage;
import network.minter.bipwallet.internal.auth.AuthSession;
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
public class AddressListPresenter extends MvpBasePresenter<AddressManageModule.AddressListView> {
    private final static int REQUEST_ADDRESS_ITEM = 100;
    private final static int REQUEST_FOR_RESULT = 200;
    @Inject AuthSession session;
    @Inject SecretStorage secretRepo;
    private AddressRepository addressRepo;
    private AddressListAdapter mAdapter;
    private List<AddressData> mItems = new ArrayList<>(0);
    private RxPagedListBuilder<Integer, AddressData> mPageBuilder;
    private Disposable mPageDisposable;

    @Inject
    public AddressListPresenter(AddressRepository repo) {
        addressRepo = repo;

        mAdapter = new AddressListAdapter();
        mAdapter.setOnAddressClickListener((v, address) -> getViewState().startAddressItem(REQUEST_ADDRESS_ITEM, address));
        mAdapter.setOnSetMainListener(this::onSetMain);

        AddressListDataSource.Factory pageFactory = new AddressListDataSource.Factory(addressRepo);
        final PagedList.Config cfg = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        mPageBuilder = new RxPagedListBuilder<>(pageFactory, cfg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reload();
//        mAdapter.clear();
//        loadAddresses();
    }

    public void onClickAddAddress() {
        if (session.getRole() == AuthSession.AuthType.Advanced) {
            getViewState().startCreateAddress(REQUEST_FOR_RESULT);
        } else {
            getViewState().showProgress("Please, wait", "Creating address...");
            final SecretData secretData = SecretStorage.generateAddress();
            secretRepo.add(secretData);
            boolean isMain = Stream.of(mItems).filter(item -> item.isMain).count() == 0;
            safeSubscribeIoToUi(
                    rxCall(addressRepo.addAddress(secretData.toAddressData(isMain, true, secretRepo.getEncryptionKey())))
            ).subscribe(res -> {
                reload();
                getViewState().hideProgress();
//                mAdapter.clear();
//                loadAddresses();
            });
        }
    }

    @Override
    public void attachView(AddressManageModule.AddressListView view) {
        super.attachView(view);
        getViewState().setAdapter(mAdapter);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        reload();
    }

    private void reload() {
        if (mPageDisposable != null && !mPageDisposable.isDisposed()) {
            mPageDisposable.dispose();
            getViewState().scrollToPosition(0);
        }

        mPageDisposable = mPageBuilder.buildObservable()
                .subscribe(res -> {
                    getViewState().hideProgress();
                    mAdapter.submitList(res);
                });
    }

    private void onSetMain(boolean isMain, AddressData addressData) {
        safeSubscribeIoToUi(rxCall(addressRepo.setAddressMain(isMain, addressData)))
                .subscribe(res -> {
                    getViewState().scrollToPosition(0);
                    reload();
                });

    }

}
