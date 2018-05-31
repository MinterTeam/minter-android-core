package network.minter.mintercore.internal.data;

import android.support.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import network.minter.mintercore.crypto.AES256Crypt;
import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.mintercore.util.BytesData;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;
import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class EncryptedString {
    private final static String IV = "Minter seed";
    private String mEncrypted;

    public EncryptedString(@NonNull final String rawString, @NonNull final String encryptionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        checkArgument(rawString != null && rawString.length() > 0, "Nothing to encrypt. Raw string is empty");
        checkArgument(encryptionKey != null && encryptionKey.length() > 0, "Encryption key can't be empty");
        final AES256Crypt crypt = new AES256Crypt();
        mEncrypted = crypt.encryptSimple(rawString, encryptionKey, IV);
    }

    public EncryptedString(@NonNull final String encryptedString) {
        mEncrypted = checkNotNull(encryptedString, "Encrypted data can't be null. It may lead decryption errors");
    }

    public String decrypt(@NonNull final String encryptionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {

        final AES256Crypt crypt = new AES256Crypt();
        return crypt.decryptSimple(mEncrypted, encryptionKey, IV);
    }

    public BytesData decryptBytes(@NonNull final String encryptionKey)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {
        final AES256Crypt crypt = new AES256Crypt();
        return new BytesData(StringHelper.hexStringToBytes(crypt.decryptSimple(mEncrypted, encryptionKey, IV)));
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
