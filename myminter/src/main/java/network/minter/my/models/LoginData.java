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

import org.parceler.Parcel;

import network.minter.mintercore.crypto.HashUtil;

/**
 * MyMinter SDK. May 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class LoginData {
    public String username;
    public String password;
    public transient String rawPassword;

    public LoginData() {}

    private LoginData(Builder builder) {
        username = builder.username;
        password = builder.password;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public LoginData preparePassword() {
        password = HashUtil.sha256HexDouble(rawPassword);
        return this;
    }

    /**
     * {@code LoginData} builder static inner class.
     */
    public static final class Builder {
        private String username;
        private String password;

        private Builder() {
        }

        /**
         * Sets the {@code username} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param username the {@code username} to set
         * @return a reference to this Builder
         */
        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets the {@code password} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param password the {@code password} to set
         * @return a reference to this Builder
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Returns a {@code LoginData} built from the parameters previously set.
         *
         * @return a {@code LoginData} built with parameters of this {@code LoginData.Builder}
         */
        public LoginData build() {
            return new LoginData(this);
        }
    }
}
