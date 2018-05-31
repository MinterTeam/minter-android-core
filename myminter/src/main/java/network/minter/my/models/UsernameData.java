package network.minter.my.models;

import com.google.gson.annotations.SerializedName;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class UsernameData {
    public String username;
    @SerializedName("is_available")
    public boolean isAvailable;
}
