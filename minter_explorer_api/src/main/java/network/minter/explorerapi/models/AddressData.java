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

package network.minter.explorerapi.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import network.minter.mintercore.MinterSDK;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressData {
    public Map<String, CoinBalance> coins;
    public long txCount;
    // not null only if get list of balances by addresses
    public MinterAddress address;

    public AddressData() {
        coins = new HashMap<>();
    }

    public void fillDefaultsOnEmpty() {
        if (coins == null) {
            coins = new HashMap<>(1);
        }
        if (coins.isEmpty()) {
            coins.put(MinterSDK.DEFAULT_COIN, new CoinBalance(MinterSDK.DEFAULT_COIN, new BigDecimal(0), new BigDecimal(0)));
        }
    }

    public BigDecimal getTotalBalance() {
        if (coins == null || coins.isEmpty()) {
            return new BigDecimal(0);
        }

        // @TODO ?
        BigDecimal totalOut = new BigDecimal(0.0f);
        for (Map.Entry<String, CoinBalance> entry : coins.entrySet()) {
            totalOut = totalOut.add(entry.getValue().amount);
        }

        return totalOut;
    }

    public static class CoinBalance {
        public String coin;
        public BigDecimal amount;
        public BigDecimal usdAmount;

        public CoinBalance() {
        }

        public CoinBalance(String coin, BigDecimal value, BigDecimal valueUsd) {
            this.coin = coin;
            this.amount = value;
            this.usdAmount = valueUsd;
        }

        public String getCoin() {
            return coin;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public BigDecimal getUsdAmount() {
            return usdAmount;
        }
    }
}
