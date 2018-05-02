package network.minter.bipwallet.models;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;

import network.minter.mintercore.MinterApi;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.models.operational.OperationType;
import network.minter.mintercore.models.operational.SendTx;
import network.minter.mintercore.models.operational.Transaction;
import network.minter.mintercore.models.operational.TransactionSign;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class TransactionTest {

    @Test
    public void testSignPredefinedTransaction() {
        final String validSign = "Mxf86d0b0101a6e58a4d4e540000000000000094c3a55cdb5bcb97fd5657794247de4ed5e4a49f0d8405f5e1001ca014d7cc6217325e58a3884d7abe9e99d2d03f86f8d0019328e69cbdad8dda8272a07f655e0cf5845cfe142f637cad3378900eb66cceb6d04addaec1ab1c3f16d44e";

        final PrivateKey privateKey = new PrivateKey(
                "418e4be028dcaed85aa58b643979f644f806a42bb6d1912848720788a53bb8a4");
        final MinterAddress address = new MinterAddress("Mxc3a55cdb5bcb97fd5657794247de4ed5e4a49f0d");

        Transaction<SendTx> tx = Transaction
                .newSendTransaction(new BigInteger("11"), new BigInteger("1"))
                .setCoin("MNT")
                .setTo(address)
                .setValue(1)
                .build();


        TransactionSign sign = tx.sign(privateKey);
        assertNotNull(sign);
        assertEquals(validSign.length(), sign.getTxSign().length());
        assertEquals(validSign, sign.getTxSign());
    }


    @Test
    public void testDecodeSendTransaction() {
        MinterAddress fromAddress = new MinterAddress("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99");
        MinterAddress toAddress = new MinterAddress("Mxc3a55cdb5bcb97fd5657794247de4ed5e4a49f0d");
        final String encodedTransaction = "+G0LAQGm5YpNTlQAAAAAAAAAlMOlXNtby5f9Vld5QkfeTtXkpJ8NhAX14QAcoBTXzGIXMl5Yo4hNer6emdLQP4b40AGTKOacva2N2oJyoH9lXgz1hFz+FC9jfK0zeJAOtmzOttBK3a7Bqxw/FtRO";
        BigInteger nonce = new BigInteger("11");
        BigInteger gasPrice = new BigInteger("1");
        OperationType type = OperationType.SendCoin;
        long valueHuman = 1L;
        BigInteger value = new BigInteger("1").multiply(new BigInteger(Transaction.VALUE_MUL.toString()));
        String coin = MinterApi.DEFAULT_COIN;

        Transaction<SendTx> tx = Transaction.fromEncoded(encodedTransaction, SendTx.class);

        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());
        assertEquals(gasPrice, tx.getGasPrice());
        assertEquals(type, tx.getType());

        assertNotNull(tx.getData());
        assertTrue(tx.getData() instanceof SendTx);
        assertEquals(valueHuman, tx.getData().getValue());
        assertEquals(value, tx.getData().getValueBigInteger());
        assertEquals(coin, tx.getData().getCoin());
        assertEquals(toAddress, tx.getData().getTo());
    }
}
