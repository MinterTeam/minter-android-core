package network.minter.bipwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import network.minter.mintercore.MinterApi;
import network.minter.mintercore.bip39.HDKey;
import network.minter.mintercore.bip39.MnemonicResult;
import network.minter.mintercore.bip39.NativeBip39;
import network.minter.mintercore.bip39.NativeHDKeyEncoder;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.crypto.PublicKey;
import network.minter.mintercore.helpers.StringHelper;
import timber.log.Timber;

import static junit.framework.Assert.assertEquals;

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


    }
}
