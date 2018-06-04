package network.minter.bipwallet.advanced.repo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.bipwallet.internal.storage.KVStorage;
import network.minter.mintercore.bip39.HDKey;
import network.minter.mintercore.bip39.MnemonicResult;
import network.minter.mintercore.bip39.NativeHDKeyEncoder;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.crypto.PublicKey;

import static network.minter.bipwallet.internal.common.Preconditions.checkArgument;
import static network.minter.bipwallet.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SecretLocalRepository {

    private final static String KEY_SECRETS = "mnemonic_secret_list";
    private final static String KEY_ADDRESSES = "addresses_list";

    private final KVStorage mStorage;

    public SecretLocalRepository(KVStorage storage) {
        mStorage = storage;
    }

    public MinterAddress add(@NonNull MnemonicResult mnemonicResult) {
        if (!mnemonicResult.isOk()) {
            throw new IllegalArgumentException("Mnemonic result is not in valid state");
        }

        return add(mnemonicResult.toSeed());
    }

    public MinterAddress add(@NonNull final BytesData seed) {
        checkNotNull(seed);
        checkArgument(seed.size() > 0, "Seed can't be empty");

        Map<String, SecretData> secrets = getSecrets();
        List<MinterAddress> addresses = getAddresses();

        final HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed.getData());
        final HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey);
        final PrivateKey privateKey = extKey.getPrivateKey();
        final PublicKey publicKey = extKey.getPublicKey();
        final MinterAddress address = publicKey.toMinter();

        final SecretData data = new SecretData(seed, privateKey, publicKey);
        secrets.put(address.toString(), data);
        mStorage.put(KEY_SECRETS, secrets);

        if (!addresses.contains(address)) {
            addresses.add(address);
            mStorage.put(KEY_ADDRESSES, addresses);
        }

        rootKey.clear();
        extKey.clear();
        privateKey.cleanup();

        return address;
    }

    public MinterAddress add(@NonNull final byte[] seedBytes) {
        return add(new BytesData(seedBytes));
    }

    public void remove(MinterAddress address) {
        Map<MinterAddress, SecretData> secrets = mStorage.get(KEY_SECRETS);
        if (secrets == null || secrets.isEmpty()) {
            return;
        }

        secrets.remove(address);
    }

    public Map<String, SecretData> getSecrets() {
        Map<String, SecretData> secrets = mStorage.get(KEY_SECRETS);
        if (secrets == null) {
            secrets = new HashMap<>();
        }

        return secrets;
    }

    public List<MinterAddress> getAddresses() {
        List<MinterAddress> addresses = mStorage.get(KEY_ADDRESSES);
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        return addresses;
    }

    public void destroy() {
        mStorage.deleteAll();
    }

    public final static class SecretData implements Serializable {
        private BytesData mSeed;
        private PrivateKey mPrivateKey;
        private PublicKey mPublicKey;
        private MinterAddress mMinterAddress;

        SecretData(BytesData seed, PrivateKey privateKey, PublicKey publicKey) {
            mSeed = seed;
            mPrivateKey = privateKey;
            mPublicKey = publicKey;
            mMinterAddress = publicKey.toMinter();
        }

        public BytesData getSeed() {
            return mSeed;
        }

        public PrivateKey getPrivateKey() {
            return mPrivateKey;
        }

        public PublicKey getPublicKey() {
            return mPublicKey;
        }

        public MinterAddress getMinterAddress() {
            return mMinterAddress;
        }
    }
}
