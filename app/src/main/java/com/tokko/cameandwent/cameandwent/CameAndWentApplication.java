package com.tokko.cameandwent.cameandwent;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;

public class CameAndWentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            sendBroadcast(new Intent(getApplicationContext(), GeofenceReceiver.class).setAction(GeofenceReceiver.ACTIVATE_GEOFENCE));
    }
}
