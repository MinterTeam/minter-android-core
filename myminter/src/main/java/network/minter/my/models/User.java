/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.my.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Locale;

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

        public Avatar getAvatar() {
            if (avatar == null) {
                avatar = new Avatar();
            }

            avatar.id = this.id;

            return avatar;
        }
    }

    @Parcel
    public static class Avatar {
        public String src;
        public String description;
        long id = -1;

        public Avatar() {
        }

        public String getUrl() {
            if (id < 0) {
                return "https://my.beta.minter.network/api/v1/avatar/by/user/1";
            }

            if (src == null) {
                return String.format("https://my.beta.minter.network/api/v1/avatar/by/user/%d", id);
            }

            return src;
        }
    }
}
