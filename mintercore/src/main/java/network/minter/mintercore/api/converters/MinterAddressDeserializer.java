package network.minter.mintercore.api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.helpers.StringHelper;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterAddressDeserializer implements JsonDeserializer<MinterAddress> {
    @Override
    public MinterAddress deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (json.isJsonNull()) {
            return null;
        }

        String val = json.getAsString();

        if (!val.matches(StringHelper.HEX_ADDRESS_PATTERN)) {
            return null;
        }

        return new MinterAddress(val);

    }
}
