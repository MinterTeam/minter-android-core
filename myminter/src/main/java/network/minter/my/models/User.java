package network.minter.my.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Locale;

import network.minter.my.api.ProfileEndpoint;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class User {

    User() {}
    public User(String authToken) {
        token = new Token();
        token.tokenType = "advanced";
        token.expiresIn = ((System.currentTimeMillis() / 1000) + 315569260);
        token.accessToken = authToken;
        token.refreshToken = null;

        data = new Data();
    }
    public Token token;
    @SerializedName("user")
    public Data data;

    public Data getData() {
        if(data == null) {
            data = new Data();
        }

        return data;
    }

    @Parcel
    public static class Token {
        public String tokenType;
        public long expiresIn;
        public String accessToken;
        public String refreshToken;
    }

    @Parcel
    public static class Data {
        public long id;
        public String username;
        public String name;
        public String email;
        public String phone;
        public String language;
        public Avatar avatar;
        public AddressData mainAddress;

        public boolean hasAvatar() {
            return avatar != null;
        }

        public String getLanguage() {
            if(language == null) {
                language = "en_US";
            }
            return language;
        }

        public String getLanguageDisplay() {
            return new Locale(getLanguage()).getDisplayLanguage();
        }
    }

    @Parcel
    public static class Avatar {
        public String src;
        public String description;
    }
}
