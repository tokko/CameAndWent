package com.tokko.cameandwent.cameandwent;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment  {
    public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onStop() {
        new GeoFenceManager(getActivity()).registerGeofence();

        super.onStop();
    }


}
