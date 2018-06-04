package network.minter.blockchainapi;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.blockchainapi.repo.AccountRepository;
import network.minter.blockchainapi.repo.CoinRepository;
import network.minter.blockchainapi.repo.TransactionRepository;
import network.minter.mintercore.BuildConfig;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.api.converters.BigIntegerDeserializer;
import network.minter.mintercore.internal.api.converters.BytesDataDeserializer;
import network.minter.mintercore.internal.api.converters.MinterAddressDeserializer;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterBlockChainApi {
    private final static String BASE_NODE_URL = "https://minter-testnet.dl-dev.ru";
    private static MinterBlockChainApi INSTANCE;
    private ApiService.Builder mApiService;
    private AccountRepository mAccountRepository;
    private CoinRepository mCoinRepository;
    private TransactionRepository mTransactionRepository;

    public MinterBlockChainApi() {
        this(BASE_NODE_URL);
    }

    public MinterBlockChainApi(@NonNull String baseNodeApiUrl) {
        mApiService = new ApiService.Builder(baseNodeApiUrl, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
    }

    public static void initialize(boolean debug) {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterBlockChainApi();
        INSTANCE.mApiService.setDebug(debug);
        if (debug) {
//            Timber.plant(new Timber.DebugTree() {
//                @Override
//                protected void log(int priority, String tag, String message, Throwable t) {
//                    super.log(priority, "MinterCore-" + tag, message, t);
//                }
//
//
//            });

            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    public static MinterBlockChainApi getInstance() {
        return INSTANCE;
    }

    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

        return out;
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
