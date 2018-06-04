package network.minter.bipwallet.auth.ui;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.bipwallet.internal.helpers.forms.validators.BaseValidator;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class InputGroup {
    private List<EditText> mInputs = new ArrayList<>();
    private List<OnTextChangedListener> mTextWatchers = new ArrayList<>();
    private Map<EditText, List<BaseValidator>> mInputValidators = new HashMap<>();
    private List<OnFormValidateListener> mValidFormListeners = new ArrayList<>();
    private Map<Integer, Boolean> mValidMap = new HashMap<>();
    private List<Integer> mRequiredInputs = new ArrayList<>();
    private OnTextChangedListener mInternalTextListener = new OnTextChangedListener() {
        @Override
        public void onTextChanged(EditText editText, boolean valid) {
            mValidMap.put(editText.getId(), valid);

            int countValid = 0;
            for (Integer id : mRequiredInputs) {
                if (mValidMap.containsKey(id) && mValidMap.get(id)) {
                    countValid++;
                }
            }

            // valid required elements and all elements are valid
            boolean outValid = countValid == mRequiredInputs.size() && Stream.of(mValidMap).filter(
                    Map.Entry::getValue).count() == mValidMap.size();

//            final boolean outValid = mValidMap.size() == mRequiredInputs.size()
//                    && Stream.of(mValidMap.entrySet()).filter(Map.Entry::getValue).count() == mRequiredInputs.size();

            for (OnFormValidateListener listener : mValidFormListeners) {
                listener.onValid(outValid);
            }
        }
    };
    private Map<String, EditText> mInputNames = new HashMap<>();

    public InputGroup addFormValidateListener(OnFormValidateListener listener) {
        mValidFormListeners.add(listener);
        return this;
    }

    public InputGroup addInput(final TextInputLayout inputLayout) {
        return addInput(inputLayout.getEditText());
    }

    public InputGroup addInput(final EditText input) {
        mInputs.add(input);
        if(input.getTag() != null && input.getTag() instanceof String) {
            mInputNames.put(((String) input.getTag()), input);
        }

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mTextWatchers.isEmpty()) {
                    final boolean refValid[] = new boolean[]{false};
                    Stream.of(mTextWatchers).forEach(item -> {
                        boolean valid = validate(input, true);
                        item.onTextChanged(input, valid);
                        refValid[0] = valid;
                    });
                    mInternalTextListener.onTextChanged(input, refValid[0]);
                } else {
                    final boolean valid = validate(input, true);
                    mInternalTextListener.onTextChanged(input, valid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return this;
    }

    public boolean validate(boolean withError) {
        int countValid = 0;
        for (Map.Entry<EditText, List<BaseValidator>> entry : mInputValidators.entrySet()) {
            if (validate(entry.getKey(), withError)) {
                countValid++;
            }
        }

        return countValid == mInputValidators.size();
    }

    public InputGroup addTextChangedListener(OnTextChangedListener listener) {
        mTextWatchers.add(listener);
        return this;
    }

    public InputGroup addValidator(TextInputLayout inputLayout, BaseValidator validator) {
        return addValidator(inputLayout.getEditText(), validator);
    }

    public InputGroup addValidator(EditText editText, BaseValidator validator) {
        if (!mInputValidators.containsKey(editText)) {
            mInputValidators.put(editText, new ArrayList<>());
        }
        mInputValidators.get(editText).add(validator);
        if (validator.isRequired()) {
            mRequiredInputs.add(editText.getId());
        }
        return this;
    }

    public void clearErrors() {
        for(Map.Entry<String, EditText> entry: mInputNames.entrySet()) {
            if (entry.getValue().getParent() != null && entry.getValue().getParent().getParent() instanceof TextInputLayout) {
                final TextInputLayout tl = ((TextInputLayout) entry.getValue().getParent().getParent());
                tl.setError(null);
                tl.setErrorEnabled(false);
            } else {
                entry.getValue().setError(null);
            }
        }
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        Stream.of(mInputs)
                .forEach(item -> item.setOnEditorActionListener(listener));
    }

    public void setError(String fieldName, String message) {
        if (!mInputNames.containsKey(fieldName)) {
            return;
        }

        if (mInputNames.get(fieldName).getParent() != null && mInputNames.get(
                fieldName).getParent().getParent() instanceof TextInputLayout) {
            final TextInputLayout tl = ((TextInputLayout) mInputNames.get(fieldName).getParent().getParent());
            tl.setError(message);
            tl.setErrorEnabled(message != null && message.length() > 0);
        } else {
            mInputNames.get(fieldName).setError((message == null || message.isEmpty()) ? null : message);
        }
    }

    public void setErrors(Map<String, List<String>> fieldsErrors) {
        if(fieldsErrors == null || fieldsErrors.isEmpty()) {
            return;
        }

        for(Map.Entry<String, List<String>> entry: fieldsErrors.entrySet()) {
            if(entry.getValue().isEmpty()) {
                continue;
            }

            setError(entry.getKey(), entry.getValue().get(0));
        }
    }

    private boolean validate(EditText editText, boolean withError) {
        if (!mInputValidators.containsKey(editText)) {
            return true;
        }

        final CharSequence t = editText.getText();
        long cnt = Stream.of(mInputValidators.get(editText))
                .filter(item -> {
                    boolean valid = item.validate(t);

                    if (withError) {
                        if (editText.getParent() != null && editText.getParent().getParent() instanceof TextInputLayout) {
                            final TextInputLayout lay = ((TextInputLayout) editText.getParent().getParent());
                            if (!valid) {
                                lay.setErrorEnabled(true);
                                lay.setError(item.getErrorMessage());
                            } else {
                                lay.setError(null);
                                lay.setErrorEnabled(false);
                            }
                        } else {
                            if (!valid) {
                                editText.setError(item.getErrorMessage());
                            } else {
                                editText.setError(null);
                            }
                        }
                    }

                    return valid;
                })
                .count();


        // count validated == validators length
        return cnt == mInputValidators.get(editText).size();
    }

    public interface OnFormValidateListener {
        void onValid(boolean valid);
    }

    public interface OnTextChangedListener {
        void onTextChanged(EditText editText, boolean valid);
    }


}