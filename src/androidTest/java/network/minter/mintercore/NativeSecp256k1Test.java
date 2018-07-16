package network.minter.mintercore;

import android.support.test.runner.AndroidJUnit4;

import com.edwardstock.secp256k1.NativeSecp256k1;
import com.edwardstock.secp256k1.NativeSecp256k1Util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import network.minter.mintercore.internal.helpers.StringHelper;

import static com.edwardstock.secp256k1.NativeSecp256k1.contextCleanup;
import static com.edwardstock.secp256k1.NativeSecp256k1.contextCreate;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class NativeSecp256k1Test {
    static {
        NativeSecp256k1.init();
    }

    @Before
    public void setUp() {
        assert NativeSecp256k1.isEnabled();
    }

    /**
     * This tests public key create() for a invalid secretkey
     */
    @Test
    public void testPubKeyCreateNeg() throws NativeSecp256k1Util.AssertFailException {
        byte[] sec = StringHelper.hexStringToBytes(
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF".toLowerCase());

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.computePubkey(ctx, sec, false);
            String pubkeyString = StringHelper.bytesToHexString(resultArr);
            assertEquals("", pubkeyString);
        } finally {
            contextCleanup(ctx);
        }

    }


    //TODO improve comments/add more tests

    /**
     * This tests sign() for a valid secretkey
     */
    @Test
    public void testSignPos() throws NativeSecp256k1Util.AssertFailException {

        byte[] data = StringHelper.hexStringToBytes(
                "CF80CD8AED482D5D1527D7DC72FCEFF84E6326592848447D2DC0B0E87DFC9A90".toLowerCase()); //sha256hash of "testing"
        byte[] sec = StringHelper.hexStringToBytes(
                "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase());

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.sign(ctx, data, sec);
            String sigString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "30440220182A108E1448DC8F1FB467D06A0F3BB8EA0533584CB954EF8DA112F1D60E39A202201C66F36DA211C087F3AF88B50EDF4F9BDAA6CF5FD6817E74DCA34DB12390C6E9",
                    sigString
            );
        } finally {
            contextCleanup(ctx);
        }
    }

    /**
     * This tests sign() for a invalid secretkey
     */
    @Test
    public void testSignNeg() throws NativeSecp256k1Util.AssertFailException {
        byte[] data = StringHelper.hexStringToBytes(
                "CF80CD8AED482D5D1527D7DC72FCEFF84E6326592848447D2DC0B0E87DFC9A90".toLowerCase()); //sha256hash of "testing"
        byte[] sec = StringHelper.hexStringToBytes(
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF".toLowerCase());

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.sign(ctx, data, sec);
            String sigString = StringHelper.bytesToHexString(resultArr);
            assertEquals("", sigString);
        } finally {
            contextCleanup(ctx);
        }

    }

    /**
     * This tests private key tweak-add
     */
    @Test
    public void testPrivKeyTweakAdd_1() throws NativeSecp256k1Util.AssertFailException {
        byte[] sec = StringHelper.hexStringToBytes(
                "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase());
        byte[] data = StringHelper.hexStringToBytes(
                "3982F19BEF1615BCCFBB05E321C10E1D4CBA3DF0E841C2E41EEB6016347653C3".toLowerCase()); //sha256hash of "tweak"

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.privKeyTweakAdd(ctx, sec, data);
            String sigString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "A168571E189E6F9A7E2D657A4B53AE99B909F7E712D1C23CED28093CD57C88F3",
                    sigString
            );
        } finally {
            contextCleanup(ctx);
        }
    }

    /**
     * This tests private key tweak-mul
     */
    @Test
    public void testPrivKeyTweakMul_1() throws NativeSecp256k1Util.AssertFailException {
        byte[] sec = StringHelper.hexStringToBytes(
                "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase());
        byte[] data = StringHelper.hexStringToBytes(
                "3982F19BEF1615BCCFBB05E321C10E1D4CBA3DF0E841C2E41EEB6016347653C3".toLowerCase()); //sha256hash of "tweak"

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.privKeyTweakMul(ctx, sec, data);
            String sigString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "97F8184235F101550F3C71C927507651BD3F1CDB4A5A33B8986ACF0DEE20FFFC",
                    sigString
            );
        } finally {
            contextCleanup(ctx);
        }
    }

    /**
     * This tests private key tweak-add uncompressed
     */
    @Test
    public void testPrivKeyTweakAdd_2() throws NativeSecp256k1Util.AssertFailException {
        byte[] pub = StringHelper.hexStringToBytes(
                "040A629506E1B65CD9D2E0BA9C75DF9C4FED0DB16DC9625ED14397F0AFC836FAE595DC53F8B0EFE61E703075BD9B143BAC75EC0E19F82A2208CAEB32BE53414C40".toLowerCase());
        byte[] data = StringHelper.hexStringToBytes(
                "3982F19BEF1615BCCFBB05E321C10E1D4CBA3DF0E841C2E41EEB6016347653C3".toLowerCase()); //sha256hash of "tweak"

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.pubKeyTweakAdd(ctx, pub, data);
            String sigString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "0411C6790F4B663CCE607BAAE08C43557EDC1A4D11D88DFCB3D841D0C6A941AF525A268E2A863C148555C48FB5FBA368E88718A46E205FABC3DBA2CCFFAB0796EF",
                    sigString
            );
        } finally {
            contextCleanup(ctx);
        }
    }

    /**
     * This tests private key tweak-mul uncompressed
     */
    @Test
    public void testPrivKeyTweakMul_2() throws NativeSecp256k1Util.AssertFailException {
        byte[] pub = StringHelper.hexStringToBytes(
                "040A629506E1B65CD9D2E0BA9C75DF9C4FED0DB16DC9625ED14397F0AFC836FAE595DC53F8B0EFE61E703075BD9B143BAC75EC0E19F82A2208CAEB32BE53414C40".toLowerCase());
        byte[] data = StringHelper.hexStringToBytes(
                "3982F19BEF1615BCCFBB05E321C10E1D4CBA3DF0E841C2E41EEB6016347653C3".toLowerCase()); //sha256hash of "tweak"

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.pubKeyTweakMul(ctx, pub, data);
            String sigString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "04E0FE6FE55EBCA626B98A807F6CAF654139E14E5E3698F01A9A658E21DC1D2791EC060D4F412A794D5370F672BC94B722640B5F76914151CFCA6E712CA48CC589",
                    sigString
            );
        } finally {
            contextCleanup(ctx);
        }

    }

    /**
     * This tests seed randomization
     */
    @Test
    public void testRandomize() throws NativeSecp256k1Util.AssertFailException {
        byte[] seed = StringHelper.hexStringToBytes(
                "A441B15FE9A3CF56661190A0B93B9DEC7D04127288CC87250967CF3B52894D11".toLowerCase()); //sha256hash of "random"

        long ctx = contextCreate();
        try {
            boolean result = NativeSecp256k1.randomize(ctx, seed);
            assertTrue(result);
        } finally {
            contextCleanup(ctx);
        }
    }

    @Test
    public void testCreateECDHSecret() throws NativeSecp256k1Util.AssertFailException {
        byte[] sec = StringHelper.hexStringToBytes(
                "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase());
        byte[] pub = StringHelper.hexStringToBytes(
                "040A629506E1B65CD9D2E0BA9C75DF9C4FED0DB16DC9625ED14397F0AFC836FAE595DC53F8B0EFE61E703075BD9B143BAC75EC0E19F82A2208CAEB32BE53414C40".toLowerCase());

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.createECDHSecret(ctx, sec, pub);
            String ecdhString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "2A2A67007A926E6594AF3EB564FC74005B37A9C8AEF2033C4552051B5C87F043",
                    ecdhString
            );
        } finally {
            contextCleanup(ctx);
        }
    }

    /**
     * This tests public key create() for a valid secretkey
     */
    @Test
    public void testPubKeyCreatePos() throws NativeSecp256k1Util.AssertFailException {
        byte[] sec = StringHelper.hexStringToBytes(
                "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase());

        long ctx = contextCreate();
        try {
            byte[] resultArr = NativeSecp256k1.computePubkey(ctx, sec, false);
            String pubkeyString = StringHelper.bytesToHexString(resultArr, true);
            assertEquals(
                    "04C591A8FF19AC9C4E4E5793673B83123437E975285E7B442F4EE2654DFFCA5E2D2103ED494718C697AC9AEBCFD19612E224DB46661011863ED2FC54E71861E2A6",
                    pubkeyString);
        } finally {
            contextCleanup(ctx);
        }
    }

    /**
     * This tests verify() for a valid signature
     */
    @Test
    public void testVerifyPos() throws NativeSecp256k1Util.AssertFailException {
        boolean result;
        long ctx = contextCreate();
        byte[] data = StringHelper.hexStringToBytes(
                "CF80CD8AED482D5D1527D7DC72FCEFF84E6326592848447D2DC0B0E87DFC9A90".toLowerCase()); //sha256hash of "testing"
        byte[] sig = StringHelper.hexStringToBytes(
                "3044022079BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F817980220294F14E883B3F525B5367756C2A11EF6CF84B730B36C17CB0C56F0AAB2C98589".toLowerCase());
        byte[] pub = StringHelper.hexStringToBytes(
                "040A629506E1B65CD9D2E0BA9C75DF9C4FED0DB16DC9625ED14397F0AFC836FAE595DC53F8B0EFE61E703075BD9B143BAC75EC0E19F82A2208CAEB32BE53414C40".toLowerCase());

        try {
            result = NativeSecp256k1.verify(ctx, data, sig, pub);
        } finally {
            contextCleanup(ctx);
        }

        assertTrue(result);
    }

    /**
     * This tests verify() for a non-valid signature
     */
    @Test
    public void testVerifyNeg() throws NativeSecp256k1Util.AssertFailException {
        boolean result;
        byte[] data = StringHelper.hexStringToBytes(
                "CF80CD8AED482D5D1527D7DC72FCEFF84E6326592848447D2DC0B0E87DFC9A91".toLowerCase()); //sha256hash of "testing"
        byte[] sig = StringHelper.hexStringToBytes(
                "3044022079BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F817980220294F14E883B3F525B5367756C2A11EF6CF84B730B36C17CB0C56F0AAB2C98589".toLowerCase());
        byte[] pub = StringHelper.hexStringToBytes(
                "040A629506E1B65CD9D2E0BA9C75DF9C4FED0DB16DC9625ED14397F0AFC836FAE595DC53F8B0EFE61E703075BD9B143BAC75EC0E19F82A2208CAEB32BE53414C40".toLowerCase());

        long ctx = contextCreate();
        try {
            result = NativeSecp256k1.verify(ctx, data, sig, pub);
        } finally {
            contextCleanup(ctx);
        }

        //System.out.println(" TEST " + new BigInteger(1, resultbytes).toString(16));
        assertFalse(result);
    }

    /**
     * This tests secret key verify() for a valid secretkey
     */
    @Test
    public void testSecKeyVerifyPos() throws NativeSecp256k1Util.AssertFailException {
        boolean result;
        byte[] sec = StringHelper.hexStringToBytes(
                "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase());

        long ctx = contextCreate();
        try {
            result = NativeSecp256k1.secKeyVerify(ctx, sec);
        } finally {
            contextCleanup(ctx);
        }

        //System.out.println(" TEST " + new BigInteger(1, resultbytes).toString(16));
        assertTrue(result);
    }

    /**
     * This tests secret key verify() for an invalid secretkey
     */
    @Test
    public void testSecKeyVerifyNeg() throws NativeSecp256k1Util.AssertFailException {
        boolean result;
        byte[] sec = StringHelper.hexStringToBytes(
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF".toLowerCase());
        long ctx = contextCreate();
        try {
            result = NativeSecp256k1.secKeyVerify(ctx, sec);
        } finally {
            contextCleanup(ctx);
        }
        //System.out.println(" TEST " + new BigInteger(1, resultbytes).toString(16));
        assertFalse(result);
    }
}
