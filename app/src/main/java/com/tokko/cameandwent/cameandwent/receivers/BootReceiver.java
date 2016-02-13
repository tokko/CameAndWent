package com.tokko.cameandwent.cameandwent.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.tokko.cameandwent.cameandwent.notifications.ReminderScheduler;
import com.tokko.cameandwent.cameandwent.services.GeofenceService;

public class BootReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(context, GeofenceService.class).setAction(GeofenceService.ACTIVATE_GEOFENCE));
        new ReminderScheduler(context).scheduleWeeklyReminder();
        new ReminderScheduler(context).scheduleMonthlyReminder();
        Toast.makeText(context, "CameAndWent service started", Toast.LENGTH_SHORT).show();
    }
}
