/*
 * Copyright (C) 2018 by MinterTeam
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

package network.minter.mintercore;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import network.minter.mintercore.bip39.NativeBip39;
import network.minter.mintercore.crypto.NativeSecp256k1;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterSDK {
    public final static String DEFAULT_COIN = BuildConfig.DEFAULT_COIN_NAME;
    public final static String PREFIX_ADDRESS = "Mx";
    public final static String PREFIX_TX = "Mt";
    public final static String PREFIX_CHECK = "Mc";
    public final static String PREFIX_PUBLIC_KEY = "Mp";
    private static MinterSDK INSTANCE;

    private MinterSDK() {
    }

    public static void initialize() {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterSDK();

        NativeSecp256k1.init();
        NativeBip39.init();

        if (!NativeSecp256k1.isEnabled()) {
            throw new RuntimeException("Unable to load secp256k1 library");
        }

        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public static MinterSDK getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Have you forget to call MinterSDK.initialize()?");
        }
        return INSTANCE;
    }


}
