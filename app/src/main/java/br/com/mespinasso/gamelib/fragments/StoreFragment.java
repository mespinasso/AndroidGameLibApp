package br.com.mespinasso.gamelib.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.adapters.StoreAdapter;
import br.com.mespinasso.gamelib.models.Store;
import br.com.mespinasso.gamelib.requests.StoreRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    private RecyclerView rvStore;
    private RecyclerView.Adapter adapter;

    private List<Store> listStores;

    public StoreFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);

        rvStore = (RecyclerView) view.findViewById(R.id.rv_store);
        rvStore.setHasFixedSize(true);
        rvStore.setLayoutManager(new LinearLayoutManager(getContext()));

        listStores = new LinkedList<>();

        adapter = new StoreAdapter(listStores, getContext());
        rvStore.setAdapter(adapter);

        loadStore();

        return view;
    }

    private void loadStore() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading_store));
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponseObject = new JSONObject(response);
                    JSONArray jsonResponseArray = jsonResponseObject.getJSONArray("stores");

                    for(int i = 0; i < jsonResponseArray.length(); i++) {
                        JSONObject jsonResponse = jsonResponseArray.getJSONObject(i);

                        String name = jsonResponse.getString("name");
                        String address = jsonResponse.getString("address");
                        Double latitude = jsonResponse.getDouble("lat");
                        Double longitude = jsonResponse.getDouble("long");
                        String phone = jsonResponse.getString("phone");
                        String phoneValue = jsonResponse.getString("phoneValue");

                        Store store = new Store(name, address, latitude, longitude, phone, phoneValue);

                        listStores.add(store);
                    }

                    adapter = new StoreAdapter(listStores, getContext());
                    rvStore.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
                }
            }
        };

        StoreRequest storeRequest = new StoreRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(storeRequest);
    }
}
