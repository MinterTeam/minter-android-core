package network.minter.bipwallet.internal.di;

import dagger.Module;
import dagger.Provides;
import network.minter.bipwallet.advanced.repo.SecretLocalRepository;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.storage.KVStorage;
import network.minter.explorerapi.MinterExplorerApi;
import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.my.MyMinterApi;
import network.minter.my.repo.AddressRepository;
import network.minter.my.repo.AuthRepository;
import network.minter.my.repo.InfoRepository;
import network.minter.my.repo.ProfileRepository;

/**
 * Dogsy. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Module
public class RepoModule {

    @Provides
    @WalletApp
    public SecretLocalRepository provideSecretRepository(KVStorage storage) {
        return new SecretLocalRepository(storage);
    }

    @Provides
    @WalletApp
    public TransactionRepository provideExplorerTransactionsRepo() {
        return MinterExplorerApi.getInstance().transactions();
    }

    @Provides
    @WalletApp
    public AuthRepository provideAuthRepository(MyMinterApi api) {
        return api.auth();
    }

    @Provides
    @WalletApp
    public AddressRepository provideAddressRepository(MyMinterApi api) {
        return api.address();
    }

    @Provides
    @WalletApp
    public ProfileRepository provideProfileRepository(MyMinterApi api) {
        return api.profile();
    }

    @Provides
    @WalletApp
    public MyMinterApi provideMyMinterApi(AuthSession session) {
        MyMinterApi.getInstance().getApiService().setAuthHeaderName("Authorization");
        MyMinterApi.getInstance().getApiService().setTokenGetter(() -> "Bearer " + session.getAuthToken());
        return MyMinterApi.getInstance();
    }

    @Provides
    @WalletApp
    public InfoRepository provideInfoRepository(MyMinterApi api) {
        return api.info();
    }
}
