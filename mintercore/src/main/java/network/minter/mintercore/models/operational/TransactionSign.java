package network.minter.mintercore.models.operational;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TransactionSign {
    private final String mSign;

    TransactionSign(String sign) {
        mSign = sign;
    }

    public String getTxSign() {
        return mSign;
    }


}
