package network.minter.my.models;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.data.EncryptedString;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressData {
    public long id;
    public MinterAddress address;
    public boolean isMain;
    public boolean isServerSecured;
    public EncryptedString encrypted;

}
