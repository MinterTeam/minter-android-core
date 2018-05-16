package network.minter.bipwallet.repo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import network.minter.mintercore.MinterApi;
import network.minter.mintercore.models.DataResult;
import network.minter.mintercore.models.HistoryTransaction;
import network.minter.mintercore.models.operational.OperationType;
import network.minter.mintercore.models.operational.TxSendCoin;
import network.minter.mintercore.repo.TransactionRepository;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class HistoryTransactionTest {
    /**
     *
     */
    @Test
    public void testGetTransactions() throws IOException {
        final String to = "Mx2d483a56027638ec9b5d69568c82aaf6af891456";
        Response<DataResult<List<HistoryTransaction>>> result = MinterApi.getInstance().transactions().getTransactions(
                new TransactionRepository.TQuery().setTo(to)
        ).execute();

        assertNotNull(result);
        assertTrue(result.isSuccessful());

        DataResult<List<HistoryTransaction>> data = result.body();
        assertNotNull(data);
        assertEquals(DataResult.ResultCode.Success, data.code);

        int foundWithTo = 0;
        for (HistoryTransaction t : data.result) {
            assertNotNull(t.data);
            if (t.type == OperationType.SendCoin) {
                if (t.<TxSendCoin>getData().getTo().equals(to)) {
                    foundWithTo++;
                }

            }

        }

        assertTrue(foundWithTo > 0);


    }

}
