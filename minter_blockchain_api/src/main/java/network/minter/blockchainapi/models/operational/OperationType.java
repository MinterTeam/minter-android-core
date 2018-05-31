package network.minter.blockchainapi.models.operational;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public enum OperationType {
    @SerializedName("1")
    SendCoin((byte)0x01, TxSendCoin.class),
    @SerializedName("2")
    ConvertCoin((byte)0x02, TxConvertCoin.class),
    @SerializedName("3")
    CreateCoin((byte)0x03, null),
    @SerializedName("4")
    DeclareCandidacy((byte)0x04, null),
    @SerializedName("5")
    Delegate((byte)0x05, null),
    @SerializedName("6")
    Unbond((byte)0x06, null);

    BigInteger mValue;
    Class<? extends Operation> mOpClass;

    OperationType(byte value, Class<? extends Operation> opClass) {
        mValue = new BigInteger(String.valueOf(value));
        mOpClass = opClass;
    }

    @Nullable
    public static OperationType findByValue(BigInteger type) {
        for (OperationType t : values()) {
            if (t.mValue.equals(type)) {
                return t;
            }
        }
        return null;
    }

    @Nullable
    public static OperationType findByOpClass(Class<? extends Operation> opClass) {
        for (OperationType t : values()) {
            if (t.getOpClass().equals(opClass)) {
                return t;
            }
        }

        return null;
    }

    public Class<? extends Operation> getOpClass() {
        return mOpClass;
    }

    public BigInteger getValue() {
        return mValue;
    }
}
