package network.minter.my.repo;

import android.support.annotation.NonNull;

import java.util.Map;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.mintercore.internal.helpers.CollectionsHelper;
import network.minter.my.api.ProfileEndpoint;
import network.minter.my.models.MyResult;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.User;
import retrofit2.Call;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ProfileRepository extends DataRepository<ProfileEndpoint> {
    public ProfileRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    @NonNull
    @Override
    protected Class<ProfileEndpoint> getServiceClass() {
        return ProfileEndpoint.class;
    }

    @Override
    protected boolean isAuthRequired() {
        return true;
    }

    public Call<MyResult<ProfileRequestResult>> updateProfile(@NonNull User.Data data) {
        checkNotNull(data);
        return getService().updateProfile(data);
    }

    public Call<MyResult<ProfileRequestResult>> updateField(String field, String value) {
        Map<String, String> data = CollectionsHelper.asMap(field, value);
        return getService().updateProfile(data);
    }
}
