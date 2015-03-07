package com.tokko.cameandwent.cameandwent;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

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
        new ReminderScheduler(getActivity()).scheduleWeeklyReminder();
        new ReminderScheduler(getActivity()).scheduleMonthlyReminder();
        getActivity().getContentResolver().call(CameAndWentProvider.URI_GET_GET_MONTHS, CameAndWentProvider.RECRETE_METHOD, null, null);
        super.onStop();
    }
}