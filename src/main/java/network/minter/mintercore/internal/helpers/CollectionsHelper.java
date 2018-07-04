package network.minter.mintercore.internal.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class CollectionsHelper {

    public static <T> Map<T, T> asMap(T... valuesCountEquals2) {
        if (valuesCountEquals2.length == 0 || (valuesCountEquals2.length % 2) != 0) {
            throw new IllegalArgumentException("Values count must be equals 2 (count % 2 == 0)");
        }

        final HashMap<T, T> out = new HashMap<>();
        for (int i = 0; i < valuesCountEquals2.length; i += 2) {
            final T key = valuesCountEquals2[i];
            final T value = valuesCountEquals2[i + 1];
            out.put(key, value);
        }

        return out;
    }
}
