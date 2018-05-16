package network.minter.mintercore.crypto;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import network.minter.mintercore.models.BytesData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class PrivateKey extends BytesData implements java.security.PrivateKey {
    public PrivateKey(String pk) {
        super(pk);
    }

    public PrivateKey(BytesData data) {
        super(data);
    }

    public PrivateKey(byte[] data) {
        super(data);
    }

    public PrivateKey(CharSequence hexData) {
        super(hexData);
    }

    /* @TODO
    public static PrivateKey fromMnemonic(String mnemonic) {
        return null;
    }
    */

    /**
     * Load private key from file
     *
     * @param file
     * @return PrivateKey data
     * @throws IOException if file not found, can't open stream or something else
     */
    public static PrivateKey fromFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File %s does not exists", file.getAbsoluteFile()));
        }
        if (!file.canRead()) {
            throw new IllegalStateException(String.format("File %s is not readable", file.getAbsoluteFile()));
        }

        byte[] data = new byte[(int) file.length()];
        DataInputStream is = new DataInputStream(new FileInputStream(file));
        is.readFully(data);


        return new PrivateKey(data);
    }

    /**
     * Check private key for validity
     *
     * @return true if private key is valid
     */
    public boolean verify() {
        if (!isValid()) {
            throw new IllegalStateException("Can't verify, private key already disposed");
        }

        long ctx = NativeSecp256k1.contextCreate();
        boolean res;
        try {
            res = NativeSecp256k1.secKeyVerify(ctx, getData());
        } catch (Throwable t) {
            res = false;
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }
        return res;
    }

    public PublicKey getPublicKey() {
        return getPublicKey(false);
    }

    /**
     * Extract public key from private
     *
     * @return Public Key data
     */
    public PublicKey getPublicKey(boolean compressed) {
        if (!isValid()) {
            throw new IllegalStateException("Can't get public key, private key already disposed");
        }

        long ctx = NativeSecp256k1.contextCreate();
        PublicKey out;
        try {
            out = new PublicKey(NativeSecp256k1.computePubkey(ctx, getData(), compressed));
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        return out;
    }

    @Override
    public String getAlgorithm() {
        return "EC";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return getDataImmutable();
    }

    @Override
    public void destroy() {
        cleanup();
    }

    @Override
    public boolean isDestroyed() {
        return !isValid();
    }
}
