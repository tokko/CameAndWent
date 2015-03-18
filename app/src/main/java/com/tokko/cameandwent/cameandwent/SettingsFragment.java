package com.tokko.cameandwent.cameandwent;

import android.app.backup.BackupManager;
import android.content.Context;
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
        new ReminderScheduler(getActivity()).scheduleWeeklyReminder();
        new ReminderScheduler(getActivity()).scheduleMonthlyReminder();
        if(getActivity().getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).getBoolean(ClockManager.PREF_CLOCKED_IN, false))
            new CountDownManager(getActivity()).startCountDown();
        if(!PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("countdown", false))
            new CountDownManager(getActivity()).stopCountDown();
        getActivity().getContentResolver().call(CameAndWentProvider.URI_MONTHS, CameAndWentProvider.RECREATE_METHOD, null, null);
        new BackupManager(getActivity()).dataChanged();
        super.onStop();
    }
}