package network.minter.my.repo;

import android.support.annotation.NonNull;

import java.util.List;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.my.api.AddressEndpoint;
import network.minter.my.models.AddressData;
import network.minter.my.models.MyResult;
import retrofit2.Call;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressRepository extends DataRepository<AddressEndpoint> {
    public AddressRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<MyResult<List<AddressData>>> getAddresses() {
        return getService().getAddresses();
    }

    public Call<MyResult<List<AddressData>>> getAddressesWithEncrypted() {
        return getService().getAddressesWithEncrypted();
    }

    public Call<MyResult<Void>> delete(AddressData address) {
        return getService().deleteAddress(address.id);
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @NonNull
    @Override
    protected Class<AddressEndpoint> getServiceClass() {
        return AddressEndpoint.class;
    }

}
