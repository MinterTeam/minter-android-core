package network.minter.bipwallet.settings.views;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import network.minter.bipwallet.home.HomeScope;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.settings.SettingsTabModule;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@HomeScope
@InjectViewState
public class SettingsTabPresenter extends MvpBasePresenter<SettingsTabModule.SettingsTabView> {

    @Inject
    public SettingsTabPresenter() {
    }

    @Override
    public void attachView(SettingsTabModule.SettingsTabView view) {
        super.attachView(view);
        getViewState().showLists();
    }

    public void onLogout() {
        getViewState().startLogin();
    }


}
