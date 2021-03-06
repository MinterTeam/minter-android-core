/*
 * Copyright (C) by MinterTeam. 2020
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
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

package network.minter.core.internal.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.common.Lazy;
import network.minter.core.internal.common.LazyMem;

import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-core. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class DataRepository<Service> {
    private final ApiService.Builder mApi;
    private final Lazy<Service> mService;

    public DataRepository(@Nonnull final ApiService.Builder apiBuilder) {
        mApi = checkNotNull(apiBuilder, "Api client required");
        checkNotNull(getServiceClass(), "Service class is null!");
        mService = LazyMem.memoize(() -> mApi.build().create(getServiceClass()));
    }

    /**
     * Creates and returns newly created endpoint service
     *
     * @return
     */
    @Nonnull
    public Service getInstantService() {
        return getInstantService(null);
    }

    @Nonnull
    public Service getInstantService(@Nullable Configurator cfg) {
        final ApiService.Builder b = mApi.clone();
        if (cfg != null) {
            cfg.configure(b);
        } else if (this instanceof Configurator) {
            return getInstantService(((Configurator) this));
        }

        return b.build().create(getServiceClass());
    }

    @Nonnull
    abstract protected Class<Service> getServiceClass();

    /**
     * Callback interface to configure api builder before calling request using retrofit
     */
    public interface Configurator {
        void configure(ApiService.Builder api);
    }
}
