package com.tokko.cameandwent.cameandwent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class BootReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, new Intent(context, GeofenceService.class).setAction(GeofenceService.ACTIVATE_GEOFENCE));
        Toast.makeText(context, "CameAndWent service started", Toast.LENGTH_SHORT).show();
    }
}
