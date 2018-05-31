package network.minter.bipwallet.internal.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.GsonBuilder;

import javax.inject.Named;

import dagger.Component;
import network.minter.bipwallet.internal.Wallet;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.auth.SessionStorage;
import network.minter.bipwallet.internal.helpers.DisplayHelper;
import network.minter.bipwallet.internal.helpers.ImageHelper;
import network.minter.bipwallet.internal.helpers.NetworkHelper;
import network.minter.bipwallet.internal.storage.KVStorage;
import network.minter.bipwallet.advanced.repo.SecretLocalRepository;
import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.my.repo.AddressRepository;
import network.minter.my.repo.AuthRepository;
import network.minter.my.repo.InfoRepository;
import network.minter.my.repo.ProfileRepository;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Component(modules = {
        WalletModule.class,
        HelpersModule.class,
        RepoModule.class,
        InjectorsModule.class,
})
@WalletApp
public interface WalletComponent {

    void inject(Wallet app);

    // app
    Context context();
    Resources res();

    ApiService.Builder apiBuilder();
    AuthSession session();
    SessionStorage sessionStorage();
    KVStorage storage();

    @Named("uuid")
    String uuid();

    // helpers
    DisplayHelper display();
    NetworkHelper network();
    ImageHelper image();
    SharedPreferences prefs();
    GsonBuilder gsonBuilder();

    // repositories
    SecretLocalRepository secretRepo();
    TransactionRepository explorerTransactionsRepo();
    AuthRepository authRepo();
    InfoRepository infoRepo();
    AddressRepository addressRepo();
    ProfileRepository profileRepo();
}
