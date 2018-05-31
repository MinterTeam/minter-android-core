package network.minter.my.models;

/**
 * MyMinter SDK. May 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class LoginData {
    public String username;
    public String password;

    public LoginData() {}

    private LoginData(Builder builder) {
        username = builder.username;
        password = builder.password;
    }

    public static Builder newBuilder() {
        return new Builder();
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
