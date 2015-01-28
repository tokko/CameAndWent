package com.tokko.cameandwent.cameandwent;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.tokko.cameandwent.GEOFENCE_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        Log.d("recvr", "Intent fired");
        int transition = event.getGeofenceTransition();
        ContentValues cv = new ContentValues();
        if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("recvr", "entered");
            Toast.makeText(context, "Entered", Toast.LENGTH_SHORT).show();
            cv.put(CameAndWentProvider.ENTERED, 1);
        }
        else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d("recvr", "exited");
            cv.put(CameAndWentProvider.ENTERED, 0);
        }
            context.getContentResolver().insert(CameAndWentProvider.URI_CAME_OR_WENT, cv);

    }
}
