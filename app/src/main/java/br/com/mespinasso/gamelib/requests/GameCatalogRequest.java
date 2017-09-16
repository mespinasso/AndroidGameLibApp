package br.com.mespinasso.gamelib.requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by MatheusEspinasso on 10/09/17.
 */

public class GameCatalogRequest extends StringRequest {

    private static final String GAME_CATALOG_REQUEST_URL = "http://www.mocky.io/v2/59b72e840f0000120c712682";

    public GameCatalogRequest(Response.Listener<String> listener) {
        super(Request.Method.GET, GAME_CATALOG_REQUEST_URL, listener, null);
    }
}
