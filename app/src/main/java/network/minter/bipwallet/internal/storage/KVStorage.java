package network.minter.bipwallet.internal.storage;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.Storage;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class KVStorage implements Storage {
    @Override
    public <T> boolean put(String key, T value) {
        return Hawk.put(key, value);
    }

    @Override
    public <T> T get(String key) {
        return Hawk.get(key);
    }

    public <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    @Override
    public boolean delete(String key) {
        return Hawk.delete(key);
    }

    @Override
    public boolean deleteAll() {
        return Hawk.deleteAll();
    }

    @Override
    public long count() {
        return Hawk.count();
    }

    @Override
    public boolean contains(String key) {
        return Hawk.contains(key);
    }
}
