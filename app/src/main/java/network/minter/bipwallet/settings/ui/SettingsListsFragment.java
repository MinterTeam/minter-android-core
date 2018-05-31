package network.minter.bipwallet.settings.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.internal.BaseFragment;
import network.minter.bipwallet.internal.BaseInjectFragment;
import network.minter.bipwallet.internal.views.list.BorderedItemSeparator;
import network.minter.bipwallet.internal.views.list.NonScrollableLinearLayoutManager;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.views.SettingsListsPresenter;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SettingsListsFragment extends BaseFragment implements SettingsTabModule.SettingsListsView {

    @Inject Provider<SettingsListsPresenter> presenterProvider;
    @InjectPresenter SettingsListsPresenter presenter;
    @BindView(R.id.list_main) RecyclerView listMain;
    @BindView(R.id.list_additional) RecyclerView listAdditional;
    private WeakReference<SettingsTabModule.SettingsTabView> mParent;

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (!(childFragment instanceof SettingsTabFragment)) {
            throw new IllegalStateException("SettingsListsFragment can be added only to SettingsTabFragment");
        }

        mParent = new WeakReference<>((SettingsTabModule.SettingsTabView) childFragment);
    }

    @Override
    public void onAttach(Context context) {
        HomeModule.getComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HomeModule.getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_list, container, false);
        ButterKnife.bind(this, view);
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

    }

    @Override
    public void startEditField(View shared, CharSequence label, String fieldName, String value) {
        if(getParentFragment() != null) {
            ((SettingsTabFragment) getParentFragment()).startEditField(shared, label, fieldName, value);
        }
    }

    @ProvidePresenter
    SettingsListsPresenter providePresenter() {
        return presenterProvider.get();
    }
}
