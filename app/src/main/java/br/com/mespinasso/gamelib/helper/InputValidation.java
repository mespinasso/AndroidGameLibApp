package br.com.mespinasso.gamelib.helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by MatheusEspinasso on 10/09/17.
 */

public class InputValidation {

    private Context context;

    public InputValidation(Context context){
        this.context = context;
    }

    public boolean isInputEditTextFilled(TextInputLayout textInputLayout, String message){
        String value = textInputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty()){
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputLayout.getEditText());

            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void hideKeyboardFrom(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
