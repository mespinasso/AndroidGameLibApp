package br.com.mespinasso.gamelib.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by MatheusEspinasso on 14/09/17.
 */

public class StoreRequest extends StringRequest {

    private static final String STORE_REQUEST_URL = "http://www.mocky.io/v2/59bb49e80f00008f08622ad9";

    public StoreRequest(Response.Listener<String> listener) {
        super(Method.GET, STORE_REQUEST_URL, listener, null);
    }
}
