package com.tokko.cameandwent.cameandwent;

import android.app.Application;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.tokko.cameandwent.cameandwent.automaticbreaks.AutomaticBreakManager;
import com.tokko.cameandwent.cameandwent.notifications.ReminderScheduler;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.receivers.GeofenceReceiver;

public class CameAndWentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        AutomaticBreakManager.scheduleAutomaticBreaks(this);
        new ReminderScheduler(this).scheduleWeeklyReminder();
        new ReminderScheduler(this).scheduleMonthlyReminder();
        sendBroadcast(new Intent(getApplicationContext(), GeofenceReceiver.class).setAction(GeofenceReceiver.ACTIVATE_GEOFENCE));
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                new BackupManager(CameAndWentApplication.this).dataChanged();
            }
        });
        ContentObserver obs = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                new BackupManager(CameAndWentApplication.this).dataChanged();
                super.onChange(selfChange);
            }
        };
        getContentResolver().registerContentObserver(CameAndWentProvider.URI_DURATIONS, true, obs);
        getContentResolver().registerContentObserver(CameAndWentProvider.URI_LOG_ENTRIES, true, obs);
        if(BuildConfig.DEBUG){
            getContentResolver().call(CameAndWentProvider.URI_LOG_ENTRIES, CameAndWentProvider.SEED_METHOD, null, null);
        }
    }
}
