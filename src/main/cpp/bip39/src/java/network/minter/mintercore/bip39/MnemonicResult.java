package network.minter.mintercore.bip39;

import static network.minter.mintercore.bip39.NativeBip39.MR_OK;
import static network.minter.mintercore.bip39.NativeBip39.MR_UNKNOWN_ERROR;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class MnemonicResult {
    private int status = MR_OK;
    private String words;
    private int len;

    public MnemonicResult() {
    }

    public MnemonicResult(String phrase, String language) {
        if (!NativeBip39.validateMnemonic(phrase, language)) {
            throw new IllegalArgumentException("Invalid seed phrase. Unsupported words contains in variable");
        }

        words = phrase;
        final String[] split = words.split("\\s");
        len = split.length + 1;
        status = len != 0 ? MR_OK : MR_UNKNOWN_ERROR;
    }

    public MnemonicResult(String phrase) {
        this(phrase, NativeBip39.LANG_DEFAULT);
    }

    public String getMnemonic() {
        return words;
    }

    public int size() {
        return len;
    }

    public boolean isOk() {
        return status == MR_OK;
    }

    public byte[] toSeed() {
        return NativeBip39.mnemonicToBip39Seed(words);
    }

    public String getStatus() {
        switch (status) {
            case MR_OK:
                return "OK";
            case NativeBip39.MR_UNSUPPORTED_ENTROPY:
                return "Unsupported entropy";
            case NativeBip39.MR_UNKNOWN_ERROR:
            default:
                return "Decode error";
        }
    }
}
