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

package network.minter.explorerapi.repo;

import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import network.minter.explorerapi.api.AddressEndpoint;
import network.minter.explorerapi.models.AddressData;
import network.minter.explorerapi.models.ExpResult;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressRepository extends DataRepository<AddressEndpoint> {
    private final static String COINS_BALANCE = "balance";
    private final static String COINS_USD_BALANCE = "balanceUsd";

    public AddressRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<ExpResult<AddressData>> getAddressData(MinterAddress address) {
        return getAddressData(address.toString());
    }

    public Call<ExpResult<AddressData>> getAddressData(String address) {
        return getService().balance(address);
    }

    @NonNull
    @Override
    protected Class<AddressEndpoint> getServiceClass() {
        return AddressEndpoint.class;
    }

    @Override
    protected void configureService(ApiService.Builder apiBuilder) {
        super.configureService(apiBuilder);
        apiBuilder.registerTypeAdapter(AddressData.class, new AddressDataDeserializer());
    }

    public static class AddressDataDeserializer implements JsonDeserializer<AddressData> {
        @Override
        public AddressData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            }

            AddressData data = new AddressData();

            JsonObject root = json.getAsJsonObject();

            if (root.has(COINS_BALANCE)) {
                JsonArray coins = root.getAsJsonArray(COINS_BALANCE);
                final Map<String, AddressData.CoinBalance> out = new HashMap<>();
                for (int i = 0; i < coins.size(); i++) {
                    final JsonObject coin = coins.get(i).getAsJsonObject();
                    for (String key : coin.keySet()) {
                        final String uKey = key.toUpperCase();
                        final AddressData.CoinBalance b = new AddressData.CoinBalance();
                        b.balance = new BigDecimal(coin.get(uKey).getAsString());
                        b.coin = uKey;
                        out.put(uKey, b);
                    }
                }

                data.coins = out;
            }

            if (root.has(COINS_USD_BALANCE)) {
                JsonArray coins = root.getAsJsonArray(COINS_USD_BALANCE);
                final Map<String, AddressData.CoinBalance> out = new HashMap<>();
                for (int i = 0; i < coins.size(); i++) {
                    final JsonObject coin = coins.get(i).getAsJsonObject();
                    for (String key : coin.keySet()) {
                        final String uKey = key.toUpperCase();
                        final AddressData.CoinBalance b = new AddressData.CoinBalance();
                        b.balance = coin.get(uKey).getAsBigDecimal();
                        b.coin = uKey;
                        out.put(uKey, b);
                    }
                }

                data.coinsInUsd = out;
            }

            data.bipTotal = root.get("bipTotal").getAsBigDecimal();
            data.usdTotal = root.get("usdTotal").getAsBigDecimal();
            data.txCount = root.get("txCount").getAsLong();

            return data;
        }
    }
}
