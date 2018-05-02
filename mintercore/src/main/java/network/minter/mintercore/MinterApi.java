package network.minter.mintercore;

import com.google.gson.GsonBuilder;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import network.minter.mintercore.api.ApiService;
import network.minter.mintercore.crypto.NativeSecp256k1;
import network.minter.mintercore.repo.AccountRepository;
import network.minter.mintercore.repo.CoinRepository;
import network.minter.mintercore.repo.TransactionRepository;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterApi {
    public final static String DEFAULT_COIN = "MNT";
    public final static String MINTER_PREFIX = "Mx";
    private final static String BASE_URL = "https://minter-testnet.dl-dev.ru";
    private static MinterApi INSTANCE;
    private ApiService.Builder mApiService;
    private AccountRepository mAccountRepository;
    private CoinRepository mCoinRepository;
    private TransactionRepository mTransactionRepository;

    private MinterApi() {
        mApiService = new ApiService.Builder(BASE_URL, new GsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
    }

    public static void initialize() {
        initialize(false);
    }

    public static void initialize(boolean debug) {
        INSTANCE = new MinterApi();
        INSTANCE.mApiService.setDebug(true);

        NativeSecp256k1.init();

        if (!NativeSecp256k1.isEnabled()) {
            throw new RuntimeException("Unable to load secp256k1 library");
        }


        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        if (debug) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    super.log(priority, "MinterCore-" + tag, message, t);
                }


            });

            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    public static MinterApi getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Have you forget to call MinterApi.initialize(Context ctx)?");
        }
        return INSTANCE;
    }

    public AccountRepository account() {
        if (mAccountRepository == null) {
            mAccountRepository = new AccountRepository(mApiService);
        }

        return mAccountRepository;
    }

    public TransactionRepository transactions() {
        if (mTransactionRepository == null) {
            mTransactionRepository = new TransactionRepository(mApiService);
        }

        return mTransactionRepository;
    }

    public CoinRepository coin() {
        if (mCoinRepository == null) {
            mCoinRepository = new CoinRepository(mApiService);
        }

        return mCoinRepository;
    }


}
