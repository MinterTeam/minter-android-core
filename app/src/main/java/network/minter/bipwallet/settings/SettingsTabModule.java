package network.minter.bipwallet.settings;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import network.minter.bipwallet.auth.ui.InputGroup;
import network.minter.bipwallet.settings.ui.SettingsFieldType;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface SettingsTabModule {

    interface SettingsTabView extends MvpView {
        void startLogin();
        void startEditField(SettingsFieldType type, CharSequence label, String fieldNamed, String value);
        void setMainAdapter(RecyclerView.Adapter<?> mainAdapter);
        void setAdditionalAdapter(RecyclerView.Adapter<?> additionalAdapter);
        void startManageAddresses();
        void startChangePassword();
    }

    @StateStrategyType(OneExecutionStateStrategy.class)
    interface SettingsUpdateFieldView extends MvpView {
        void setOnTextChangedListener(TextWatcher watcher);
        void setLabel(CharSequence label);
        void setValue(String value);
        void setOnSubmit(View.OnClickListener listener);
        void configureInput(SettingsFieldType type, InputGroup.OnTextChangedListener textChangedListener);
        void setEnableSubmit(boolean valid);
        void dismiss();
        void callOnSaveListener();
    }
}
