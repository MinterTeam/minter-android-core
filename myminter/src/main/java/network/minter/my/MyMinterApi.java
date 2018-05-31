package network.minter.my;

import android.net.Uri;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.mintercore.BuildConfig;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.api.converters.BigIntegerDeserializer;
import network.minter.mintercore.internal.api.converters.BytesDataDeserializer;
import network.minter.mintercore.internal.api.converters.EncryptedStringDeserializer;
import network.minter.mintercore.internal.api.converters.EncryptedStringSerializer;
import network.minter.mintercore.internal.api.converters.MinterAddressDeserializer;
import network.minter.mintercore.internal.api.converters.MinterAddressSerializer;
import network.minter.mintercore.internal.api.converters.UriDeserializer;
import network.minter.mintercore.internal.data.EncryptedString;
import network.minter.mintercore.util.BytesData;
import network.minter.my.repo.AddressRepository;
import network.minter.my.repo.AuthRepository;
import network.minter.my.repo.InfoRepository;
import network.minter.my.repo.ProfileRepository;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MyMinterApi {
    private static final String BASE_API_URL = "https://my.beta.minter.network";
    private static MyMinterApi INSTANCE;
    private ApiService.Builder mApiService;
    private AuthRepository mAuthRepository;
    private InfoRepository mInfoRepository;
    private AddressRepository mAddressRepository;
    private ProfileRepository mProfileRepository;

    private MyMinterApi() {
        mApiService = new ApiService.Builder(BASE_API_URL, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
        mApiService.setDateFormat("yyyy-MM-dd HH:mm:ssX");
    }

    public ApiService.Builder getApiService() {
        return mApiService;
    }

    public static void initialize(boolean debug) {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MyMinterApi();
        INSTANCE.mApiService.setDebug(debug);
        INSTANCE.mApiService.setDebugRequestLevel(debug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    public static MyMinterApi getInstance() {
        return INSTANCE;
    }

    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressSerializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());
        out.registerTypeAdapter(EncryptedString.class, new EncryptedStringDeserializer());
        out.registerTypeAdapter(EncryptedString.class, new EncryptedStringSerializer());
        out.registerTypeAdapter(Uri.class, new UriDeserializer());


        return out;
    }

    public AuthRepository auth() {
        if (mAuthRepository == null) {
            mAuthRepository = new AuthRepository(mApiService);
        }

        return mAuthRepository;
    }

    public InfoRepository info() {
        if (mInfoRepository == null) {
            mInfoRepository = new InfoRepository(mApiService);
        }

        return mInfoRepository;
    }

    public AddressRepository address() {
        if (mAddressRepository == null) {
            mAddressRepository = new AddressRepository(mApiService);
        }

        return mAddressRepository;
    }

    public ProfileRepository profile() {
        if(mProfileRepository == null) {
            mProfileRepository = new ProfileRepository(mApiService);
        }

        return mProfileRepository;
    }
}
