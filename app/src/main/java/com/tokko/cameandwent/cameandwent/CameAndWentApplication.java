package com.tokko.cameandwent.cameandwent;

import android.app.Application;
import android.content.Intent;

public class CameAndWentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(getApplicationContext(), GeofenceService.class).setAction(GeofenceService.ACTIVATE_GEOFENCE));

    }
}
