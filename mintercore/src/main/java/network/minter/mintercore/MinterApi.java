package network.minter.mintercore;

import com.google.gson.GsonBuilder;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.Security;

import network.minter.mintercore.api.ApiService;
import network.minter.mintercore.api.converters.BigIntegerDeserializer;
import network.minter.mintercore.api.converters.BytesDataDeserializer;
import network.minter.mintercore.api.converters.MinterAddressDeserializer;
import network.minter.mintercore.bip39.NativeBip39;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.NativeSecp256k1;
import network.minter.mintercore.models.BytesData;
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
        mApiService = new ApiService.Builder(BASE_URL, getGsonBuilder());
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
        NativeBip39.init();

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

    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

        return out;
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
