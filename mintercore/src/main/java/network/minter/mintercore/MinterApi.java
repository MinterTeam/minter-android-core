package network.minter.mintercore;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import network.minter.mintercore.bip39.NativeBip39;
import network.minter.mintercore.crypto.NativeSecp256k1;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterApi {
    public final static String DEFAULT_COIN = "MNT";
    public final static String MINTER_PREFIX = "Mx";
    private static MinterApi INSTANCE;

    private MinterApi() {
    }

    public static void initialize() {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterApi();

        NativeSecp256k1.init();
        NativeBip39.init();

        if (!NativeSecp256k1.isEnabled()) {
            throw new RuntimeException("Unable to load secp256k1 library");
        }

        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }


    public static MinterApi getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Have you forget to call MinterApi.initialize(Context ctx)?");
        }
        return INSTANCE;
    }


}
