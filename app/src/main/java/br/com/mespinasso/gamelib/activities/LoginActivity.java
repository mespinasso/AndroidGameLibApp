package br.com.mespinasso.gamelib.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.dao.UserDAO;
import br.com.mespinasso.gamelib.helper.InputValidation;

public class LoginActivity extends AppCompatActivity {

    public static final String KEY_APP_PREFERENCES = "login";
    public static final String KEY_LOGIN = "username";

    private UserDAO userDAO;
    private InputValidation inputValidation;
    private CallbackManager callbackManager;

    private LinearLayout lnlLogin;
    private TextInputLayout tilLogin;
    private TextInputLayout tilPassword;
    private CheckBox cbKeepLoggedIn;
    private LoginButton btFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        userDAO = new UserDAO(LoginActivity.this);

        inputValidation = new InputValidation(LoginActivity.this);

        lnlLogin = (LinearLayout) findViewById(R.id.lnl_login);
        tilLogin = (TextInputLayout) findViewById(R.id.til_login);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        cbKeepLoggedIn = (CheckBox) findViewById(R.id.cb_keep_logged_in);
        btFacebookLogin = (LoginButton) findViewById(R.id.bt_facebook_login);

        btFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startApp();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "ocorreu erro3",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "ocorreu erro",Toast.LENGTH_LONG);

            }
        });

        if(isLoggedIn())
            startApp();
    }

    /**
     * Login
     * @param v
     */
    public void logIn(View v) {
        if(isValidLogin()) {
            if(cbKeepLoggedIn.isChecked()) {
                keepLoggedIn();
            }
            startApp();
        } else {
            Snackbar.make(lnlLogin, getString(R.string.login_failed), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Validates login
     * @return
     */
    private boolean isValidLogin() {
        if (!inputValidation.isInputEditTextFilled(tilLogin, getString(R.string.login_failed)))
            return false;

        if (!inputValidation.isInputEditTextFilled(tilPassword, getString(R.string.login_failed)))
            return false;

        if (!userDAO.checkUser(tilLogin.getEditText().getText().toString().trim(), tilPassword.getEditText().getText().toString()))
            return false;

        return true;
    }

    /**
     * Sets shared preferences settings to authenticate automatically
     */
    private void keepLoggedIn() {
        String login = tilLogin.getEditText().getText().toString();
        SharedPreferences sp = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_LOGIN, login);
        editor.apply();
    }

    /**
     * Checks if user is already logged in by searching in the SharedPreferences file
     * @return
     */
    private boolean isLoggedIn() {
        AccessToken fbStatus = AccessToken.getCurrentAccessToken();
        LoginManager.getInstance().getLoginBehavior();

        SharedPreferences sp = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        String login = sp.getString(KEY_LOGIN, "");

        if(login.equals("") && fbStatus == null)
            return false;

        return true;
    }

    /**
     * Starts MainActivity and finishes the LoginActivity
     */
    private void startApp() {
        emptyInputEditText();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Cleans up the text fields contents.
     */
    private void emptyInputEditText(){
        tilLogin.getEditText().setText(null);
        tilPassword.getEditText().setText(null);
    }
}
