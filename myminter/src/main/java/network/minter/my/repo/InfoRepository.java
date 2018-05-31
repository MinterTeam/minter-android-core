package network.minter.my.repo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.my.api.InfoEndpoint;
import network.minter.my.models.AddressInfoResult;
import network.minter.my.models.MyResult;
import network.minter.my.models.User;
import retrofit2.Call;

/**
 * MyMinter API SDK. May 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class InfoRepository extends DataRepository<InfoEndpoint> {
    public InfoRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<MyResult<AddressInfoResult>> getAddressWithUserInfo(String address) {
        return getService().getAddressWithUserInfo(address);
    }

    public Call<MyResult<AddressInfoResult>> getAddressWithUserInfo(MinterAddress address) {
        return getAddressWithUserInfo(address.toString());
    }

    public Call<MyResult<List<AddressInfoResult>>> getAddressesWithUserInfo(List<String> addresses) {
        return getService().getAddressesWithUserInfo(addresses);
    }

    public Call<MyResult<List<AddressInfoResult>>> getAddressesWithUserInfoByMinter(List<MinterAddress> addresses) {
        final List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getAddressesWithUserInfo(out);
    }

    public Call<MyResult<User.Data>> getUserInfoByUsername(String username) {
        return getService().getUserInfoByUsername(username);
    }

    public Call<MyResult<User.Data>> getUserInfoByUser(User user) {
        return getUserInfoByUser(user.data);
    }

    public Call<MyResult<User.Data>> getUserInfoByUser(User.Data userData) {
        return getUserInfoByUsername(userData.username);
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    @NonNull
    @Override
    protected Class<InfoEndpoint> getServiceClass() {
        return InfoEndpoint.class;
    }

}
