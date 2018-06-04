package network.minter.bipwallet.settings.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.addresses.ui.AddressListActivity;
import network.minter.bipwallet.auth.ui.AuthActivity;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.home.HomeTabFragment;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.system.BackPressedDelegate;
import network.minter.bipwallet.internal.system.BackPressedListener;
import network.minter.bipwallet.internal.views.list.BorderedItemSeparator;
import network.minter.bipwallet.internal.views.list.NonScrollableLinearLayoutManager;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.views.SettingsTabPresenter;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SettingsTabFragment extends HomeTabFragment implements SettingsTabModule.SettingsTabView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list_main) RecyclerView listMain;
    @BindView(R.id.list_additional) RecyclerView listAdditional;

    @Inject Provider<SettingsTabPresenter> presenterProvider;
    @InjectPresenter SettingsTabPresenter presenter;
    @Inject BackPressedDelegate backPressDelegate;

    private SettingsUpdateFieldDialog fieldFragment;

    @Override
    public void onAttach(Context context) {
        HomeModule.getComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_settings, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        getActivity().getMenuInflater().inflate(R.menu.menu_tab_settings, toolbar.getMenu());
        toolbar.setOnMenuItemClickListener(SettingsTabFragment.this::onOptionsItemSelected);

        backPressDelegate.addBackPressedListener(new BackPressedListener() {
            @Override
            public boolean onBackPressed() {
                if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                    getChildFragmentManager().popBackStack();
                    return false;
                }

                return true;
            }
        });

        return view;
    }

    @Override
    public void setMainAdapter(RecyclerView.Adapter<?> mainAdapter) {
        listMain.setLayoutManager(new NonScrollableLinearLayoutManager(getActivity()));
        listMain.addItemDecoration(new BorderedItemSeparator(getActivity(), R.drawable.shape_bottom_separator, true, true));
        listMain.setAdapter(mainAdapter);
    }

    @Override
    public void setAdditionalAdapter(RecyclerView.Adapter<?> additionalAdapter) {
        listAdditional.setLayoutManager(new NonScrollableLinearLayoutManager(getActivity()));
        listAdditional.addItemDecoration(new BorderedItemSeparator(getActivity(), R.drawable.shape_bottom_separator, true, true));
        listAdditional.setAdapter(additionalAdapter);
    }

    @Override
    public void startManageAddresses() {
        getActivity().startActivity(new Intent(getActivity(), AddressListActivity.class));
    }

    @Override
    public void startEditField(SettingsFieldType type, CharSequence label, String fieldName, String value) {
        fieldFragment = SettingsUpdateFieldDialog.newInstance(type, label, fieldName, value);
        fieldFragment.setOnSaveListener(presenter::onUpdateProfile);
        fieldFragment.show(getChildFragmentManager(), SettingsUpdateFieldDialog.class.getName());
    }

    @Override
    public void startChangePassword() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            presenter.onLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HomeModule.getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startLogin() {
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Wallet.app().session().logout();
        Wallet.app().secretRepo().destroy();

        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @ProvidePresenter
    SettingsTabPresenter providePresenter() {
        return presenterProvider.get();
    }
}
