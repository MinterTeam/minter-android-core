/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.my;

import android.net.Uri;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.mintercore.BuildConfig;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.EncryptedString;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.api.converters.BigIntegerDeserializer;
import network.minter.mintercore.internal.api.converters.BytesDataDeserializer;
import network.minter.mintercore.internal.api.converters.EncryptedStringDeserializer;
import network.minter.mintercore.internal.api.converters.EncryptedStringSerializer;
import network.minter.mintercore.internal.api.converters.MinterAddressDeserializer;
import network.minter.mintercore.internal.api.converters.MinterAddressSerializer;
import network.minter.mintercore.internal.api.converters.UriDeserializer;
import network.minter.my.repo.MyAddressRepository;
import network.minter.my.repo.MyAuthRepository;
import network.minter.my.repo.MyInfoRepository;
import network.minter.my.repo.MyProfileRepository;
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
    private MyAuthRepository mAuthRepository;
    private MyInfoRepository mInfoRepository;
    private MyAddressRepository mAddressRepository;
    private MyProfileRepository mProfileRepository;

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

    public MyAuthRepository auth() {
        if (mAuthRepository == null) {
            mAuthRepository = new MyAuthRepository(mApiService);
        }

        return mAuthRepository;
    }

    public MyInfoRepository info() {
        if (mInfoRepository == null) {
            mInfoRepository = new MyInfoRepository(mApiService);
        }

        return mInfoRepository;
    }

    public MyAddressRepository address() {
        if (mAddressRepository == null) {
            mAddressRepository = new MyAddressRepository(mApiService);
        }

        return mAddressRepository;
    }

    public MyProfileRepository profile() {
        if(mProfileRepository == null) {
            mProfileRepository = new MyProfileRepository(mApiService);
        }

        return mProfileRepository;
    }
}
