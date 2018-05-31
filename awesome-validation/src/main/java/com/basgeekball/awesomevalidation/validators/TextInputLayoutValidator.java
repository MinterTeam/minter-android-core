package com.basgeekball.awesomevalidation.validators;

import android.support.design.widget.TextInputLayout;

import com.basgeekball.awesomevalidation.R;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.utility.ValidationCallback;

import java.util.regex.Matcher;

public class TextInputLayoutValidator extends Validator {

    private ValidationCallback mValidationCallback = new ValidationCallback() {
        @Override
        public void execute(ValidationHolder validationHolder, Matcher matcher) {
            TextInputLayout textInputLayout = validationHolder.getTextInputLayout();
            textInputLayout.setErrorTextAppearance(R.style.AwesomeValidation_TextInputLayout);
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(validationHolder.getErrMsg());
        }
    };

    @Override
    public boolean trigger() {
        if(mHasFailed) {
            if(!mClearOnValidOnly) {
                halt();
            }
        } else {
            halt();
        }

        return checkFields(mValidationCallback);
    }

    @Override
    public void halt() {
        for (ValidationHolder validationHolder : mValidationHolderList) {
            if (validationHolder.isSomeSortOfView()) {
                validationHolder.resetCustomError();
            } else {
                TextInputLayout textInputLayout = validationHolder.getTextInputLayout();
                textInputLayout.setErrorEnabled(false);
                textInputLayout.setError(null);
            }
        }
    }

}
