package network.minter.bipwallet.home;

import java.util.List;

import dagger.Component;
import network.minter.bipwallet.coins.ui.CoinsTabFragment;
import network.minter.bipwallet.home.ui.HomeActivity;
import network.minter.bipwallet.internal.di.WalletComponent;
import network.minter.bipwallet.receiving.ui.ReceivingTabFragment;
import network.minter.bipwallet.sending.ui.SendingTabFragment;
import network.minter.bipwallet.settings.ui.SettingsListsFragment;
import network.minter.bipwallet.settings.ui.SettingsTabFragment;
import network.minter.bipwallet.settings.ui.SettingsUpdateFieldDialog;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Component(dependencies = WalletComponent.class, modules = {
        HomeModule.class
})
@HomeScope
public interface HomeComponent {

    void inject(HomeActivity activity);
    void inject(CoinsTabFragment fragment);
    void inject(SendingTabFragment fragment);
    void inject(ReceivingTabFragment fragment);
    void inject(SettingsTabFragment fragment);
    void inject(SettingsListsFragment fragment);
    void inject(SettingsUpdateFieldDialog fragment);

    @HomeTabsClasses
    List<Class<? extends HomeTabFragment>> tabsClasses();
    HomeActivity homeActivity();
}
