package network.minter.mintercore.crypto;

import network.minter.mintercore.models.BytesData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class PublicKey extends BytesData implements java.security.PublicKey {
    public PublicKey(byte[] data) {
        super(data);
    }

    public PublicKey(CharSequence hexData) {
        super(hexData);
    }

    public PublicKey(BytesData data) {
        super(data);
    }

    /**
     * Convert public key to minter address short public key
     *
     * @return last 20 bytes of sha3-hashed original public key
     */
    public MinterAddress toMinter() {
        if (this instanceof MinterAddress) {
            return (MinterAddress) this;
        }

        return new MinterAddress(this);
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
        return new byte[0];
    }
}
