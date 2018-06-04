package network.minter.mintercore.internal.api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import network.minter.mintercore.crypto.BytesData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesDataDeserializer implements JsonDeserializer<BytesData> {
    @Override
    public BytesData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String s = json.getAsString();
        if(s == null || !s.matches("[a-fA-F0-9]+")) {
            return null;
        }

        return new BytesData(json.getAsString());
    }
}
