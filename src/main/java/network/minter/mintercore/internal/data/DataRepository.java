/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.mintercore.internal.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.common.Lazy;
import network.minter.mintercore.internal.common.LazyMem;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class DataRepository<Service> {
    private final ApiService.Builder mApi;
    private final Lazy<Service> mService;

    public DataRepository(@NonNull final ApiService.Builder apiBuilder) {
        mApi = checkNotNull(apiBuilder, "Api client required");
        checkNotNull(getServiceClass(), "Service class is null!");
        mService = LazyMem.memoize(() -> {
            mApi.authRequired(isAuthRequired());
            configureService(mApi);
            return mApi.build().create(getServiceClass());
        });
    }

    /**
     * @return {@link retrofit2.Retrofit} service
     * @deprecated Use {@link #getInstantService()} instead
     */
    @Deprecated
    public final Service getService() {
        return mService.get();
    }

    /**
     * Creates and returns newly created endpoint service
     *
     * @return
     */
    @NonNull
    public Service getInstantService() {
        return getInstantService(null);
    }

    @NonNull
    public Service getInstantService(@Nullable Configurator cfg) {
        final ApiService.Builder b = mApi.clone();
        if (cfg != null) {
            cfg.configure(b);
        } else if (this instanceof Configurator) {
            return getInstantService(((Configurator) this));
        }

        return b.build().create(getServiceClass());
    }

    /**
     * @return
     * @deprecated Use {@link #getInstantService(Configurator)} instead, set auth required with {@link Configurator#configure(ApiService.Builder)}
     */
    @Deprecated
    protected boolean isAuthRequired() {
        return false;
    }

    /**
     * @param apiBuilder
     * @see ApiService.Builder
     * @deprecated Use {@link Configurator} instead. Example:
     * <pre>
     *     {@code
     *     class MyApiRepo extends DataRepository implements Configurator {
     *         public Call<MyResult<MyObject>> getSomeApiMethod() {
     *              return getInstantService(this).getSomeApiMethod();
     *         }
     *
     *         \@Override
     *         public ApiService.Builder configure(ApiService.Builder api) {
     *             api.authRequired();
     *             return api;
     *         }
     *     }
     *     }
     * </pre>
     *
     * or just callback for single usage:
     * <pre>
     *     {@code
     *     public Call<MyResult<MyObject>> getSomeApiMethod() {
     *          return getInstantService(api->{
     *              return api.authRequired(false);
     *          }).getSomeApiMethod();
     *     }
     *     }
     * </pre>
     */
    @Deprecated
    protected void configureService(ApiService.Builder apiBuilder) {
    }

    @NonNull
    abstract protected Class<Service> getServiceClass();

    /**
     * Callback interface to configure api builder before calling request using retrofit
     */
    public interface Configurator {
        void configure(ApiService.Builder api);
    }
}
