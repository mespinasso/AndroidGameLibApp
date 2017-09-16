package br.com.mespinasso.gamelib.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.mespinasso.gamelib.R;
import br.com.mespinasso.gamelib.helper.Extras;
import br.com.mespinasso.gamelib.models.Store;

public class StoreMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private Store store;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra(Extras.EXTRA_STORE_OBJECT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng storeLocation = new LatLng(store.getLatitude(), store.getLongitude());
        mMap.addMarker(new MarkerOptions().position(storeLocation).title(store.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
