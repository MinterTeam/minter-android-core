package network.minter.blockchainapi.models;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class Balance {
    public Map<String, CoinBalance> coins;

    public CoinBalance get(String coin) {
        return coins.get(coin.toUpperCase());
    }

    public BigInteger getFor(String coin) {
        if (!coins.containsKey(coin.toUpperCase())) {
            return new BigInteger("0");
        }

        return coins.get(coin.toUpperCase()).balance;
    }

    public static class CoinBalance {
        public String coin;
        public BigInteger balance;

        /**
         * @return Coin name
         */
        public String getCoin() {
            return coin;
        }

        /**
         * @return Current balance in coins for specified address
         * @throws NumberFormatException
         */
        public BigInteger getBalance() {
            return balance;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coin, balance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoinBalance balance1 = (CoinBalance) o;
            return Objects.equals(coin, balance1.coin) &&
                    Objects.equals(balance, balance1.balance);
        }
    }


}
