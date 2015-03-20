package com.tokko.cameandwent.cameandwent.manualclockin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.tokko.cameandwent.cameandwent.ClockManager;
import com.tokko.cameandwent.cameandwent.R;

public class GpsLocationReceiver extends BroadcastReceiver {
    private static final String ACTION_CLOCKOUT = "com.tokko.cameandwent.ACTION_CLOCKOUT_FROM_NOTIFICATION";

    public GpsLocationReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("clockoutquestion", false)) return;

        if(intent.getAction().equals("android.location.PROVIDERS_CHANGED")){
            if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enabled", false)) return;
            boolean clockedIn = context.getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).getBoolean(ClockManager.PREF_CLOCKED_IN, false);

            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!statusOfGPS){
                if(clockedIn){
                    Notification.Builder nb = new Notification.Builder(context);
                    nb.setContentTitle("Clock in?");
                    nb.setContentText("GPS switched off, clock out?");
                    nb.setSmallIcon(R.drawable.ic_launcher);
                    nb.setAutoCancel(true);
                    RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.notificationclockoutbutton);
                    rvs.setOnClickPendingIntent(R.id.clockoutnotificationbutton, PendingIntent.getBroadcast(context.getApplicationContext(), 1, new Intent(ACTION_CLOCKOUT), PendingIntent.FLAG_UPDATE_CURRENT));
                    nb.setContent(rvs);
                    ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(10, nb.build());
                }
            }
        }
        else if(intent.getAction().equals(ACTION_CLOCKOUT)){
            new ClockManager(context).clockOut();
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(10);
        }
        else
            throw new IllegalStateException("Unknown action");
    }
}
