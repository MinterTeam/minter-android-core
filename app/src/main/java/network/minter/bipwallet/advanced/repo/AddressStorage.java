package network.minter.bipwallet.advanced.repo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface AddressStorage {
    Observable<List<MinterAddress>> getAddresses();
    Completable addAddress(MinterAddress address);
    Completable removeAddress(MinterAddress address);
}
