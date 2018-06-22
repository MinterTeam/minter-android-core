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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.models.AccountItem;
import network.minter.bipwallet.auth.ui.InputGroup;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.home.HomeTabFragment;
import network.minter.bipwallet.internal.dialogs.WalletDialog;
import network.minter.bipwallet.internal.helpers.forms.validators.RegexValidator;
import network.minter.bipwallet.sending.SendingTabModule;
import network.minter.bipwallet.sending.account.AccountSelectedAdapter;
import network.minter.bipwallet.sending.account.WalletAccountSelectorDialog;
import network.minter.bipwallet.sending.views.SendingTabPresenter;
import network.minter.explorerapi.MinterExplorerApi;
import network.minter.mintercore.internal.helpers.StringHelper;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SendingTabFragment extends HomeTabFragment implements SendingTabModule.SendingView {
    @Inject Provider<SendingTabPresenter> presenterProvider;
    @InjectPresenter SendingTabPresenter presenter;
    @BindView(R.id.coin_input) TextInputEditText coinInput;
    @BindView(R.id.recipient_input) TextInputEditText recipientInput;
    @BindView(R.id.amount_input) TextInputEditText amountInput;
    @BindView(R.id.free_value) Switch freeValue;
    @BindView(R.id.action) Button action;
    private Unbinder mUnbinder;
    private InputGroup mInputGroup;

    @Override
    public void onAttach(Context context) {
        HomeModule.getComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HomeModule.getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    private WalletDialog mCurrentDialog = null;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_send, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mInputGroup = new InputGroup();
        mInputGroup.addInput(recipientInput);
        mInputGroup.addInput(amountInput);
        mInputGroup.addValidator(amountInput, new RegexValidator("^(\\d{0,})(\\.|\\,)?(\\d{1,18})$", "Invalid number"));
        /* ideal case
        mInputGroup.addValidator(recipientInput,
                new RegexValidator(
                        // address or username with @ at begin or mobile or email
                        String.format("(((0|M|m)x)?([a-fA-F0-9]{40}))|(\\@[a-zA-Z0-9\\_\\-]+)|%s|%s", Patterns.PHONE, Patterns.EMAIL_ADDRESS),
                        "Incorrect recipient format"
                ));
                */
        mInputGroup.addValidator(recipientInput,
                new RegexValidator(
                        // address only for now
                        StringHelper.HEX_ADDRESS_PATTERN, "Incorrect recipient format"
                ));

        recipientInput.clearFocus();

        return view;
    }

    @Override
    public void setOnClickAccountSelectedListener(View.OnClickListener listener) {
        coinInput.setOnClickListener(listener);
    }

    @Override
    public void setOnTextChangedListener(InputGroup.OnTextChangedListener listener) {
        mInputGroup.addTextChangedListener(listener);
    }

    @Override
    public void setAccountName(CharSequence accountName) {
        coinInput.setText(accountName);
    }

    @Override
    public void setOnSubmit(View.OnClickListener listener) {
        action.setOnClickListener(listener);
    }

    @Override
    public void setSubmitEnabled(boolean enabled) {
        action.setEnabled(enabled);
    }

    @Override
    public void clearInputs() {
        recipientInput.setError(null);
        recipientInput.setText(null);
        amountInput.setError(null);
        amountInput.setText(null);
    }

    @Override
    public void startDialog(SendingTabModule.DialogExecutor executor) {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            mCurrentDialog = null;
        }

        mCurrentDialog = executor.run(getActivity());
        mCurrentDialog.show();
    }

    @Override
    public void startExplorer(String txHash) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MinterExplorerApi.FRONT_URL + "/transactions/" + txHash)));
    }

    @Override
    public void setFormValidationListener(InputGroup.OnFormValidateListener listener) {
        mInputGroup.addFormValidateListener(listener);
    }

    @Override
    public void startAccountSelector(List<AccountItem> accounts, AccountSelectedAdapter.OnClickListener clickListener) {
        new WalletAccountSelectorDialog.Builder(getActivity(), "Select account")
                .setItems(accounts)
                .setOnClickListener(clickListener)
                .create().show();
    }

    @ProvidePresenter
    SendingTabPresenter providePresenter() {
        return presenterProvider.get();
    }
}
