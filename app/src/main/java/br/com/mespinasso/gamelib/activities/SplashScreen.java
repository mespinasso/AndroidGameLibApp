package br.com.mespinasso.gamelib.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.dao.UserDAO;
import br.com.mespinasso.gamelib.helper.InputValidation;
import br.com.mespinasso.gamelib.models.User;
import br.com.mespinasso.gamelib.requests.LoginRequest;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_SCREEN_DISPLAY_TIME = 3000;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        userDAO = new UserDAO(this);

        load();
    }

    private void load() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        animation.reset();

        ImageView iv = (ImageView) findViewById(R.id.splash_icon);
        if(iv != null) {
            iv.clearAnimation();
            iv.startAnimation(animation);
        }

        syncLoginData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                SplashScreen.this.finish();
            }
        }, SPLASH_SCREEN_DISPLAY_TIME);
    }

    private void syncLoginData() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    String username = jsonResponse.getString("usuario");
                    String password = jsonResponse.getString("senha");

                    User user = new User(username, password);

                    if (!userDAO.checkUser(user.getUsername()))
                        userDAO.add(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);
        queue.add(loginRequest);
    }
}
