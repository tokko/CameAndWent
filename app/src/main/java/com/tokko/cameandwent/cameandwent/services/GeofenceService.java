package com.tokko.cameandwent.cameandwent.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.inject.Inject;
import com.tokko.cameandwent.cameandwent.clockmanager.ClockManager;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

import java.util.List;

import roboguice.service.RoboIntentService;


public class GeofenceService extends RoboIntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String ACTION = "GEOFENCE_ACTION";
    public static final String ACTIVATE_GEOFENCE = "ACTIVATE_GEOFENCE";
    public static final String DEACTIVATE_GEOFENCE = "DEACTIVATE_GEOFENCE";
    public static final String DELAYED_DEACTIVATION = "DELAYED_DEACTIVATION";
    public static final String EXTRA_ID = "EXTRA_ID";

    private GoogleApiClient googleApiClient;
    @Inject ClockManager cm;

    public GeofenceService() {
        super("GeofenceService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), GeofenceService.class).setAction(DELAYED_DEACTIVATION);
        PendingIntent delayedClockoutIntent = PendingIntent.getService(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
       // ClockManager cm = new ClockManager(getApplicationContext());
        if(intent.getAction().equals(ACTIVATE_GEOFENCE)){
            registerGeofences();
        }
        else if(intent.getAction().equals(DEACTIVATE_GEOFENCE)){
            deregisterGeofence(intent.getLongExtra(EXTRA_ID, -1));
        }
        else if(intent.getAction().equals(DELAYED_DEACTIVATION))
            clockout(getApplicationContext(), cm);

        else{// if(intent.getAction().equals(ACTION)) {
            if(!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("enabled", true)) return;
            final GeofencingEvent event = GeofencingEvent.fromIntent(intent);
            int transition = event.getGeofenceTransition();
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                am.cancel(delayedClockoutIntent);
                List<Geofence> triggerList = event.getTriggeringGeofences();
                if(triggerList == null) return;
                for (Geofence fence : triggerList){
                    cm.clockIn(Integer.valueOf(fence.getRequestId().split("/")[1]));
                }
            }
            else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                delayClockoutIfDelayed(am, delayedClockoutIntent);
            }
        }
    }

    private void delayClockoutIfDelayed(AlarmManager am, PendingIntent delayedClockoutIntent) {
        boolean delayed = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("delayed_clockout", false);
        int delayInMinutes = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("delayed_clockout_time", "5"));
        if(!delayed)
            clockout(getApplicationContext(), cm);
        else
            am.setExact(AlarmManager.RTC_WAKEUP, TimeConverter.getCurrentTime().withFieldAdded(DurationFieldType.minutes(), delayInMinutes).getMillis(), delayedClockoutIntent);
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

    public void registerGeofences() {
        registerGeofences(this, this);
    }

    public void registerGeofences(Context context) {
        registerGeofences(context, this);
    }
    public void registerGeofences(GoogleApiClient.ConnectionCallbacks callbacks) {
        registerGeofences(this, callbacks);
    }
    public void registerGeofences(Context context, GoogleApiClient.ConnectionCallbacks callbacks) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        boolean enabled = sp.getBoolean("enabled", false);
     	//String[] location = sp.getString("origin", "").split(";");
        if (!enabled) return;
        googleApiClient = new GoogleApiClient.Builder(context.getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    public void deregisterGeofence(final long id){
        registerGeofences(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                LocationServices.GeofencingApi.removeGeofences(googleApiClient, getPendingIntent(id));
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String radiuS = sp.getString("radius", null);
        if(radiuS != null) {
            Cursor c  = getApplicationContext().getContentResolver().query(CameAndWentProvider.URI_TAGS, null, null, null, null);
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
                PendingIntent pendingIntent = getPendingIntent(id);
                LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);

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
                    GeofencingRequest request = requestBuilder.build();
                    if(sp.getBoolean("enabled", false))
                        LocationServices.GeofencingApi.addGeofences(googleApiClient, request, pendingIntent);
                    else
                        LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);
                }
            }
            c.close();
        }
        googleApiClient.disconnect();
    }

    private PendingIntent getPendingIntent(long id) {
        return PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), GeofenceService.class).setAction(ACTION + id), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
