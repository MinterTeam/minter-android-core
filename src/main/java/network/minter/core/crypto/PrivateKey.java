/*
 * Copyright (C) by MinterTeam. 2019
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

package network.minter.core.crypto;

import com.edwardstock.secp256k1.NativeSecp256k1;

import org.parceler.Parcel;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import network.minter.core.bip39.HDKey;
import network.minter.core.bip39.MnemonicResult;
import network.minter.core.bip39.NativeHDKeyEncoder;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-core. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class PrivateKey extends BytesData implements java.security.PrivateKey {
    public PrivateKey(BytesData data) {
        super(data);
    }

    public PrivateKey(BytesData data, boolean immutable) {
        super(data, immutable);
    }

    public PrivateKey(Byte[] data) {
        super(data);
    }

    public PrivateKey(byte[] data, boolean immutable) {
        super(data, immutable);
    }

    public PrivateKey(ByteBuffer buffer) {
        super(buffer);
    }

    public PrivateKey(ByteBuffer buffer, boolean immutable) {
        super(buffer, immutable);
    }

    public PrivateKey(byte[] data) {
        super(data);
    }

	public PrivateKey(char[] data) {
		super(data);
	}

    public PrivateKey(CharSequence hexData) {
        super(hexData);
    }

    //parcel - be carefully
    PrivateKey() {
    }

    public static PrivateKey fromMnemonic(@Nonnull final String mnemonic) {
        checkArgument(mnemonic != null && !mnemonic.isEmpty(), "Mnemonic phrase can't be empty");
        final MnemonicResult mnemonicResult = new MnemonicResult(mnemonic);
        final BytesData seed = new BytesData(mnemonicResult.toSeed());
        final HDKey rootKey = NativeHDKeyEncoder.makeBip32RootKey(seed.getBytes());
        final HDKey extKey = NativeHDKeyEncoder.makeExtenderKey(rootKey);
        final PrivateKey privateKey = extKey.getPrivateKey();

        seed.cleanup();
        rootKey.clear();
        extKey.clear();

        return privateKey;
    }

    /**
     * Load private key from file
     * @param file
     * @return PrivateKey data
     * @throws IOException if file not found, can't open stream or something else
     */
    public static PrivateKey fromFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File %s does not exists", file.getAbsoluteFile()));
        }
        if (!file.canRead()) {
            throw new IllegalStateException(String.format("File %s is not readable", file.getAbsoluteFile()));
        }

        byte[] data = new byte[(int) file.length()];
        DataInputStream is = new DataInputStream(new FileInputStream(file));
        is.readFully(data);


        return new PrivateKey(data);
    }

    /**
     * Check private key for validity
     * @return true if private key is valid
     */
    public boolean verify() {
        if (!isValid()) {
            throw new IllegalStateException("Can't verify, private key already disposed");
        }

        long ctx = NativeSecp256k1.contextCreate();
        boolean res;
        try {
            res = NativeSecp256k1.secKeyVerify(ctx, getBytes());
        } catch (Throwable t) {
            res = false;
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }
        return res;
    }

    public PublicKey getPublicKey() {
        return getPublicKey(false);
    }

    /**
     * Extract public key from private
     * @return Public Key data
     */
    public PublicKey getPublicKey(boolean compressed) {
        if (!isValid()) {
            throw new IllegalStateException("Can't get public key, private key already disposed");
        }

        long ctx = NativeSecp256k1.contextCreate();
        PublicKey out;
        try {
            out = new PublicKey(NativeSecp256k1.computePubkey(ctx, getBytes(), compressed));
        } finally {
            NativeSecp256k1.contextCleanup(ctx);
        }

        return out;
    }

    @Override
    public String getAlgorithm() {
        return "EC";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return getBytes();
    }

    @Override
    public void destroy() {
        cleanup();
    }

    @Override
    public boolean isDestroyed() {
        return !isValid();
    }

    @Override
    public PrivateKey clone() {
        super.clone();
        PrivateKey out = new PrivateKey();
        out.mValid = mValid;
        out.mData = new char[mData.length];
        System.arraycopy(mData, 0, out.mData, 0, mData.length);

        return out;
    }
}
