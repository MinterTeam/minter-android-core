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

package network.minter.core.internal.api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import network.minter.core.crypto.BytesData;
import network.minter.core.internal.helpers.StringHelper;
import timber.log.Timber;

import static network.minter.core.MinterSDK.PREFIX_ADDRESS;
import static network.minter.core.MinterSDK.PREFIX_CHECK;
import static network.minter.core.MinterSDK.PREFIX_PUBLIC_KEY;
import static network.minter.core.MinterSDK.PREFIX_TX;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesDataDeserializer implements JsonDeserializer<BytesData> {
    @Override
    public BytesData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String s;
        try {
            s = json.getAsString();
        } catch (Exception e) {
            Timber.e("Unable to deserialize BytesData: %s", json.toString());
            s = null;
        }

        if (s == null) {
            return null;
        }

        if (s.length() > 2) {
            final String pref = s.substring(0, 2);
            if (pref.equals(PREFIX_ADDRESS) || pref.equals(PREFIX_CHECK) || pref.equals(PREFIX_PUBLIC_KEY) || pref.equals(PREFIX_TX)) {
                s = s.substring(2);
            }
        }

        if (!s.matches(StringHelper.HEX_NUM_PATTERN)) {
            return null;
        }

        return new BytesData(json.getAsString());
    }
}
