package com.tokko.cameandwent.cameandwent;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;


public class GeofenceService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    public static final String ACTION = "com.tokko.cameandwent.GEOFENCE_ACTION";
    public static final String ACTIVATE_GEOFENCE = "com.tokko.cameandwent.ACTIVATE_GEOFENCE";
    public static final String DEACTIVATE_GEOFENCE = "com.tokko.cameandwent.DEACTIVATE_GEOFENCE";

    private PendingIntent pendingIntent;
    private GoogleApiClient googleApiClient;
    private GeofencingRequest request;
    private boolean enabled;
    public GeofenceService() {
        super("GeofenceService.class");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pendingIntent = PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), GeofenceService.class).setAction(ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.getAction().equals(ACTIVATE_GEOFENCE)){
            registerGeofence();
        }
        else if(intent.getAction().equals(DEACTIVATE_GEOFENCE)){
            registerGeofence();
        }
        else if(intent.getAction().equals(ACTION)) {
            if(!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("enabled", true)) return;
            GeofencingEvent event = GeofencingEvent.fromIntent(intent);
            Log.d("recvr", "Intent fired");
            int transition = event.getGeofenceTransition();
            ClockManager cm = new ClockManager(getApplicationContext());
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d("recvr", "entered");
                Toast.makeText(getApplicationContext(), "Entered", Toast.LENGTH_SHORT).show();
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
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        
        if(enabled) {
            PendingResult<Status> res = LocationServices.GeofencingApi.addGeofences(googleApiClient, request, pendingIntent);
            res.setResultCallback(this);
        }
		else
			LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);
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
            Toast.makeText(getApplicationContext(), "Geofence added", Toast.LENGTH_SHORT).show();
        if(!status.isSuccess())
            Toast.makeText(getApplicationContext(), "Geofence add failed", Toast.LENGTH_SHORT).show();
        if(status.isCanceled())
            Toast.makeText(getApplicationContext(), "Geofence add canceled", Toast.LENGTH_SHORT).show();
        if(status.isInterrupted())
            Toast.makeText(getApplicationContext(), "Geofence add interrupted", Toast.LENGTH_SHORT).show();

    }
}
