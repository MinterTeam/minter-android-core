package network.minter.my.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ProfileRequestResult {
    public List<Confirmation> confirmations;

    public static class Confirmation {
        public Uri endpoint;
        public ConfirmType type;
    }

    public enum ConfirmType {
        @SerializedName("phone")
        Phone,
        @SerializedName("email")
        Email,
    }
}
