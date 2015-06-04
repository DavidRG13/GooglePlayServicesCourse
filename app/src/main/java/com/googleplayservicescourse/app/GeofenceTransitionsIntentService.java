package com.googleplayservicescourse.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import java.util.ArrayList;
import java.util.List;

public class GeoFenceTransitionsIntentService extends IntentService {

    public GeoFenceTransitionsIntentService() {
        super("GeoFenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorString = GeofenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e("AQUII", errorString);
            return;
        }
        int transition = geofencingEvent.getGeofenceTransition();
        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String details = getGeofenceTransitionDetails(transition, triggeringGeofences);
            sendNotification(details);
        }

    }

    private String getGeofenceTransitionDetails(final int transition, final List<Geofence> triggeringGeofences) {
        String transitionString = getTransitionString(transition);

        ArrayList<String> strings = new ArrayList<>();
        for (Geofence triggeringGeofence : triggeringGeofences) {
            strings.add(triggeringGeofence.getRequestId());
        }

        String geofencesIds = TextUtils.join(", ", strings);
        return transitionString + ": " + geofencesIds;
    }

    private String getTransitionString(final int transition) {
        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            return getString(R.string.transition_enter);
        } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            return getString(R.string.transition_exit);
        }
        return "";
    }

    private void sendNotification(String notificationDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
            .setColor(Color.RED)
            .setContentTitle(notificationDetails)
            .setContentText(getString(R.string.geofence_transition_notification_text))
            .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
}
