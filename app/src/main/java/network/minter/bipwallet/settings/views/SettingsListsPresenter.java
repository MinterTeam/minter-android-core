package network.minter.bipwallet.settings.views;

import android.content.Intent;
import android.view.View;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import network.minter.bipwallet.home.HomeScope;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.views.rows.ChangeAvatarRow;
import network.minter.bipwallet.settings.views.rows.SettingsButtonRow;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@HomeScope
@InjectViewState
public class SettingsListsPresenter extends MvpBasePresenter<SettingsTabModule.SettingsListsView> {

    @Inject AuthSession session;

    private MultiRowAdapter mMainAdapter, mAdditionalAdapter;
    private ChangeAvatarRow mChangeAvatarRow;
    private Map<String, SettingsButtonRow> mMainSettingsRows = new HashMap<>();
    private Map<String, SettingsButtonRow> mAdditionalSettingsRows = new HashMap<>();

    @Inject
    public SettingsListsPresenter() {
        mMainAdapter = new MultiRowAdapter();
        mAdditionalAdapter = new MultiRowAdapter();
    }

    @Override
    public void attachView(SettingsTabModule.SettingsListsView view) {
        super.attachView(view);
        mChangeAvatarRow = new ChangeAvatarRow(session.getUser().getData().avatar, this::onClickChangeAvatar);
        mMainSettingsRows.put("username", new SettingsButtonRow("Username", session.getUser().getData().username, this::onClickChangeUsername));
        mMainSettingsRows.put("phone", new SettingsButtonRow("Mobile", session.getUser().getData().phone, "Add", this::onClickChangePhone));
        mMainSettingsRows.put("email", new SettingsButtonRow("Email", session.getUser().getData().email, "Add", this::onClickChangeEmail));
        mMainSettingsRows.put("password", new SettingsButtonRow("Password", "Change", this::onClickChangePassword).setInactive(true));

        mMainAdapter.addRow(mChangeAvatarRow);
        Stream.of(mMainSettingsRows.values()).forEach(item -> mMainAdapter.addRow(item));

        mAdditionalSettingsRows.put("language", new SettingsButtonRow("Language", session.getUser().getData().getLanguageDisplay(), this::onClickChangePassword).setInactive(true));
        Stream.of(mAdditionalSettingsRows.values()).forEach(item -> mAdditionalAdapter.addRow(item));
        mAdditionalAdapter.addRow(new SettingsButtonRow("My Addresses", "Manage", this::onClickAddresses).setInactive(true));

        getViewState().setMainAdapter(mMainAdapter);
        getViewState().setAdditionalAdapter(mAdditionalAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // update avatar
    }

    private void onClickAddresses(View view, View sharedView, String value) {
        getViewState().startManageAddresses();
    }

    private void onClickChangePassword(View view, View sharedView, String value) {

    }

    private void onClickChangeEmail(View view, View sharedView, String value) {
        getViewState().startEditField(sharedView, "Email", "email", value);
    }

    private void onClickChangePhone(View view, View sharedView, String value) {

    }

    private void onClickChangeUsername(View view, View sharedView, String value) {

    }

    private void onClickChangeAvatar(View view) {
    }
}
