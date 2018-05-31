package network.minter.bipwallet.settings.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.internal.MvpBlurCompatDialogFragment;
import network.minter.bipwallet.settings.SettingsTabModule;
import network.minter.bipwallet.settings.views.SettingsUpdateFieldPresenter;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SettingsUpdateFieldDialog extends MvpBlurCompatDialogFragment implements SettingsTabModule.SettingsUpdateFieldView {
    public final static String ARG_LABEL = "ARG_LABEL";
    public final static String ARG_FIELD_NAME = "ARG_FIELD_NAME";
    public final static String ARG_VALUE = "ARG_VALUE";

    @Inject Provider<SettingsUpdateFieldPresenter> presenterProvider;
    @InjectPresenter SettingsUpdateFieldPresenter presenter;

    @BindView(R.id.field_label) TextView label;
    @BindView(R.id.field_input) TextInputEditText input;
    @BindView(R.id.field_action) Button action;

    public static SettingsUpdateFieldDialog newInstance(CharSequence label, String fieldName, String value) {
        Bundle args = new Bundle();
        args.putCharSequence(ARG_LABEL, label);
        args.putString(ARG_FIELD_NAME, fieldName);
        args.putString(ARG_VALUE, value);
        SettingsUpdateFieldDialog dialog = new SettingsUpdateFieldDialog();
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        View view = inflater.inflate(R.layout.fragment_update_field, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.handleExtras(getArguments());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HomeModule.getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Wallet_DialogFragment);
    }

    @Override
    public void onAttach(Context context) {
        HomeModule.getComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    protected boolean autoInjection() {
        return false;
    }

    @Override
    public void setOnTextChangedListener(TextWatcher watcher) {
        input.addTextChangedListener(watcher);
    }

    @Override
    public void setLabel(CharSequence label) {
        this.label.setText(label);
    }

    @Override
    public void setValue(String value) {
        input.setText(value);
    }

    @Override
    public void setOnSubmit(View.OnClickListener listener) {
        action.setOnClickListener(listener);
    }

    @ProvidePresenter
    SettingsUpdateFieldPresenter providePresenter() {
        return presenterProvider.get();
    }
}
