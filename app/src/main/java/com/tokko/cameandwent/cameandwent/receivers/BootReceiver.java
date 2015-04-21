package com.tokko.cameandwent.cameandwent.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.tokko.cameandwent.cameandwent.notifications.ReminderScheduler;

public class BootReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(context, GeofenceReceiver.class).setAction(GeofenceReceiver.ACTIVATE_GEOFENCE));
        new ReminderScheduler(context).scheduleWeeklyReminder();
        new ReminderScheduler(context).scheduleMonthlyReminder();
        Toast.makeText(context, "CameAndWent service started", Toast.LENGTH_SHORT).show();
    }
}
