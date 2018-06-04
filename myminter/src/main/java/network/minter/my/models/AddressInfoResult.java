package network.minter.my.models;

import org.parceler.Parcel;

import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class AddressInfoResult {
    public MinterAddress address;
    public User.Data user;
}
