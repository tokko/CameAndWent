package com.tokko.cameandwent.cameandwent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

public class GeoFenceManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private PendingIntent pendingIntent;
    private GoogleApiClient googleApiClient;
    private GeofencingRequest request;
    private boolean enabled;
    private Context ctx;

    public GeoFenceManager(Context ctx) {
        this.ctx = ctx;
        pendingIntent = PendingIntent.getBroadcast(ctx, 0, new Intent(GeofenceReceiver.ACTION), PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public void registerGeofence() {
        PreferenceManager.setDefaultValues(ctx, R.xml.preferences, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
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
            GeofencingRequest.Builder reqquestBuilder = new GeofencingRequest.Builder();
            reqquestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
            reqquestBuilder.addGeofence(fence);
            request = reqquestBuilder.build();
            googleApiClient = new GoogleApiClient.Builder(ctx)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleApiClient.connect();
        }
    }
        @Override
        public void onConnected(Bundle bundle) {
            if(enabled)
                LocationServices.GeofencingApi.addGeofences(googleApiClient, request, pendingIntent);
            else
                LocationServices.GeofencingApi.removeGeofences(googleApiClient, pendingIntent);
            googleApiClient.disconnect();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
}
