package com.googleplayservicescourse.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ResultCallback<Status> {

    private GoogleApiClient googleApiClient;
    private ArrayList<Geofence> geoFenceList;
    private Tracker tracker;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).startTracking();

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        geoFenceList = new ArrayList<>();
        populateGeofenceList();

        findViewById(R.id.add_geofences).setOnClickListener(this);
        findViewById(R.id.raise_exception).setOnClickListener(this);
        findViewById(R.id.daily_special).setOnClickListener(this);
        findViewById(R.id.launch_ads_activity).setOnClickListener(this);
        findViewById(R.id.launch_maps_activity).setOnClickListener(this);
        findViewById(R.id.launch_street_view_activity).setOnClickListener(this);
        findViewById(R.id.launch_sign_in_activity).setOnClickListener(this);


        TagManager tagManager = ((MyApplication) getApplication()).getTagManager();

        PendingResult<ContainerHolder> pendingResult = tagManager.loadContainerPreferFresh("GTM-M9373G", R.raw.default_container_v2);

        pendingResult.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(final ContainerHolder containerHolder) {
                if (containerHolder.getStatus().isSuccess()) {
                    containerHolder.refresh();
                    ((MyApplication) getApplication()).setContainerHolder(containerHolder);
                }
            }
        }, 2, TimeUnit.SECONDS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime = System.nanoTime();
        googleApiClient.connect();

        tracker = ((MyApplication) getApplication()).getTracker();
        tracker.setScreenName("MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
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
        long elapsedTime = System.nanoTime() - startTime;
        tracker.send(new HitBuilders.TimingBuilder()
            .setCategory("connect to play services")
            .setValue(elapsedTime).setLabel("Play services")
            .setVariable("duration").build()
        );
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
            } catch (SecurityException ignored) {
            }

            tracker.send(new HitBuilders.EventBuilder().setCategory("Geofences").setAction("Add geofences").setLabel("Default label for goal purposes").build());
        } else if (id == R.id.raise_exception) {

            try {
                throw new NullPointerException();
            } catch (Exception e) {
                tracker.send(new HitBuilders.ExceptionBuilder().setDescription(e.getMessage()).setFatal(true).build());
            }
        } else if (id == R.id.daily_special) {
            startActivity(new Intent(this, DailySpecialActivity.class));
        } else if (id == R.id.launch_ads_activity) {
            startActivity(new Intent(this, AdsActivity.class));
        } else if (id == R.id.launch_maps_activity) {
            startActivity(new Intent(this, MapActivity.class));
        } else if (id == R.id.launch_street_view_activity) {
            startActivity(new Intent(this, StreetViewActivity.class));
        } else if (id == R.id.launch_sign_in_activity) {
            startActivity(new Intent(this, SignInActivity.class));
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
