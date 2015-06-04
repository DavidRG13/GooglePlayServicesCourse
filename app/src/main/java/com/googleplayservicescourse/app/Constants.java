package com.googleplayservicescourse.app;

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

public class Constants {

    public static final HashMap<String, LatLng> geofencesCoordinates = new HashMap<>();
    public static final long EXPIRATION_DURATION = 60 * 60 * 1000;
    //public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km
    public static final float RADIUS = 1; // 1 mile, 1.6 km

    static {
        geofencesCoordinates.put("home", new LatLng(37.3871624, -5.9673969));
    }

}
