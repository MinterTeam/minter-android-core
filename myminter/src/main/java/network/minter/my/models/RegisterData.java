package network.minter.my.models;

import org.parceler.Parcel;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class RegisterData {
    public String username;
    public String name;
    public String email;
    public String phone;
    public String language;
    public String password;
    public AddressData mainAddress;
}
