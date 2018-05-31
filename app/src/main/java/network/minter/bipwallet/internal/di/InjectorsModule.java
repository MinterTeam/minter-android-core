package network.minter.bipwallet.internal.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import network.minter.bipwallet.auth.ui.AuthActivity;
import network.minter.bipwallet.auth.ui.RegisterActivity;
import network.minter.bipwallet.auth.ui.SigninActivity;
import network.minter.bipwallet.auth.ui.SplashFragment;
import network.minter.bipwallet.advanced.AdvancedModeModule;
import network.minter.bipwallet.advanced.ui.AdvancedGenerateActivity;
import network.minter.bipwallet.advanced.ui.AdvancedMainActivity;
import network.minter.bipwallet.auth.ui.AuthFragment;
import network.minter.bipwallet.internal.di.annotations.ActivityScope;
import network.minter.bipwallet.internal.di.annotations.FragmentScope;

/**
 * Dogsy. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Module(includes = AndroidSupportInjectionModule.class)
public interface InjectorsModule {

    @ContributesAndroidInjector
    @ActivityScope
    AuthActivity authActivityInjector();

    @ContributesAndroidInjector
    @FragmentScope
    SplashFragment splashFragmentInjector();

    @ContributesAndroidInjector
    @FragmentScope
    AuthFragment authFragmentInjector();

    @ContributesAndroidInjector
    @ActivityScope
    SigninActivity signinActivityInjector();

    @ContributesAndroidInjector
    @ActivityScope
    RegisterActivity registerActivityInjector();

    @ContributesAndroidInjector(modules = AdvancedModeModule.class)
    @ActivityScope
    AdvancedMainActivity advancedMainActivity();

    @ContributesAndroidInjector
    @ActivityScope
    AdvancedGenerateActivity advancedGenerateActivity();

//    @ContributesAndroidInjector
//    @FragmentScope
//    SettingsListsFragment settingsListsFragmentInjector();
//
//    @ContributesAndroidInjector
//    @FragmentScope
//    SettingsUpdateFieldFragment settingsUpdateFieldFragmentInjector();

}
