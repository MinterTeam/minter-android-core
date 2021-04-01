/*
 * Copyright (C) by MinterTeam. 2018
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
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

package network.minter.core.crypto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import network.minter.core.internal.helpers.StringHelper;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class EncryptedString implements Serializable {
    private final static String IV = "Minter seed";
    String mEncrypted;

    /**
     * @param rawString
     * @param encryptionKey32B 32 bytes hex string, can be just SHA256 hashed
     * @param iv
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public EncryptedString(@Nonnull final String rawString, @Nonnull final String encryptionKey32B, @Nonnull final String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        checkArgument(rawString != null && rawString.length() > 0, "Nothing to encrypt. Raw string is empty");
        checkArgument(
                encryptionKey32B != null
                        && encryptionKey32B.length() == 64
                        && StringHelper.testHex(encryptionKey32B),
                String.format(Locale.getDefault(), "Encryption key must have 32 bytes (64 in hex representation), given hex length: %d", encryptionKey32B.length())
        );

        final AES256Crypt crypt = new AES256Crypt();
        mEncrypted = crypt.encryptRaw(rawString, encryptionKey32B, iv);
    }

    public EncryptedString(@Nonnull final String rawString, @Nonnull final String encryptionKey32B)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        this(rawString, encryptionKey32B, IV);
    }

    public EncryptedString(@Nonnull final String encryptedString) {
        mEncrypted = checkNotNull(encryptedString, "Encrypted data can't be null. It may lead decryption errors");
    }

    /**
     * @param encryptionKey32B 32 bytes hex string, can be just SHA256 hashed
     * @param iv
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public String decrypt(@Nonnull final String encryptionKey32B, final String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {

        checkArgument(
                encryptionKey32B != null
                        && encryptionKey32B.length() == 64
                        && StringHelper.testHex(encryptionKey32B),
                String.format(Locale.getDefault(), "Encryption key must have 32 bytes (64 in hex representation), given hex length: %d", encryptionKey32B.length())
        );

        final AES256Crypt crypt = new AES256Crypt();
        return crypt.decryptRaw(mEncrypted, encryptionKey32B, iv);
    }

    public String decrypt(@Nonnull final String encryptionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        return decrypt(encryptionKey, IV);
    }

    @Nonnull
    public String getEncrypted() {
        return mEncrypted;
    }

    @Override
    public String toString() {
        return getEncrypted();
    }
}
