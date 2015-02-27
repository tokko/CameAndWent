package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPreferenceManager;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ReminderSchedulerTest {
    private final String alarmTimeS = "12:00";
    private final int alarmDay = 5;
    private AlarmManager alarmManager;
    private ShadowAlarmManager shadowAlarmManager;

    @Before
    public void setup(){
        alarmManager = (AlarmManager) Robolectric.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Robolectric.shadowOf(alarmManager);
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences.edit()
                .putBoolean("enabled", true)
                .putBoolean("weekly_reminders", true)
                .putString("weekly_reminder_time", alarmTimeS)
                .putString("weekly_reminder_day", String.valueOf(alarmDay))
                .apply();
    }

    @Test
    public void scheduleWeeklyReminder_SchedulesAlarm(){
        Assert.assertNull(shadowAlarmManager.getNextScheduledAlarm());
        new ReminderScheduler(Robolectric.application.getApplicationContext()).scheduleWeeklyReminder();
        Assert.assertNotNull(shadowAlarmManager.getNextScheduledAlarm());
    }
}
