/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.core;

import com.edwardstock.secp256k1.NativeSecp256k1;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import network.minter.core.bip39.NativeBip39;
import network.minter.core.internal.exceptions.NativeLoadException;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterSDK {
    @SuppressWarnings("unused")
    public final static String DEFAULT_COIN = BuildConfig.DEFAULT_COIN_NAME;
    public final static String PREFIX_ADDRESS = "Mx";
    public final static String PREFIX_TX = "Mt";
    public final static String PREFIX_CHECK = "Mc";
    public final static String PREFIX_PUBLIC_KEY = "Mp";
    private static MinterSDK INSTANCE;

    private MinterSDK() {
    }

    public static void initialize() throws NativeLoadException {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterSDK();

        NativeSecp256k1.init();
        NativeBip39.init();

        if (!NativeSecp256k1.isEnabled()) {
            throw new NativeLoadException(NativeSecp256k1.getError());
        }

        if (!NativeBip39.isEnabled()) {
            throw new NativeLoadException(NativeBip39.getError());
        }

        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * Use this if you are catched {@link UnsatisfiedLinkError} and loaded native libraries by yourself, if not, it will crash at unexpected place
     * @param enabledNativeLibs
     */
    public static void setEnabledNativeLibs(boolean enabledNativeLibs) {
        NativeSecp256k1.setEnabled(enabledNativeLibs);
        NativeBip39.setEnabled(enabledNativeLibs);
    }

    public static MinterSDK getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Did you forget to call MinterSDK.initialize()?");
        }
        return INSTANCE;
    }


}
