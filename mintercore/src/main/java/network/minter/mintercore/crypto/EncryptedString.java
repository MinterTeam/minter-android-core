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

package network.minter.mintercore.crypto;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;
import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class EncryptedString implements Serializable {
    private final static String IV = "Minter seed";
    String mEncrypted;

    public EncryptedString(@NonNull final String rawString, @NonNull final String encryptionKey, @NonNull final String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        checkArgument(rawString != null && rawString.length() > 0, "Nothing to encrypt. Raw string is empty");
        checkArgument(encryptionKey != null && encryptionKey.length() > 0, "Encryption key can't be empty");
        checkArgument(encryptionKey.length() >= 32);

        final AES256Crypt crypt = new AES256Crypt();
        mEncrypted = crypt.encrypt(rawString, encryptionKey.substring(0, 32), iv);
    }

    public EncryptedString(@NonNull final String rawString, @NonNull final String encryptionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        this(rawString, encryptionKey, IV);
    }

    public EncryptedString(@NonNull final String encryptedString) {
        mEncrypted = checkNotNull(encryptedString, "Encrypted data can't be null. It may lead decryption errors");
    }

    //parcel
    EncryptedString() {
    }

    public String decrypt(@NonNull final String encryptionKey, final String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {

        checkArgument(encryptionKey.length() >= 32);

        final AES256Crypt crypt = new AES256Crypt();
        return crypt.decrypt(mEncrypted, encryptionKey.substring(0, 32), iv);
    }

    public String decrypt(@NonNull final String encryptionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        return decrypt(encryptionKey, IV);
    }


    @NonNull
    public String getEncrypted() {
        return mEncrypted;
    }

    @Override
    public String toString() {
        return getEncrypted();
    }
}
