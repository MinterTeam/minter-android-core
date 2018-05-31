package network.minter.mintercore.internal.helpers;

import network.minter.mintercore.MinterApi;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class StringHelper {

    // 20 byte address with or without prefix
    public static final String HEX_ADDRESS_PATTERN = "^((0|M|m)x)?([a-fA-F0-9]{40})$";
    public static final String HEX_NUM_PATTERN = "^(0x)?([a-fA-F0-9]{2,})$";
    private final static char[] hexArray = "0123456789ABCDEF".toLowerCase().toCharArray();

    public static String strlpad(int size, String input) {
        int offset = size - input.length();
        char[] inBytes = input.toCharArray();
        char[] out = new char[size];

        for (int i = 0, s = 0; i < size; i++) {
            if (i < offset) {
                out[i] = '\0';
            } else {
                out[i] = inBytes[s++];
            }
        }

        return new String(out);
    }

    public static String strrpad(int size, String input) {
        int offset = size - input.length();
        char[] inBytes = input.toCharArray();
        char[] out = new char[size];

        for (int i = 0, s = 0; i < size; i++) {
            if (i < input.length()) {
                out[i] = inBytes[i];
            } else {
                out[i] = '\0';
            }
        }

        return new String(out);
    }

    public static String bytesToHexString(final byte[] data) {
        return bytesToHexString(data, false);
    }

    public static String bytesToHexString(final byte[] data, boolean uppercase) {
        if (data == null || data.length == 0) {
            return "";
        }

        int size = data.length;
        char[] hexChars = new char[data.length * 2];
        for (int j = 0; j < size; j++) {
            int v = data[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        if (uppercase) {
            return new String(hexChars).toUpperCase();
        }

        return new String(hexChars);
    }

    public static byte[] hexStringToBytes(final String s) {
        if (s == null || s.length() == 0) {
            return new byte[0];
        }

        String in = s.replace(MinterApi.MINTER_PREFIX, "").replace("0x", "");

        int len = in.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(in.charAt(i), 16) << 4)
                    + Character.digit(in.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToString(String hexString) {
        return bytesToString(hexStringToBytes(hexString));
    }

    public static String bytesToString(byte[] data) {
        return bytesToString(data, data.length);
    }

    public static String bytesToString(byte[] data, int readLength) {
        if(data.length < readLength) {
            throw new ArrayIndexOutOfBoundsException("Read length less than array size: "+String.valueOf(readLength)+" of " + String.valueOf(data.length));
        }
        final char[] out = new char[readLength];
        for (int i = 0; i < readLength; i++) {
            out[i] = (char) data[i];
        }

        return new String(out);
    }
}
