package network.minter.mintercore.models.operational;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public enum OperationType {
    @SerializedName("sendCoin")
    SendCoin(new BigInteger("1"), SendTx.class);

    BigInteger mValue;
    Class<? extends Operation> mOpClass;

    OperationType(BigInteger value, Class<? extends Operation> opClass) {
        mValue = value;
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
