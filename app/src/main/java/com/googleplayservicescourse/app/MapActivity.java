package com.googleplayservicescourse.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private static final CameraPosition SEATTLE = CameraPosition.builder()
        .target(new LatLng(47.6204, -122.3491))
        .zoom(17)
        .bearing(0)
        .tilt(45)
        .build();
    private MarkerOptions renton;
    private MarkerOptions kirkland;
    private MarkerOptions everett;
    private MarkerOptions lynnwood;
    private MarkerOptions montlake;
    private MarkerOptions kentValley;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        renton = new MarkerOptions().position(new LatLng(47.489805, -122.120502)).title("Renton")
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        kirkland = new MarkerOptions().position(new LatLng(47.7301986, -122.1768858)).title("kirkland");
        everett = new MarkerOptions().position(new LatLng(47.978748, -122.202001)).title("Everett");
        lynnwood = new MarkerOptions().position(new LatLng(47.819533, -122.32288)).title("Lynnwood");
        montlake = new MarkerOptions().position(new LatLng(47.7973733, -122.3281771)).title("Montlake  Terrace");
        kentValley = new MarkerOptions().position(new LatLng(47.385938, -122.258212)).title("Kent valley");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(renton);
        map.addMarker(kirkland);
        map.addMarker(everett);
        map.addMarker(lynnwood);
        map.addMarker(montlake);
        map.addMarker(kentValley);
        flyTo(SEATTLE);
    }

    private void flyTo(final CameraPosition cameraPosition) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
