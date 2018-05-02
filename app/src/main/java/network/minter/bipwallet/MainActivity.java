package network.minter.bipwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import network.minter.mintercore.MinterApi;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.crypto.PublicKey;

public class MainActivity extends AppCompatActivity {

    private final static String PRIV = "418e4be028dcaed85aa58b643979f644f806a42bb6d1912848720788a53bb8a4";
    private final static String PRIV_MY = "098e910195e8dc28075c000fa8a5dd811adf705e72f3ac62996049712da1801e";
    private final static String ADDR = "Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99";
    private final static String ADDR_MY = "Mxa257e2ec0def0d3a23f0a7e5d61a254c96a1ce4e";
    private final static String VALID_TX = "+G0LAQGm5YpNTlQAAAAAAAAAlMOlXNtby5f9Vld5QkfeTtXkpJ8NhAX14QAcoBTXzGIXMl5Yo4hNer6emdLQP4b40AGTKOacva2N2oJyoH9lXgz1hFz+FC9jfK0zeJAOtmzOttBK3a7Bqxw/FtRO";
    private final static String VALID_SIGN = "Mxf86d0b0101a6e58a4d4e540000000000000094c3a55cdb5bcb97fd5657794247de4ed5e4a49f0d8405f5e1001ca014d7cc6217325e58a3884d7abe9e99d2d03f86f8d0019328e69cbdad8dda8272a07f655e0cf5845cfe142f637cad3378900eb66cceb6d04addaec1ab1c3f16d44e";

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.statusView);
        Button button = findViewById(R.id.signButton);

        button.setOnClickListener(this::sign);

        sign(button);
    }

    private void sign(View v) {
        MinterApi.initialize(true);

        final PublicKey publicKey = new PublicKey(ADDR_MY);
        final PrivateKey privateKey = new PrivateKey(PRIV_MY);
        final PublicKey extracted = privateKey.getPublicKey();

/*
        RawTransaction<SendTx> tx = RawTransaction
                .newSendTransaction(new BigInteger("1"), new BigInteger("1"))
                .setCoin("MNT")
                .setTo("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99")
                .setValue(1)
                .build();


        // Send transaction
        MinterApi.getInstance().account()
                .sendTransaction(tx.sign(new PrivateKey(PRIV_MY)))
                .enqueue(new Callback<TransactionSendResult>() {
                    @Override
                    public void onResponse(@NonNull Call<TransactionSendResult> call, @NonNull Response<TransactionSendResult> response) {

                    }

                    @Override
                    public void onFailure(Call<TransactionSendResult> call, Throwable t) {

                    }
                });
*/


//        RawTransaction<Object> t = new RawTransaction<>(new BigInteger("11"));
//        t.setGasPrice(new BigInteger("1"));
//        t.setType(new BigInteger("1"));
//        SendTx tx = new SendTx();
//        tx.setCoin("MNT");
//        tx.setTo("Mxc3a55cdb5bcb97fd5657794247de4ed5e4a49f0d");
//        tx.setValue(1);
//        t.setData(tx);
//
//        final String txSign = t.sign(PRIV);
//
//        StringBuilder statusText = new StringBuilder();
//
//        if (txSign == null) {
//            status.setText(String.format("Invalid signature:\nNULL\n\nMust be:\n%s", VALID_SIGN));
//            return;
//        }
//
//        if (!txSign.equals(VALID_SIGN)) {
//            statusText.append(String.format("Invalid signature:\n%s\n\nMust be:\n%s", txSign, VALID_SIGN));
//        } else {
//            statusText.append(String.format("TX valid:\n %s\n\nMust be:\n%s", txSign, VALID_SIGN));
//        }
//
//
//        statusText.append("\n\n");
//        statusText.append(String.format("Singed size: %d", txSign.replace("Mx", "").length()));
//        statusText.append('\n');
//        statusText.append(String.format("Valid size:  %d", VALID_SIGN.replace("Mx", "").length()));
//
//        status.setText(statusText.toString());


    }
}
