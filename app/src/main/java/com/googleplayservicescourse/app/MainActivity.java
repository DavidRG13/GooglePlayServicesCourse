package com.googleplayservicescourse.app;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ResultCallback<Status> {

    private GoogleApiClient googleApiClient;
    private Button requestActivityUpdates;
    private Button removeActivityUpdates;
    private TextView detectedActivities;
    private ActivityDetectionBroadcastReceiver activityDetectionBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(ActivityRecognition.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();

        activityDetectionBroadcastReceiver = new ActivityDetectionBroadcastReceiver();

        requestActivityUpdates = ((Button) findViewById(R.id.request_activity_updates));
        requestActivityUpdates.setOnClickListener(this);
        removeActivityUpdates = ((Button) findViewById(R.id.remove_activity_updates));
        removeActivityUpdates.setOnClickListener(this);
        detectedActivities = ((TextView) findViewById(R.id.activities_detected));
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(activityDetectionBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activityDetectionBroadcastReceiver);
        super.onPause();
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
        if (id == R.id.request_activity_updates) {
            if (!googleApiClient.isConnected()) {
                Toast.makeText(this, R.string.google_api_not_connected, Toast.LENGTH_LONG).show();
            }
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, Constants.DETECTION_INTERVAL, getActivityDetectionPendingIntent()).setResultCallback(this);
            requestActivityUpdates.setEnabled(false);
            removeActivityUpdates.setEnabled(true);
        } else if (id == R.id.remove_activity_updates) {
            if (!googleApiClient.isConnected()) {
                Toast.makeText(this, R.string.google_api_not_connected, Toast.LENGTH_LONG).show();
            }
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(googleApiClient, getActivityDetectionPendingIntent()).setResultCallback(this);
            requestActivityUpdates.setEnabled(true);
            removeActivityUpdates.setEnabled(false);
        }
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getActivityString(final int detectedActivity) {
        switch (detectedActivity) {
            case DetectedActivity.IN_VEHICLE:
                return getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return getString(R.string.running);
            case DetectedActivity.STILL:
                return getString(R.string.still);
            case DetectedActivity.TILTING:
                return getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return getString(R.string.walking);
            default:
                return getString(R.string.unidentifiable_activity);
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

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            ArrayList<DetectedActivity> activities = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

            String status = "";
            for (DetectedActivity activity : activities) {
                status += getActivityString(activity.getType()) + activity.getConfidence() + "%\n";
            }
            detectedActivities.setText(status);
        }
    }
}
