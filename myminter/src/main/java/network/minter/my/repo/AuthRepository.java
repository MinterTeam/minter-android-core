package network.minter.my.repo;

import android.support.annotation.NonNull;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import network.minter.my.api.AuthEndpoint;
import network.minter.my.models.LoginData;
import network.minter.my.models.MyResult;
import network.minter.my.models.ProfileRequestResult;
import network.minter.my.models.RegisterData;
import network.minter.my.models.User;
import network.minter.my.models.UsernameData;
import retrofit2.Call;

/**
 * MinterWallet. Май 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AuthRepository extends DataRepository<AuthEndpoint> {
    public AuthRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    @NonNull
    @Override
    protected Class<AuthEndpoint> getServiceClass() {
        return AuthEndpoint.class;
    }

    public Call<MyResult<User>> login(LoginData loginData) {
        return getService().login(loginData);
    }

    public Call<MyResult<ProfileRequestResult>> register(RegisterData registerData) {
        return getService().register(registerData);
    }

    public Call<MyResult<UsernameData>> checkUsernameAvailability(String username) {
        return getService().checkUsernameAvailability(username);
    }
}
