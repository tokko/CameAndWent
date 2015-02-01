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
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

public class GeofenceService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
        postNotification(0, intent.getAction());
        if(intent.getAction().equals(ACTIVATE_GEOFENCE)){
            registerGeofence();
        }
        else if(intent.getAction().equals(DEACTIVATE_GEOFENCE)){
            registerGeofence();
        }
        else if(intent.getAction().equals(ACTION)) {
            GeofencingEvent event = GeofencingEvent.fromIntent(intent);
            Log.d("recvr", "Intent fired");
            int transition = event.getGeofenceTransition();
            ClockManager cm = new ClockManager(getApplicationContext());
            postNotification(1, "GEOFENCE EVENT FUCK YEAH");
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

    private void postNotification(int id, String s) {
        Notification.Builder nb = new Notification.Builder(getApplicationContext());
        nb.setVibrate(new long[]{1000});
        nb.setContentTitle(s);
        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, nb.build());
    }

    public void registerGeofence() {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        enabled = sp.getBoolean("enabled", false);
        String radiuS = sp.getString("radius", null);
        Location location = new Gson().fromJson(sp.getString("origin", null), Location.class);
        if (enabled && radiuS != null && location != null) {
            float radii = Float.parseFloat(radiuS);
            Geofence.Builder builder = new Geofence.Builder();
            builder.setCircularRegion(location.getLatitude(), location.getLongitude(), radii);
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
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);
        if(enabled)
            LocationServices.GeofencingApi.addGeofences(googleApiClient, request, pendingIntent);
     //   googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
