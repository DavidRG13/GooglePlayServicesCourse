package com.googleplayservicescourse.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    private static final CameraPosition SEATTLE = CameraPosition.builder().target(new LatLng(47.6204, -122.3491)).zoom(17).bearing(0).tilt(45).build();
    private MarkerOptions renton;
    private MarkerOptions kirkland;
    private MarkerOptions everett;
    private MarkerOptions lynnwood;
    private MarkerOptions montlake;
    private MarkerOptions kentValley;
    private LatLng rentonPosition;
    private LatLng kirklandPosition;
    private LatLng everettPosition;
    private LatLng lynnwoodPosition;
    private LatLng montlakePosition;
    private LatLng kentValleyPosition;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        rentonPosition = new LatLng(47.489805, -122.120502);
        renton = new MarkerOptions().position(rentonPosition).title("Renton").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        kirklandPosition = new LatLng(47.7301986, -122.1768858);
        kirkland = new MarkerOptions().position(kirklandPosition).title("kirkland");
        everettPosition = new LatLng(47.978748, -122.202001);
        everett = new MarkerOptions().position(everettPosition).title("Everett");
        lynnwoodPosition = new LatLng(47.819533, -122.32288);
        lynnwood = new MarkerOptions().position(lynnwoodPosition).title("Lynnwood");
        montlakePosition = new LatLng(47.7973733, -122.3281771);
        montlake = new MarkerOptions().position(montlakePosition).title("Montlake  Terrace");
        kentValleyPosition = new LatLng(47.385938, -122.258212);
        kentValley = new MarkerOptions().position(kentValleyPosition).title("Kent valley");

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

        map.addPolyline(new PolylineOptions().geodesic(true)
            .add(rentonPosition)
            .add(kirklandPosition)
            .add(everettPosition)
            .add(lynnwoodPosition)
            .add(montlakePosition)
            .add(kentValleyPosition)
            .add(rentonPosition)
        );

        map.addCircle(new CircleOptions().center(rentonPosition).radius(5000).strokeColor(Color.GREEN).fillColor(Color.argb(64, 0, 255, 0)));

        flyTo(SEATTLE);
    }

    private void flyTo(final CameraPosition cameraPosition) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
