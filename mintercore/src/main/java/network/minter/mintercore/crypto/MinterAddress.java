package network.minter.mintercore.crypto;

import org.parceler.Parcel;

import network.minter.mintercore.MinterApi;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class MinterAddress extends PublicKey {
    MinterAddress() {
    }

    public MinterAddress(byte[] data) {
        super(
                checkArgument(data.length == 20, data, "Minter public key must contains exact 20 bytes")
        );
    }

    public MinterAddress(CharSequence hexData) {
        super(
                checkArgument(
                        hexData != null && (hexData.length() == 40 || hexData.length() == 42),
                        hexData,
                        "Minter public key in hex format must contains 40 or 42 characters, where first 2 chars is a prefix like: Mx or 0x"
                )
        );
    }

    public MinterAddress(MinterAddress data) {
        super(data.getData());
    }

    /**
     * Creates minter public key from raw public key
     *
     * @param data Raw public key extracted from private key
     */
    public MinterAddress(PublicKey data) {
        // don't change source public key
        super(
                new BytesData(data.dropFirst())
                        .sha3Mutable()
                        .takeLastMutable(20)
        );
    }

    /**
     * Convert public key to minter address short public key and converts to hex string with prefix
     *
     * @return last 20 bytes with minter prefix of sha3-hashed original public key
     */
    public String toString() {
        return toHexString(MinterApi.MINTER_PREFIX, false);
    }

    public boolean equals(String other) {
        return other.equals(toString());
    }
}
