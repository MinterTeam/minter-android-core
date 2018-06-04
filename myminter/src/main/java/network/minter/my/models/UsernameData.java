package network.minter.my.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class UsernameData {
    public String username;
    @SerializedName("is_available")
    public boolean isAvailable;
}
