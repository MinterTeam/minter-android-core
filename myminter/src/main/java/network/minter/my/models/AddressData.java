package network.minter.my.models;

import org.parceler.Parcel;

import network.minter.mintercore.crypto.EncryptedString;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class AddressData {
    public long id;
    public MinterAddress address;
    public boolean isMain;
    public boolean isServerSecured;
    public EncryptedString encrypted;

}
