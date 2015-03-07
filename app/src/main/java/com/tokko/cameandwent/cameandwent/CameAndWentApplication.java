package com.tokko.cameandwent.cameandwent;

import android.app.Application;
import android.content.Intent;
import android.preference.PreferenceManager;

public class CameAndWentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        new ReminderScheduler(this).scheduleWeeklyReminder();
        new ReminderScheduler(this).scheduleMonthlyReminder();
        sendBroadcast(new Intent(getApplicationContext(), GeofenceReceiver.class).setAction(GeofenceReceiver.ACTIVATE_GEOFENCE));
        if(BuildConfig.DEBUG){
            getContentResolver().call(CameAndWentProvider.URI_GET_LOG_ENTRIES, CameAndWentProvider.SEED_METHOD, null, null);
        }
    }
}
