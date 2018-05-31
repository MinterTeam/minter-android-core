package network.minter.explorerapi;

import com.google.gson.GsonBuilder;


import java.math.BigInteger;

import network.minter.explorerapi.repo.TransactionRepository;
import network.minter.mintercore.BuildConfig;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.api.converters.BigIntegerDeserializer;
import network.minter.mintercore.internal.api.converters.BytesDataDeserializer;
import network.minter.mintercore.internal.api.converters.MinterAddressDeserializer;
import network.minter.mintercore.util.BytesData;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterExplorerApi {
    private final static String BASE_API_URL = "https://testnet.explorer.minter.network";
    private static MinterExplorerApi INSTANCE;
    private ApiService.Builder mApiService;
    private TransactionRepository mTransactionRepository;

    public static void initialize(boolean debug) {
        if(INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterExplorerApi();
        INSTANCE.mApiService.setDebug(debug);

        if(debug) {
            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    private MinterExplorerApi() {
        mApiService = new ApiService.Builder(BASE_API_URL, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
        mApiService.setDateFormat("yyyy-MM-dd HH:mm:ssX");
    }

    public static MinterExplorerApi getInstance() {
        return INSTANCE;
    }

    public TransactionRepository transactions() {
        if(mTransactionRepository == null) {
            mTransactionRepository = new TransactionRepository(mApiService);
        }

        return mTransactionRepository;
    }


    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

        return out;
    }
}
