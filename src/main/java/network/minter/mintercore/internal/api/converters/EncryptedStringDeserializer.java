package network.minter.mintercore.internal.api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import network.minter.mintercore.crypto.EncryptedString;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class EncryptedStringDeserializer implements JsonDeserializer<EncryptedString> {
    @Override
    public EncryptedString deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if(json.isJsonNull()) {
            return null;
        }

        return new EncryptedString(json.getAsString());
    }
}
