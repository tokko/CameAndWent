package com.tokko.cameandwent.cameandwent;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;


public class GeofenceReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    public static final String ACTION = "com.tokko.cameandwent.GEOFENCE_ACTION";
    public static final String ACTIVATE_GEOFENCE = "com.tokko.cameandwent.ACTIVATE_GEOFENCE";
    public static final String DEACTIVATE_GEOFENCE = "com.tokko.cameandwent.DEACTIVATE_GEOFENCE";

    private PendingIntent pendingIntent;
    private GoogleApiClient googleApiClient;
    private GeofencingRequest request;
    private boolean enabled;
    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, GeofenceReceiver.class).setAction(ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        if(intent.getAction().equals(ACTIVATE_GEOFENCE)){
            registerGeofence();
        }
        else if(intent.getAction().equals(DEACTIVATE_GEOFENCE)){
            registerGeofence();
        }
        else if(intent.getAction().equals(ACTION)) {
            if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enabled", true)) return;
            GeofencingEvent event = GeofencingEvent.fromIntent(intent);
            Log.d("recvr", "Intent fired");
            int transition = event.getGeofenceTransition();
            ClockManager cm = new ClockManager(context);
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d("recvr", "entered");
                Toast.makeText(context, "Entered", Toast.LENGTH_SHORT).show();
                cm.clockIn();
            }
            else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d("recvr", "exited");
                cm.clockOut();
            }
        }
        else
            throw new IllegalStateException("Unknown action for service: " + intent.getAction());
    }

    public void registerGeofence() {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sp.getBoolean("enabled", false);
        String radiuS = sp.getString("radius", null);
        //Location location = new Gson().fromJson(sp.getString("origin", null), Location.class);
     	String[] location = sp.getString("origin", "").split(";");
		if (enabled && radiuS != null && location.length == 2) {
            float radii = Float.parseFloat(radiuS);
            Geofence.Builder builder = new Geofence.Builder();
            builder.setCircularRegion(Float.parseFloat(location[0]), Float.parseFloat(location[1]), radii);
            builder.setRequestId("com.tokko.cameandwent");
            builder.setExpirationDuration(Geofence.NEVER_EXPIRE);
            builder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
            Geofence fence = builder.build();
            GeofencingRequest.Builder requestBuilder = new GeofencingRequest.Builder();
            requestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            requestBuilder.addGeofence(fence);
            request = requestBuilder.build();
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }
        else
            Toast.makeText(context, "Incomplete settings, geofence creation failed", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);
        if(enabled) {
            com.google.android.gms.common.api.PendingResult<Status> res = LocationServices.GeofencingApi.addGeofences(googleApiClient, request, pendingIntent);
            //res.setResultCallback(this);
            Toast.makeText(context, "waiting for geofence result", Toast.LENGTH_SHORT).show();
         /*   Status status = res.await();
            if(status.isSuccess())
                Toast.makeText(context, "Geofence added", Toast.LENGTH_SHORT).show();
            if(!status.isSuccess())
                Toast.makeText(context, "Geofence add failed", Toast.LENGTH_SHORT).show();
            if(status.isCanceled())
                Toast.makeText(context, "Geofence add canceled", Toast.LENGTH_SHORT).show();
            if(status.isInterrupted())
                Toast.makeText(context, "Geofence add interrupted", Toast.LENGTH_SHORT).show();
				*/
        }
     //   googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(Status status) {
        if(status.isSuccess())
            Toast.makeText(context, "Geofence added", Toast.LENGTH_SHORT).show();
        if(!status.isSuccess())
            Toast.makeText(context, "Geofence add failed", Toast.LENGTH_SHORT).show();
        if(status.isCanceled())
            Toast.makeText(context, "Geofence add canceled", Toast.LENGTH_SHORT).show();
        if(status.isInterrupted())
            Toast.makeText(context, "Geofence add interrupted", Toast.LENGTH_SHORT).show();

    }

}
