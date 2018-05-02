package network.minter.bipwallet.repo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import network.minter.mintercore.MinterApi;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.models.Balance;
import network.minter.mintercore.models.DataResult;
import retrofit2.Response;

import static junit.framework.Assert.assertNotNull;
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
        MinterAddress pk = new MinterAddress("Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e");

        Response<DataResult<Balance>> result = MinterApi.getInstance()
                .account()
                .getBalance(pk, MinterApi.DEFAULT_COIN)
                .execute();

        assertNotNull(result);
        assertNotNull(result.body());
        final DataResult<Balance> data = result.body();
        assertEquals(toJson(data), DataResult.ResultCode.Success, data.code);
        assertNotNull(data.result);
        assertEquals(data.result.coins.size(), 1); // cause we were set exact coin name
        assertEquals(toJson(data.result), MinterApi.DEFAULT_COIN, data.result.get(MinterApi.DEFAULT_COIN).coin);
    }


    public void testGetTransactionsCount() throws IOException {
        MinterAddress pk = new MinterAddress("Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e");

        Response<DataResult<Long>> result = MinterApi.getInstance()
                .account()
                .getTransactionCount(pk)
                .execute();

        assertNotNull(result);
        assertNotNull(result.body());
        assertNotNull(result.body().result);
        assertEquals(DataResult.ResultCode.Success, result.body().code);
        assertNotNull(result.body().result);
    }


}
