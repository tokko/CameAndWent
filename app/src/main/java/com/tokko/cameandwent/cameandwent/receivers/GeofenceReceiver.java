package com.tokko.cameandwent.cameandwent.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
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
import com.tokko.cameandwent.cameandwent.clockmanager.ClockManager;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DurationFieldType;

import java.util.List;


public class GeofenceReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String ACTION = "GEOFENCE_ACTION";
    public static final String ACTIVATE_GEOFENCE = "ACTIVATE_GEOFENCE";
    public static final String DEACTIVATE_GEOFENCE = "DEACTIVATE_GEOFENCE";
    public static final String DELAYED_DEACTIVATION = "DELAYED_DEACTIVATION";

    private PendingIntent pendingIntent;
    private GoogleApiClient googleApiClient;
    private GeofencingRequest request;
    private boolean enabled;
    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, GeofenceReceiver.class).setAction(ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(DELAYED_DEACTIVATION);
        PendingIntent delayedClockoutIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        ClockManager cm = new ClockManager(context);
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
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d("recvr", "entered");
                am.cancel(delayedClockoutIntent);
                List<Geofence> triggerList = event.getTriggeringGeofences();
                for (Geofence fence : triggerList){
                    cm.clockIn(Integer.valueOf(fence.getRequestId().split("/")[1]));
                }
            }
            else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("delayed_clockout", true))
                    clockout(context, cm);
                else
                    am.set(AlarmManager.RTC_WAKEUP, TimeConverter.getCurrentTime().withFieldAdded(DurationFieldType.minutes(), 5).getMillis(), delayedClockoutIntent);
            }
        }
        else if(intent.getAction().equals(DELAYED_DEACTIVATION))
            clockout(context, cm);
        else
            throw new IllegalStateException("Unknown action for service: " + intent.getAction());
    }

    private void clockout(Context context, ClockManager cm) {
        Log.d("recvr", "exited");
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("clockoutquestion", false)){
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                return;
        }
        cm.clockOut();
    }

    public void registerGeofence() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sp.getBoolean("enabled", false);
        String radiuS = sp.getString("radius", null);
        if (!enabled || radiuS == null) return;
        Cursor c  = context.getContentResolver().query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
           long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
           double longitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE));
           double latitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE));
           if(longitude != -1 && latitude != -1) {
                float radii = Float.parseFloat(radiuS);
                Geofence.Builder builder = new Geofence.Builder();
                builder.setCircularRegion(latitude, longitude, radii);
                builder.setRequestId("com.tokko.cameandwent/"+id);
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
        }
        c.close();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);
        if(enabled) {
            LocationServices.GeofencingApi.addGeofences(googleApiClient, request, pendingIntent);
        }
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
