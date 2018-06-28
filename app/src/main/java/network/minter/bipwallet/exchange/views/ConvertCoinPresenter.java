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

package network.minter.bipwallet.exchange.views;

import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import network.minter.bipwallet.R;
import network.minter.bipwallet.advanced.models.AccountItem;
import network.minter.bipwallet.advanced.models.UserAccount;
import network.minter.bipwallet.advanced.repo.AccountStorage;
import network.minter.bipwallet.advanced.repo.SecretStorage;
import network.minter.bipwallet.apis.explorer.CachedExplorerTransactionRepository;
import network.minter.bipwallet.exchange.ExchangeModule;
import network.minter.bipwallet.internal.data.CachedRepository;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.blockchainapi.repo.BlockChainCoinRepository;
import network.minter.explorerapi.models.HistoryTransaction;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class ConvertCoinPresenter extends MvpBasePresenter<ExchangeModule.ConvertCoinView> {
    @Inject SecretStorage secretStorage;
    @Inject CachedRepository<UserAccount, AccountStorage> accountStorage;
    @Inject CachedRepository<List<HistoryTransaction>, CachedExplorerTransactionRepository> txRepo;
    @Inject BlockChainCoinRepository coinRepo;

    private AccountItem mFromAccount;
    private String mInCoin = null;
    private BigDecimal mInAmount = new BigDecimal(0);
    private BigDecimal mOutAmount = new BigDecimal(0);

    @Inject
    public ConvertCoinPresenter() {
    }

    @Override
    public void attachView(ExchangeModule.ConvertCoinView view) {
        super.attachView(view);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        safeSubscribeIoToUi(accountStorage.observe())
                .subscribe(res -> {
                    if (!res.isEmpty()) {
                        mFromAccount = res.getAccounts().get(0);
                        onAccountSelected(mFromAccount);
                    }
                });

        getViewState().setSubmitEnabled(false);
        getViewState().setFormValidationListener(valid -> getViewState().setSubmitEnabled(valid));
        getViewState().setTextChangedListener(this::onInputChanged);
        getViewState().setOnClickSelectAccount(this::onClickSelectAccount);
        getViewState().setMaximumTitle("Use max.");
    }

    private void onClickSelectAccount(View view) {
        getViewState().startAccountSelector(accountStorage.getData().getAccounts(), this::onAccountSelected);
    }

    private void onAccountSelected(AccountItem accountItem) {
        getViewState().setOutAccountName(String.format("%s (%s)", accountItem.coin.toUpperCase(), accountItem.balance.toString()));
    }

    private void onInputChanged(EditText editText, boolean valid) {
        final String text = editText.getText().toString();
        switch (editText.getId()) {
            case R.id.input_incoming_coin:
                mInCoin = text;
                break;
            case R.id.input_incoming_amount:
                if (!text.isEmpty()) {
                    mInAmount = new BigDecimal(text);
                } else {
                    mInAmount = new BigDecimal(0);
                }
                break;
            case R.id.input_outgoing_amount:
                if (!text.isEmpty()) {
                    mOutAmount = new BigDecimal(text);
                } else {
                    mOutAmount = new BigDecimal(0);
                }
                break;
        }
    }
}
