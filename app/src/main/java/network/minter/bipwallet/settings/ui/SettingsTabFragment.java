package network.minter.bipwallet.settings.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import network.minter.bipwallet.auth.ui.AuthActivity;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.home.HomeTabFragment;
import network.minter.bipwallet.internal.RevealFragment;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.system.BackPressedDelegate;
import network.minter.bipwallet.internal.system.BackPressedListener;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.views.SettingsTabPresenter;
import timber.log.Timber;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SettingsTabFragment extends HomeTabFragment implements SettingsTabModule.SettingsTabView {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject Provider<SettingsTabPresenter> presenterProvider;
    @InjectPresenter SettingsTabPresenter presenter;

    @Inject BackPressedDelegate backPressDelegate;

    private SettingsListsFragment mListsFragment;
    private SettingsUpdateFieldDialog mUpdateFieldFragment;

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

        mListsFragment = new SettingsListsFragment();

        getActivity().getMenuInflater().inflate(R.menu.menu_tab_settings, toolbar.getMenu());
        toolbar.setOnMenuItemClickListener(SettingsTabFragment.this::onOptionsItemSelected);

        backPressDelegate.addBackPressedListener(new BackPressedListener() {
            @Override
            public boolean onBackPressed() {
                if(getChildFragmentManager().getBackStackEntryCount() > 0) {
                    getChildFragmentManager().popBackStack();
                    return false;
                }

                return true;
            }
        });

        return view;
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

    @Override
    public void showLists() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, mListsFragment, "settings_list")
                .commit();
    }

    private SettingsUpdateFieldDialog fieldFragment;

    @Override
    public void startEditField(View shared, CharSequence label, String fieldName, String value) {
        fieldFragment = SettingsUpdateFieldDialog.newInstance(label, fieldName, value);
        fieldFragment.show(getChildFragmentManager(), SettingsUpdateFieldDialog.class.getName());


//        TransitionSet sharedSet = new TransitionSet();
//        sharedSet.addTransition(new ChangeBounds());
//        sharedSet.addTransition(new ChangeTransform());
//        sharedSet.addTarget(R.id.field_label);
//
//        TransitionSet commonSet = new TransitionSet();
//        commonSet.addTransition(new Slide(Gravity.BOTTOM));
//        commonSet.addTransition(new Fade());
//        commonSet.setDuration(sharedSet.getDuration() * 2);
//        commonSet.addTarget(R.id.field_layout);
//        commonSet.addTarget(R.id.field_action);
//
//        fieldFragment.setSharedElementEnterTransition(sharedSet);
//        fieldFragment.setSharedElementReturnTransition(sharedSet);
//        fieldFragment.setEnterTransition(commonSet);
//        fieldFragment.setExitTransition(commonSet);
//
//        getChildFragmentManager().beginTransaction()
//                .hide(mListsFragment)
//                .add(R.id.container, fieldFragment, "settings_field_update")
//                .addToBackStack(null)
//                .addSharedElement(shared, ViewCompat.getTransitionName(shared))
//                .commit();
    }

    @ProvidePresenter
    SettingsTabPresenter providePresenter() {
        return presenterProvider.get();
    }
}
