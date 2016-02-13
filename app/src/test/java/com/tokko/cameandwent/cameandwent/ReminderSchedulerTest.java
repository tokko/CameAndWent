package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.tokko.cameandwent.cameandwent.notifications.ReminderScheduler;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

import roboguice.RoboGuice;


@Config(sdk = Constants.SDK_VERSION, constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class ReminderSchedulerTest {
    private DateTime weeklyAlarmTime;
    private DateTime monthlyAlarmTime;
    private DateTime currentTime;

    private AlarmManager alarmManager;
    private ShadowAlarmManager shadowAlarmManager;

    @Before
    public void setup(){
        weeklyAlarmTime = new DateTime();
        weeklyAlarmTime = weeklyAlarmTime.withDate(2010, 3, 4);
        weeklyAlarmTime = weeklyAlarmTime.withTime(12, 30, 0, 0);
        monthlyAlarmTime = getMonthlyAlarmTime(weeklyAlarmTime);
        currentTime = new DateTime(weeklyAlarmTime)
                .withFieldAdded(DurationFieldType.days(), -5)
                .withFieldAdded(DurationFieldType.hours(), 3);

        TimeConverter.CURRENT_TIME = currentTime.getMillis();
        alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        shadowAlarmManager = Shadows.shadowOf(alarmManager);
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.getApplicationContext());
        sharedPreferences.edit()
                .putBoolean("enabled", true)
                .putBoolean("weekly_reminders", true)
                .putString("weekly_reminder_time", String.format("%d:%d", weeklyAlarmTime.getHourOfDay(), weeklyAlarmTime.getMinuteOfHour()))
                .putString("weekly_reminder_day", String.valueOf(weeklyAlarmTime.getDayOfWeek()))
                .putBoolean("weekly_reminder_vibrate", true)
                .putBoolean("weekly_reminder_sound", true)
                .putBoolean("monthly_reminders", true)
                .putString("monthly_reminder_time", String.format("%d:%d", monthlyAlarmTime.getHourOfDay(), monthlyAlarmTime.getMinuteOfHour()))
                .putBoolean("monthly_reminder_vibrate", true)
                .putBoolean("monthly_reminder_sound", true)
                .apply();
    }

    private DateTime getMonthlyAlarmTime(DateTime alarmTime) {
        DateTime dt =  alarmTime.dayOfMonth().withMaximumValue();
        if(dt.getDayOfWeek() == DateTimeConstants.SUNDAY) dt.withFieldAdded(DurationFieldType.days(), -2);
        if(dt.getDayOfWeek() == DateTimeConstants.SATURDAY) dt.withFieldAdded(DurationFieldType.days(), -1);
        return dt;
    }

    @After
    public void teardown(){
        RoboGuice.Util.reset();
    }

    @Test
    public void scheduleWeeklyReminder_SchedulesAlarm(){
        Assert.assertNull(shadowAlarmManager.getNextScheduledAlarm());
        new ReminderScheduler(RuntimeEnvironment.application.getApplicationContext()).scheduleWeeklyReminder();
        Assert.assertNotNull(shadowAlarmManager.getNextScheduledAlarm());
    }


    @Test
    public void scheduleWeeklyReminder_SchedulesAlarm_AtCorrectTime(){
        assertAlarm_AtCorrectTime(new ReminderScheduler(RuntimeEnvironment.application.getApplicationContext()).scheduleWeeklyReminder(), weeklyAlarmTime);
    }

    @Test
    public void scheduleWeeklyReminder_broadcastReceiverRegistered() {
        List<ShadowApplication.Wrapper> registeredReceivers = ShadowApplication.getInstance().getRegisteredReceivers();

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
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Assert.assertTrue(shadowApplication.hasReceiverForIntent(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER)));
        List<BroadcastReceiver> receiversForIntent = shadowApplication.getReceiversForIntent(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER));
        Assert.assertEquals("Expected one broadcast receiver", 1, receiversForIntent.size());
    }
    @Test
    public void scheduleWeeklyReminder_OnReceive_NotifiesUser(){
        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);

        RuntimeEnvironment.application.getApplicationContext().sendBroadcast(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER));

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 1, manager.size());

        Notification notification = manager.getNotification(ReminderScheduler.weeklyReminderNotificationId);
        Assert.assertNotNull(notification);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        Assert.assertNotNull("Expected shadow notification object", shadowNotification);
        Assert.assertEquals("Time to submit time report!", shadowNotification.getContentTitle());

    }

    @Test
    public void scheduleWeeklyReminder_OnReceive_SchedulesNewAlarm(){
        RuntimeEnvironment.application.getApplicationContext().sendBroadcast(new Intent(ReminderScheduler.ACTION_WEEKLY_REMINDER).putExtra(ReminderScheduler.EXTRA_DELAY, 1L));
        assertAlarm_AtCorrectTime(weeklyAlarmTime);
    }


    @Test
    public void scheduleMonthlyReminder_SchedulesAlarm(){
        Assert.assertNull(shadowAlarmManager.getNextScheduledAlarm());
        new ReminderScheduler(RuntimeEnvironment.application.getApplicationContext()).scheduleMonthlyReminder();
        Assert.assertNotNull(shadowAlarmManager.getNextScheduledAlarm());
    }

    @Test
    public void scheduleMonthlyReminder_SchedulesAlarm_AtCorrectTime(){
        assertAlarm_AtCorrectTime(new ReminderScheduler(RuntimeEnvironment.application.getApplicationContext()).scheduleMonthlyReminder(), monthlyAlarmTime);
    }

    private void assertAlarm_AtCorrectTime(DateTime alarmTime, int i){
        assertAlarm_AtCorrectTime(alarmTime.getMillis(), alarmTime, i);
    }
    private void assertAlarm_AtCorrectTime(DateTime alarmTime){
        assertAlarm_AtCorrectTime(alarmTime.getMillis(), alarmTime);
    }

    private void assertAlarm_AtCorrectTime(long t, DateTime alarmTime) {
        assertAlarm_AtCorrectTime(t, alarmTime, 0);
    }
    private void assertAlarm_AtCorrectTime(long t, DateTime alarmTime, int i){
        Assert.assertEquals(""+(alarmTime.getMillis() - t)/ DateTimeConstants.MILLIS_PER_HOUR, alarmTime.getMillis(), t);
        ShadowAlarmManager.ScheduledAlarm nextAlarm = shadowAlarmManager.getScheduledAlarms().get(i);
        Assert.assertNotNull(nextAlarm);
        Assert.assertEquals("" + (alarmTime.getMillis() - nextAlarm.triggerAtTime) / DateTimeConstants.MILLIS_PER_HOUR, alarmTime.getMillis(), nextAlarm.triggerAtTime);
    }
    
    @Test
    public void scheduleMonthlyReminder_hasReceiverForIntent(){
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Assert.assertTrue(shadowApplication.hasReceiverForIntent(new Intent(ReminderScheduler.ACTION_MONTHLY_REMINDER)));
        List<BroadcastReceiver> receiversForIntent = shadowApplication.getReceiversForIntent(new Intent(ReminderScheduler.ACTION_MONTHLY_REMINDER));
        Assert.assertEquals("Expected one broadcast receiver", 1, receiversForIntent.size());
    }
    @Test
    public void scheduleMonthlyReminder_OnReceive_NotifiesUser(){
        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);

        RuntimeEnvironment.application.getApplicationContext().sendBroadcast(new Intent(ReminderScheduler.ACTION_MONTHLY_REMINDER));

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 1, manager.size());

        Notification notification = manager.getNotification(ReminderScheduler.monthlyReminderNotificationId);
        Assert.assertNotNull(notification);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        Assert.assertNotNull("Expected shadow notification object", shadowNotification);
        Assert.assertEquals("Time to finalize monthly report!", shadowNotification.getContentTitle());

    }

    @Test
    public void scheduleMonthlyReminder_OnReceive_SchedulesNewAlarm(){
        RuntimeEnvironment.application.getApplicationContext().sendBroadcast(new Intent(ReminderScheduler.ACTION_MONTHLY_REMINDER).putExtra(ReminderScheduler.EXTRA_DELAY, 1L));
        assertAlarm_AtCorrectTime(monthlyAlarmTime, 1);
    }
}
