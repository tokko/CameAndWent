package com.tokko.cameandwent.cameandwent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {
    public SettingsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onStop() {
        getActivity().sendBroadcast(new Intent(getActivity(), GeofenceReceiver.class).setAction(GeofenceReceiver.ACTIVATE_GEOFENCE));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(sp.getBoolean("breaks_enabled", false)) {
            long start = TimeConverter.timeToLong(sp.getString("average_break_start", "0:0"));
            long duration = TimeConverter.timeToLong(sp.getString("average_break_duration", "0:"));
            Bundle b = new Bundle();
            b.putLong(CameAndWentProvider.RECREATE_TRIGGER_EXTRA_TIME, start);
            b.putLong(CameAndWentProvider.RECREATE_TRIGGER_EXTRA_DURATION, duration);
            getActivity().getContentResolver().call(CameAndWentProvider.URI_GET_DETAILS, CameAndWentProvider.RECREATE_TRIGGER_METHOD, null, b);
        }
        else
            getActivity().getContentResolver().call(CameAndWentProvider.URI_GET_DETAILS, CameAndWentProvider.DROP_TRIGGER_METHOD, null, null);
        super.onStop();
    }

}