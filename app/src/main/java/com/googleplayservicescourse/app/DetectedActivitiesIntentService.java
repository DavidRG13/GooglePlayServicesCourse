package com.googleplayservicescourse.app;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.location.ActivityRecognitionResult;
import java.util.ArrayList;

public class DetectedActivitiesIntentService extends IntentService {

    public DetectedActivitiesIntentService() {
        super("DetectedActivitiesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        ArrayList probableActivities = (ArrayList) result.getProbableActivities();

        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        localIntent.putExtra(Constants.ACTIVITY_EXTRA, probableActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
