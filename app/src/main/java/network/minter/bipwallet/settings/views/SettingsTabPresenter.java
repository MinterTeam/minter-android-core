package network.minter.bipwallet.settings.views;

import android.content.Intent;
import android.view.View;

import com.annimon.stream.Stream;
import com.arellomobile.mvp.InjectViewState;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import network.minter.bipwallet.home.HomeScope;
import network.minter.bipwallet.internal.auth.AuthSession;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.ui.SettingsFieldType;
import network.minter.bipwallet.settings.views.rows.ChangeAvatarRow;
import network.minter.bipwallet.settings.views.rows.SettingsButtonRow;
import network.minter.my.models.User;
import network.minter.my.repo.ProfileRepository;

import static network.minter.bipwallet.internal.ReactiveAdapter.rxCall;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@HomeScope
@InjectViewState
public class SettingsTabPresenter extends MvpBasePresenter<SettingsTabModule.SettingsTabView> {

    @Inject AuthSession session;
    @Inject ProfileRepository profileRepo;

    private MultiRowAdapter mMainAdapter, mAdditionalAdapter;
    private ChangeAvatarRow mChangeAvatarRow;
    private Map<String, SettingsButtonRow> mMainSettingsRows = new LinkedHashMap<>();
    private Map<String, SettingsButtonRow> mAdditionalSettingsRows = new LinkedHashMap<>();

    @Inject
    public SettingsTabPresenter() {
        mMainAdapter = new MultiRowAdapter();
        mAdditionalAdapter = new MultiRowAdapter();
    }

    public void onUpdateProfile() {
        safeSubscribeIoToUi(rxCall(profileRepo.getProfile()))
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        User u = session.getUser();
                        u.data = res.data;
                        session.setUser(u);
                        mMainAdapter.notifyItemRangeChanged(0, mMainAdapter.getItemCount());
                        mAdditionalAdapter.notifyItemRangeChanged(0, 1);
                    }
                });
    }

    @Override
    public void attachView(SettingsTabModule.SettingsTabView view) {
        super.attachView(view);

        getViewState().setMainAdapter(mMainAdapter);
        getViewState().setAdditionalAdapter(mAdditionalAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // update avatar
    }

    public void onLogout() {
        getViewState().startLogin();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mChangeAvatarRow = new ChangeAvatarRow(() -> session.getUser().getData().avatar, this::onClickChangeAvatar);
        mMainAdapter.addRow(mChangeAvatarRow);

        mMainSettingsRows.put("username", new SettingsButtonRow("Username", () -> "@" + session.getUser().getData().username, this::onClickChangeUsername));
        mMainSettingsRows.put("phone", new SettingsButtonRow("Mobile", () -> session.getUser().getData().phone, "Add", this::onClickChangePhone));
        mMainSettingsRows.put("email", new SettingsButtonRow("Email", () -> session.getUser().getData().email, "Add", this::onClickChangeEmail));
        mMainSettingsRows.put("password", new SettingsButtonRow("Password", "Change", this::onClickChangePassword).setInactive(true));
        Stream.of(mMainSettingsRows.values()).forEach(item -> mMainAdapter.addRow(item));

        mAdditionalSettingsRows.put("language", new SettingsButtonRow("Language", session.getUser().getData().getLanguageDisplay(), this::onClickChangePassword).setInactive(true));
        Stream.of(mAdditionalSettingsRows.values()).forEach(item -> mAdditionalAdapter.addRow(item));
        mAdditionalAdapter.addRow(new SettingsButtonRow("My Addresses", "Manage", this::onClickAddresses).setInactive(true));
    }

    private void onClickAddresses(View view, View sharedView, String value) {
        getViewState().startManageAddresses();
    }

    private void onClickChangePassword(View view, View sharedView, String value) {
        getViewState().startChangePassword();
    }

    private void onClickChangeEmail(View view, View sharedView, String value) {
        getViewState().startEditField(SettingsFieldType.Email, "Email", "email", value);
    }

    private void onClickChangePhone(View view, View sharedView, String value) {
        getViewState().startEditField(SettingsFieldType.Phone, "Mobile number", "phone", value);
    }

    private void onClickChangeUsername(View view, View sharedView, String value) {
        getViewState().startEditField(SettingsFieldType.Username, "Username", "username", value);
    }

    private void onClickChangeAvatar(View view) {
//        getViewState().startAvatarSelection();
    }


}
