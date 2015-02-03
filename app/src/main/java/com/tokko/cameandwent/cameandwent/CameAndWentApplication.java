package com.tokko.cameandwent.cameandwent;

import android.app.Application;
import android.content.Intent;

public class CameAndWentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        sendBroadcast(new Intent(getApplicationContext(), GeofenceReceiver.class).setAction(GeofenceReceiver.ACTIVATE_GEOFENCE));

    }
}
