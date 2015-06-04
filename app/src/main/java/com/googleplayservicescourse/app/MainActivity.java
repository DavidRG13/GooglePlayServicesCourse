package com.googleplayservicescourse.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ResultCallback<Status> {

    private GoogleApiClient googleApiClient;
    private ArrayList<Geofence> geoFenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        geoFenceList = new ArrayList<>();
        populateGeofenceList();

        ((Button) findViewById(R.id.add_geofences)).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(final Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(final int i) {

    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();
        if (id == R.id.add_geofences) {
            if (!googleApiClient.isConnected()) {
                Toast.makeText(this, getString(R.string.google_api_not_connected), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
                ).setResultCallback(this);
            } catch (SecurityException securityException) {
            }
        }
    }

    @Override
    public void onResult(final Status status) {
        if (status.isSuccess()) {
            Log.d("AQUII", "Successfully added or removed activity detection");
        } else {
            Log.d("AQUII", "Error adding or removing activity detection: " + status.getStatusMessage());
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeoFenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geoFenceList);
        return builder.build();
    }

    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.geofencesCoordinates.entrySet()) {
            geoFenceList.add(new Geofence.Builder().setRequestId(entry.getKey())
                .setCircularRegion(entry.getValue().latitude, entry.getValue().longitude, Constants.RADIUS)
                .setExpirationDuration(Constants.EXPIRATION_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
        }
    }
}
