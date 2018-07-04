package network.minter.mintercore.internal.api.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import network.minter.mintercore.crypto.EncryptedString;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class EncryptedStringSerializer implements JsonSerializer<EncryptedString> {
    @Override
    public JsonElement serialize(EncryptedString src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getEncrypted());
    }
}
