/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.core.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import network.minter.core.internal.helpers.StringHelper;

import static network.minter.core.internal.common.Preconditions.checkArgument;

public class AES256Crypt {


    // cipher to be used for encryption and decryption
    Cipher mCx;
    // encryption key and initialization vector
    byte[] mKey, mIV;

    /**
     * Encryption mode enumeration
     */
    private enum EncryptMode {
        ENCRYPT, DECRYPT
    }

    public AES256Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // initialize the cipher with transformation AES/CBC/PKCS7Padding
        mCx = Cipher.getInstance("AES/CBC/PKCS5Padding");
        mKey = new byte[32]; //256 bit key space
        mIV = new byte[16]; //128 bit IV
    }

    /**
     * Note: This function is no longer used.
     * This function generates md5 hash of the input string
     *
     * @param inputString
     * @return md5 hash of the input string
     */
    public static String md5(final String inputString) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(inputString.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * This function computes the SHA256 hash of input string
     * @param text input text whose SHA256 hash has to be computed
     * @param length length of the text to be returned
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static byte[] SHA256Raw(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        return digest;

    }

    /***
     * This function computes the SHA256 hash of input string
     * @param text input text whose SHA256 hash has to be computed
     * @param length length of the text to be returned
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String SHA256(String text, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        StringBuffer result = new StringBuffer();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        //return result.toString();

        if (length > result.toString().length()) {
            resultStr = result.toString();
        } else {
            resultStr = result.toString().substring(0, length);
        }

        return resultStr;

    }

    /**
     * this function generates random string for given length
     *
     * @param length Desired length
     *               * @return
     */
    public static String generateRandomIV(int length) {
        SecureRandom ranGen = new SecureRandom();
        byte[] aesKey = new byte[16];
        ranGen.nextBytes(aesKey);
        StringBuffer result = new StringBuffer();
        for (byte b : aesKey) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        if (length > result.toString().length()) {
            return result.toString();
        } else {
            return result.toString().substring(0, length);
        }
    }

    /***
     * This function encrypts the plain text to cipher text using the key
     * provided. You'll have to use the same key for decryption
     *
     * @param plainText
     *            Plain text to be encrypted
     * @param _key
     *            Encryption Key. You'll have to use the same key for decryption
     * @param IV
     * 	    initialization Vector
     * @return returns encrypted (cipher) text
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encryptRaw(String plainText, String encryptionKey32B, String IV)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(
                plainText,
                StringHelper.hexStringToBytes(encryptionKey32B),
                EncryptMode.ENCRYPT,
                IV);
    }

    /***
     * This function encrypts the plain text to cipher text using the key
     * provided. You'll have to use the same key for decryption
     *
     * @param plainText
     *            Plain text to be encrypted
     * @param key
     *            Encryption Key. You'll have to use the same key for decryption
     * @param IV
     * 	    initialization Vector
     * @return returns encrypted (cipher) text
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encrypt(String plainText, String key, String IV)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(plainText, key, EncryptMode.ENCRYPT, IV);
    }

    public String encryptSimple(String plainText, String key, String IV)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException {
        return encryptDecrypt(plainText, AES256Crypt.SHA256(key, 32), EncryptMode.ENCRYPT, IV);
    }

    /***
     * This funtion decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryprtion
     *
     * @param _encryptedText
     *            Encrypted/Cipher text to be decrypted
     * @param _key
     *            Encryption key which you used during encryption
     * @param _iv
     * 	    initialization Vector
     * @return encrypted value
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decryptRaw(String encryptedText, String encryptionKey32B, String IV)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(
                encryptedText,
                StringHelper.hexStringToBytes(encryptionKey32B),
                EncryptMode.DECRYPT,
                IV
        );
    }


    /***
     * This funtion decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryprtion
     *
     * @param _encryptedText
     *            Encrypted/Cipher text to be decrypted
     * @param _key
     *            Encryption key which you used during encryption
     * @param _iv
     * 	    initialization Vector
     * @return encrypted value
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decrypt(String _encryptedText, String _key, String _iv)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return encryptDecrypt(_encryptedText, _key, EncryptMode.DECRYPT, _iv);
    }

    public String decryptSimple(String _encryptedText, String _key, String _iv)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException {
        return encryptDecrypt(_encryptedText, AES256Crypt.SHA256(_key, 32), EncryptMode.DECRYPT, _iv);
    }

    /**
     * @param inputText      Text to be encrypted or decrypted
     * @param _encryptionKey Encryption key to used for encryption / decryption
     * @param mode           specify the mode encryption / decryption
     * @param IV             Initialization vector
     * @return encrypted HEX or decrypted raw string based on the mode
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private String encryptDecrypt(String inputText, byte[] encryptionKey32B,
                                  EncryptMode mode, String IV) throws UnsupportedEncodingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        String out = "";

        checkArgument(encryptionKey32B.length == 32);

        int ivlen = IV.getBytes("UTF-8").length;

        if (IV.getBytes("UTF-8").length > mIV.length)
            ivlen = mIV.length;

        System.arraycopy(encryptionKey32B, 0, mKey, 0, 32);
        System.arraycopy(IV.getBytes("UTF-8"), 0, mIV, 0, ivlen);
        //KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
        //_keyGen.init(128);

        SecretKeySpec keySpec = new SecretKeySpec(mKey, "AES"); // Create a new SecretKeySpec
        // for the
        // specified key
        // data and
        // algorithm
        // name.

        IvParameterSpec ivSpec = new IvParameterSpec(mIV); // Create a new
        // IvParameterSpec
        // instance with the
        // bytes from the
        // specified buffer
        // iv used as
        // initialization
        // vector.

        // encryption
        if (mode.equals(EncryptMode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older.
            // Read
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            // for more info.
            mCx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
            byte[] results = mCx.doFinal(inputText.getBytes("UTF-8")); // Finish
            // multi-part
            // transformation
            // (encryption)
//            _out = Base64.encodeToString(results, Base64.DEFAULT); // ciphertext
            out = StringHelper.bytesToHexString(results);
            // output
        }

        // decryption
        if (mode.equals(EncryptMode.DECRYPT)) {
            mCx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this ipher instance

//            byte[] decodedValue = Base64.decode(_inputText.getBytes(),
//                                                Base64.DEFAULT);
            byte[] decodedValue = StringHelper.hexStringToBytes(inputText);
            byte[] decryptedVal = mCx.doFinal(decodedValue); // Finish
            // multi-part
            // transformation
            // (decryption)
            out = new String(decryptedVal, "UTF-8");
        }
        return out; // return encrypted/decrypted string
    }

    /**
     * @param _inputText     Text to be encrypted or decrypted
     * @param _encryptionKey Encryption key to used for encryption / decryption
     * @param _mode          specify the mode encryption / decryption
     * @param _initVector    Initialization vector
     * @return encrypted HEX or decrypted raw string based on the mode
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private String encryptDecrypt(String _inputText, String _encryptionKey,
                                  EncryptMode _mode, String _initVector) throws UnsupportedEncodingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        String _out = "";

        int len = _encryptionKey.getBytes("UTF-8").length; // length of the key	provided

        if (_encryptionKey.getBytes("UTF-8").length > mKey.length)
            len = mKey.length;

        int ivlen = _initVector.getBytes("UTF-8").length;

        if (_initVector.getBytes("UTF-8").length > mIV.length)
            ivlen = mIV.length;

        System.arraycopy(_encryptionKey.getBytes("UTF-8"), 0, mKey, 0, len);
        System.arraycopy(_initVector.getBytes("UTF-8"), 0, mIV, 0, ivlen);
        //KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
        //_keyGen.init(128);

        SecretKeySpec keySpec = new SecretKeySpec(mKey, "AES"); // Create a new SecretKeySpec
        // for the
        // specified key
        // data and
        // algorithm
        // name.

        IvParameterSpec ivSpec = new IvParameterSpec(mIV); // Create a new
        // IvParameterSpec
        // instance with the
        // bytes from the
        // specified buffer
        // iv used as
        // initialization
        // vector.

        // encryption
        if (_mode.equals(EncryptMode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older.
            // Read
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            // for more info.
            mCx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
            byte[] results = mCx.doFinal(_inputText.getBytes("UTF-8")); // Finish
            // multi-part
            // transformation
            // (encryption)
//            _out = Base64.encodeToString(results, Base64.DEFAULT); // ciphertext
            _out = StringHelper.bytesToHexString(results);
            // output
        }

        // decryption
        if (_mode.equals(EncryptMode.DECRYPT)) {
            mCx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this ipher instance

//            byte[] decodedValue = Base64.decode(_inputText.getBytes(),
//                                                Base64.DEFAULT);
            byte[] decodedValue = StringHelper.hexStringToBytes(_inputText);
            byte[] decryptedVal = mCx.doFinal(decodedValue); // Finish
            // multi-part
            // transformation
            // (decryption)
            _out = new String(decryptedVal, "UTF-8");
        }
        return _out; // return encrypted/decrypted string
    }
}

