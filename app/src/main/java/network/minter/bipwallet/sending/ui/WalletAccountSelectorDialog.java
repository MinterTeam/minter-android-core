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

package network.minter.bipwallet.sending.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

import network.minter.bipwallet.internal.dialogs.WalletDialog;
import network.minter.bipwallet.internal.dialogs.WalletDialogBuilder;
import network.minter.mintercore.crypto.MinterAddress;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class WalletAccountSelectorDialog extends WalletDialog {
    private Builder mBuilder;

    protected WalletAccountSelectorDialog(@NonNull Context context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    public static final class Builder extends WalletDialogBuilder<WalletAccountSelectorDialog, WalletAccountSelectorDialog.Builder> {
        private Map<MinterAddress, AccountItem> mItems = new LinkedHashMap<>();

        public Builder(Context context) {
            super(context);
        }

        public Builder(Context context, CharSequence title) {
            super(context, title);
        }

        @Override
        public WalletAccountSelectorDialog create() {
            return new WalletAccountSelectorDialog(mContext, this);
        }

        public Builder addItem(MinterAddress address, String title) {
            mItems.put(address, new AccountItem(address, title, address.toString()));
            return this;
        }


    }

    private static final class AccountItem {
        private MinterAddress address;
        private String title;
        private String subtitle;

        private AccountItem(MinterAddress address, String title, String subtitle) {
            this.address = address;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}
