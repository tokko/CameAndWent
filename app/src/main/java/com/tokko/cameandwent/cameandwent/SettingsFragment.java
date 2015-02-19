package com.tokko.cameandwent.cameandwent;

import android.content.Intent;
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
        new ReminderScheduler(getActivity()).scheduleWeeklyReminder(PreferenceManager.getDefaultSharedPreferences(getActivity()));
        super.onStop();
    }
}