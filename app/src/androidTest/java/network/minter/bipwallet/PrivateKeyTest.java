package network.minter.bipwallet;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.crypto.PublicKey;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class PrivateKeyTest {

    @Test
    public void testVerifyPos() {
        PrivateKey pk = new PrivateKey("098e910195e8dc28075c000fa8a5dd811adf705e72f3ac62996049712da1801e");
        assertTrue(pk.verify());
    }

    @Test
    public void testExtractPublicKey() {
        final PublicKey publicKey = new PublicKey("Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e");
        final PrivateKey privateKey = new PrivateKey(
                "098e910195e8dc28075c000fa8a5dd811adf705e72f3ac62996049712da1801e");
        final MinterAddress extracted = privateKey.getPublicKey().toMinter();

        assertEquals(publicKey, extracted);
        assertTrue(Arrays.equals(publicKey.getData(), extracted.getData()));
        assertEquals("Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e", extracted.toString());
    }

}
