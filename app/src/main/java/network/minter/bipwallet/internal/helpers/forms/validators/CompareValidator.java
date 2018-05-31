package network.minter.bipwallet.internal.helpers.forms.validators;


import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import network.minter.mintercore.internal.common.CallbackProvider;

/**
 * Dogsy. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public class CompareValidator extends BaseValidator {
    private CallbackProvider<CharSequence> mComparable;

    public CompareValidator(CallbackProvider<CharSequence> comparable, boolean required) {
        super(required);
        mComparable = comparable;
    }

    public CompareValidator(CharSequence errorMessage, boolean required, @NonNull EditText editText) {
        this(errorMessage, required, editText::getText);
    }

    public CompareValidator(CharSequence errorMessage, boolean required, @NonNull TextInputLayout inputLayout) {
        this(errorMessage, required, ()->inputLayout.getEditText().getText());
    }

    public CompareValidator(CharSequence errorMessage, boolean required, CallbackProvider<CharSequence> comparable) {
        super(errorMessage, required);
        mComparable = comparable;
    }

    public CompareValidator(CharSequence errorMessage, @NonNull EditText editText) {
        this(errorMessage, editText::getText);
    }

    public CompareValidator(CharSequence errorMessage, @NonNull TextInputLayout inputLayout) {
        this(errorMessage, ()->inputLayout.getEditText().getText());
    }

    public CompareValidator(CharSequence errorMessage, CallbackProvider<CharSequence> comparable) {
        super(errorMessage);
        mComparable = comparable;
    }

    @Override
    protected boolean getCondition(CharSequence value) {
        return value != null && mComparable.get() != null && value.toString().equals(mComparable.get().toString());
    }
}
