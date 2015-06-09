package com.googleplayservicescourse.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private boolean mapReady;
    private GoogleMap map;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        findViewById(R.id.mapBtn).setOnClickListener(this);
        findViewById(R.id.satelliteBtn).setOnClickListener(this);
        findViewById(R.id.hybridBtn).setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapReady = true;
        map = googleMap;
        LatLng newYork = new LatLng(40.7484, -73.9857);
        CameraPosition cameraPosition = CameraPosition.builder().target(newYork).zoom(14).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.mapBtn) {
            if (mapReady) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        } else if (v.getId() == R.id.satelliteBtn) {
            if (mapReady) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        } else if (v.getId() == R.id.hybridBtn) {
            if (mapReady) {
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        }
    }
}
