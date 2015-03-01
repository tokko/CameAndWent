package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

import roboguice.RoboGuice;


@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ReminderSchedulerTest {
    private DateTime alarmTime;
    private DateTime currentTime;

    private AlarmManager alarmManager;
    private ShadowAlarmManager shadowAlarmManager;
    private long currentEpochTime;

    @Before
    public void setup(){

        alarmTime = new DateTime();
        alarmTime = alarmTime.withDate(2010, 3, 4);
        alarmTime = alarmTime.withTime(12, 30, 0, 0);
        currentTime = new DateTime(alarmTime)
                .withFieldAdded(DurationFieldType.days(), -5)
                .withFieldAdded(DurationFieldType.hours(), 3);

        currentEpochTime = (TimeConverter.CURRENT_TIME = currentTime.getMillis());
        alarmManager = (AlarmManager) Robolectric.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Robolectric.shadowOf(alarmManager);
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences.edit()
                .putBoolean("enabled", true)
                .putBoolean("weekly_reminders", true)
                .putString("weekly_reminder_time", String.format("%d:%d", alarmTime.getHourOfDay(), alarmTime.getMinuteOfHour()))
                .putString("weekly_reminder_day", String.valueOf(alarmTime.getDayOfWeek()))
                .putBoolean("weekly_reminder_vibrate", true)
                .putBoolean("weekly_reminder_sound", true)
                .apply();
    }

    @Test
    public void scheduleWeeklyReminder_SchedulesAlarm(){
        Assert.assertNull(shadowAlarmManager.getNextScheduledAlarm());
        new ReminderScheduler(Robolectric.application.getApplicationContext()).scheduleWeeklyReminder();
        Assert.assertNotNull(shadowAlarmManager.getNextScheduledAlarm());
    }

    @Test
    public void scheduleWeeklyReminder_SchedulesAlarm_AtCorrectTime(){
        Assert.assertNull(shadowAlarmManager.getNextScheduledAlarm());
        long t = new ReminderScheduler(Robolectric.application.getApplicationContext()).scheduleWeeklyReminder();
        Assert.assertEquals(""+(currentEpochTime - t)/ DateTimeConstants.MILLIS_PER_HOUR, alarmTime.getMillis(), t);
        ShadowAlarmManager.ScheduledAlarm nextAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertNotNull(nextAlarm);
        Assert.assertEquals(""+(alarmTime.getMillis() - nextAlarm.triggerAtTime)/(1000*60*60), alarmTime.getMillis(), nextAlarm.triggerAtTime);
    }

    @After
    public void teardown(){
        RoboGuice.Util.reset();
    }

    @Test
    public void testBroadcastReceiverRegistered() {
        List<ShadowApplication.Wrapper> registeredReceivers = Robolectric.getShadowApplication().getRegisteredReceivers();

        Assert.assertFalse(registeredReceivers.isEmpty());

        boolean receiverFound = false;
        for (ShadowApplication.Wrapper wrapper : registeredReceivers) {
            if (!receiverFound)
                receiverFound = ReminderScheduler.class.getSimpleName().equals(
                        wrapper.broadcastReceiver.getClass().getSimpleName());
        }

        Assert.assertTrue(receiverFound); //will be false if not found
    }

    @Test
    public void scheduleWeeklyReminder_hasReceiverForIntent(){
        ShadowApplication shadowApplication = Robolectric.getShadowApplication();
        Assert.assertTrue(shadowApplication.hasReceiverForIntent(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER)));
        List<BroadcastReceiver> receiversForIntent = shadowApplication.getReceiversForIntent(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER));
        Assert.assertEquals("Expected one broadcast receiver", 1, receiversForIntent.size());
    }
    @Test
    public void scheduleWeeklyReminder_OnReceive_NotifiesUser(){
        NotificationManager notificationManager = (NotificationManager) Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);

        Robolectric.application.getApplicationContext().sendBroadcast(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER));

        ShadowNotificationManager manager = Robolectric.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 1, manager.size());

        Notification notification = manager.getNotification(ReminderScheduler.weeklyReminderNotificationId);
        Assert.assertNotNull(notification);
        ShadowNotification shadowNotification = Robolectric.shadowOf(notification);
        Assert.assertNotNull("Expected shadow notification object", shadowNotification);
        Assert.assertEquals("Time to submit time report!", shadowNotification.getContentTitle());

    }
}
