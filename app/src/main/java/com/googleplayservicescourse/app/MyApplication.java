package com.googleplayservicescourse.app;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

public class MyApplication extends Application {

    private Tracker tracker;
    private TagManager tagManager;
    private ContainerHolder containerHolder;

    public void startTracking() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.track_app);
            analytics.enableAutoActivityReports(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

    public Tracker getTracker() {
        startTracking();
        return tracker;
    }

    public void setContainerHolder(final ContainerHolder containerHolder) {
        this.containerHolder = containerHolder;
    }

    public ContainerHolder getContainerHolder() {
        return containerHolder;
    }

    public TagManager getTagManager() {
        if (tagManager == null) {
            tagManager = TagManager.getInstance(this);
            tagManager.setVerboseLoggingEnabled(true);
        }
        return tagManager;
    }
}
