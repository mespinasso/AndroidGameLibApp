package br.com.mespinasso.gamelib.requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by MatheusEspinasso on 03/09/17.
 */

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://www.mocky.io/v2/58b9b1740f0000b614f09d2f";

    public LoginRequest(Response.Listener<String> listener) {
        super(Request.Method.GET, LOGIN_REQUEST_URL, listener, null);
    }
}
