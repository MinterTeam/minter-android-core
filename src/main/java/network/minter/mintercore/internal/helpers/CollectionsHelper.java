/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.mintercore.internal.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * minter-android-core. 2018
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
