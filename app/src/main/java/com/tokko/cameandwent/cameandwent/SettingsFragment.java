package com.tokko.cameandwent.cameandwent;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingRequest;

public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
    }

    private PendingIntent pendingIntent;
    private GoogleApiClient googleApiClient;
    private GeofencingRequest request;
    private boolean enabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        pendingIntent = PendingIntent.getService(getActivity(), 0, new Intent(GeofenceService.ACTION), PendingIntent.FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onStop() {
        getActivity().startService(new Intent(getActivity(), GeofenceService.class).setAction(GeofenceService.ACTIVATE_GEOFENCE));
        super.onStop();
    }
}