package com.tokko.cameandwent.cameandwent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new GeoFenceManager(context).registerGeofence();
    }
}
