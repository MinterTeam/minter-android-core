package network.minter.bipwallet.addresses.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.addresses.AddressManageModule;
import network.minter.bipwallet.addresses.views.AddressListPresenter;
import network.minter.bipwallet.internal.BaseMvpInjectActivity;
import network.minter.bipwallet.internal.views.list.NonScrollableLinearLayoutManager;
import network.minter.my.models.AddressData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressListActivity extends BaseMvpInjectActivity implements AddressManageModule.AddressListView {

    @Inject Provider<AddressListPresenter> presenterProvider;
    @InjectPresenter AddressListPresenter presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;


    @BindView(R.id.list) RecyclerView list;


    @Override
    public void setAdapter(RecyclerView.Adapter<?> adapter) {
        list.setLayoutManager(new NonScrollableLinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    public void startAddressItem(int requestCode, AddressData address) {
        new AddressItemActivity.Builder(this, address).start(requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address_manage, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @ProvidePresenter
    AddressListPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        setupToolbar(toolbar);
    }
}
