package network.minter.blockchainapi.models;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import network.minter.blockchainapi.MinterBlockChainApi;
import network.minter.blockchainapi.repo.TransactionRepository;
import network.minter.mintercore.MinterApi;
import network.minter.blockchainapi.models.BCResult;
import network.minter.blockchainapi.models.HistoryTransaction;
import network.minter.blockchainapi.models.operational.OperationType;
import network.minter.blockchainapi.models.operational.TxSendCoin;
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
        Response<BCResult<List<HistoryTransaction>>> result = MinterBlockChainApi.getInstance().transactions().getTransactions(
                new TransactionRepository.TQuery().setTo(to)
        ).execute();

        assertNotNull(result);
        assertTrue(result.isSuccessful());

        BCResult<List<HistoryTransaction>> data = result.body();
        assertNotNull(data);
        assertEquals(BCResult.ResultCode.Success, data.code);

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
