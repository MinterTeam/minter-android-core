package network.minter.bipwallet.settings;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;

import com.arellomobile.mvp.MvpView;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface SettingsTabModule {

    interface SettingsTabView extends MvpView {
        void startLogin();
        void showLists();
        void startEditField(View shared, CharSequence label, String fieldNamed, String value);
    }

    interface SettingsListsView extends MvpView {
        void setMainAdapter(RecyclerView.Adapter<?> mainAdapter);
        void setAdditionalAdapter(RecyclerView.Adapter<?> additionalAdapter);
        void startManageAddresses();
        void startEditField(View shared, CharSequence label, String fieldName, String value);
    }

    interface SettingsUpdateFieldView extends MvpView {
        void setOnTextChangedListener(TextWatcher watcher);
        void setLabel(CharSequence label);
        void setValue(String value);
        void setOnSubmit(View.OnClickListener listener);
    }
}
