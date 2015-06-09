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

    private static final CameraPosition NYC = CameraPosition.builder()
        .target(new LatLng(40.7127, 74.0059))
        .zoom(21)
        .bearing(0)
        .tilt(45)
        .build();

    private static final CameraPosition SEATTLE = CameraPosition.builder()
        .target(new LatLng(47.6204, -122.3491))
        .zoom(17)
        .bearing(0)
        .tilt(45)
        .build();

    private static final CameraPosition DUBLIN = CameraPosition.builder()
        .target(new LatLng(53.3478, 6.2597))
        .zoom(17)
        .bearing(90)
        .tilt(45)
        .build();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        findViewById(R.id.seattleBtn).setOnClickListener(this);
        findViewById(R.id.nycBtn).setOnClickListener(this);
        findViewById(R.id.dublinBtn).setOnClickListener(this);

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
        if (v.getId() == R.id.seattleBtn) {
            if (mapReady) {
                flyTo(SEATTLE);
            }
        } else if (v.getId() == R.id.nycBtn) {
            if (mapReady) {
                flyTo(NYC);
            }
        } else if (v.getId() == R.id.dublinBtn) {
            if (mapReady) {
                flyTo(DUBLIN);
            }
        }
    }

    private void flyTo(final CameraPosition cameraPosition) {
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null);
    }
}
