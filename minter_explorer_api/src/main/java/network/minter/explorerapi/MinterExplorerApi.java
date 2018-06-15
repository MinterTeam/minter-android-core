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

package network.minter.explorerapi;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.explorerapi.repo.AddressRepository;
import network.minter.explorerapi.repo.TransactionRepository;
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
public class MinterExplorerApi {
    public static final String FRONT_URL = "https://explorer.beta.minter.network";
    private final static String BASE_API_URL = "https://testnet.explorer.minter.network";
    private static MinterExplorerApi INSTANCE;
    private ApiService.Builder mApiService;
    private TransactionRepository mTransactionRepository;
    private AddressRepository mAddressRepository;

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

    public AddressRepository address() {
        if (mAddressRepository == null) {
            mAddressRepository = new AddressRepository(mApiService);
        }

        return mAddressRepository;
    }


    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

        return out;
    }
}
