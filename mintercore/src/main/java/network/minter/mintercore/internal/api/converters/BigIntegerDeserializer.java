package network.minter.mintercore.internal.api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigInteger;

import network.minter.mintercore.internal.helpers.StringHelper;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BigIntegerDeserializer implements JsonDeserializer<BigInteger> {
    @Override
    public BigInteger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if(json.isJsonNull() || !json.isJsonPrimitive()) {
            return null;
        }

        BigInteger out;
        String val = json.getAsString();
        if(val.matches(StringHelper.HEX_NUM_PATTERN)) {
            if(val.substring(0, 2).equals("0x")) {
                out = new BigInteger(val.substring(2), 16);
            } else {
                out = new BigInteger(val, 16);
            }
        } else {
            out = new BigInteger(val);
        }

        return out;
    }
}
