package network.minter.blockchainapi.models;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import network.minter.blockchainapi.MinterBlockChainApi;
import network.minter.mintercore.MinterApi;
import network.minter.mintercore.crypto.MinterAddress;
import retrofit2.Response;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class AccountTest extends BaseApiTest {

    @Test
    public void testGetBalance() throws IOException {
        MinterAddress pk = new MinterAddress("Mx2d483a56027638ec9b5d69568c82aaf6af891456");

        Response<BCResult<Balance>> result = MinterBlockChainApi.getInstance()
                .account()
                .getBalance(pk)
                .execute();

        assertNotNull(result);
        String err = result.isSuccessful() ? null : result.errorBody().string();
        assertNotNull(err, result.body());
        final BCResult<Balance> data = result.body();
        assertEquals(toJson(data), BCResult.ResultCode.Success, data.code);
        assertNotNull(data.result);
        assertTrue(data.result.coins.size() >= 1); // cause we were set exact coin name
        assertEquals(toJson(data.result), MinterApi.DEFAULT_COIN, data.result.get(MinterApi.DEFAULT_COIN).coin);
    }


    public void testGetTransactionsCount() throws IOException {
        MinterAddress pk = new MinterAddress("Mx2d483a56027638ec9b5d69568c82aaf6af891456");

        Response<BCResult<Long>> result = MinterBlockChainApi.getInstance()
                .account()
                .getTransactionCount(pk)
                .execute();

        assertNotNull(result);
        assertNotNull(result.body());
        assertNotNull(result.body().result);
        assertEquals(BCResult.ResultCode.Success, result.body().code);
        assertNotNull(result.body().result);
    }


}
