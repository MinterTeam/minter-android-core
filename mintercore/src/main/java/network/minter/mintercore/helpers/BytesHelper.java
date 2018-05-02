package network.minter.mintercore.helpers;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BytesHelper {
    public static byte[] lpad(int size, byte[] input) {
        if (input.length == size) {
            return input;
        }

        if (input.length > size) {
            final byte[] out = new byte[size];
            System.arraycopy(input, 0, out, 0, size);
            return out;
        }

        int offset = size - input.length;
        byte[] out = new byte[size];

        for (int i = 0, s = 0; i < size; i++) {
            if (i < offset) {
                out[i] = (byte) 0;
            } else {
                out[i] = input[s++];
            }
        }

        return out;
    }
}
