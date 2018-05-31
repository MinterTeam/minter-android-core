package network.minter.mintercore.internal.data;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.common.Lazy;
import network.minter.mintercore.internal.common.LazyMem;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * MinterCore. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class DataRepository<Service> {
    private final ApiService.Builder mApi;
    private final Lazy<Service> mService;

    public DataRepository(@NonNull final ApiService.Builder apiBuilder) {
        mApi = checkNotNull(apiBuilder, "Api client required");
        checkNotNull(getServiceClass(), "Service class is null reference!");
        mService = LazyMem.memoize(() -> {
            mApi.authRequired(isAuthRequired());
            configureService(mApi);
            return mApi.build().create(getServiceClass());
        });
    }

    @CallSuper
    public final Service getService() {
        return mService.get();
    }

    protected boolean isAuthRequired() {
        return false;
    }

    protected void configureService(ApiService.Builder apiBuilder) {
    }

    @NonNull
    abstract protected Class<Service> getServiceClass();
}
