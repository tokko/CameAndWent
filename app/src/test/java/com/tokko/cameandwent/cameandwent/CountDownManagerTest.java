package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.tokko.cameandwent.cameandwent.clockmanager.ClockManager;
import com.tokko.cameandwent.cameandwent.notifications.CountDownManager;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowPendingIntent;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

//@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
@Config(emulateSdk = 19, constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CountDownManagerTest {
    private final DateTime currentTime = new DateTime().withTime(13, 0, 0, 0).withDate(2010, 4, 20);
    private Context context;
    private SharedPreferences sharedPreferences;
    private CountDownManager countDownManager;

    @Before
    public void setup(){
        TimeConverter.CURRENT_TIME = currentTime.getMillis();
        context = RuntimeEnvironment.application.getApplicationContext();
        RuntimeEnvironment.application.getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).edit().putBoolean(ClockManager.PREF_CLOCKED_IN, true).apply();
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear()
                .putBoolean("countdown", true)
                .putBoolean("enabled", true)
                .putString("daily_work_duration", "8:0")
                .apply();
        String auth = CameAndWentProvider.AUTHORITY;
        ShadowContentResolver.registerProvider(auth, new CameAndWentProvider(){

            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                MatrixCursor mc = new MatrixCursor(new String[]{ID, CAME, WENT, ISBREAK, DATE});
                mc.addRow(new Object[]{1, currentTime.withTime(8, 0, 0, 0).getMillis(), 0, 0, TimeConverter.extractDate(currentTime.getMillis())});
                mc.addRow(new Object[]{2, currentTime.withTime(12, 0, 0, 0).getMillis(), currentTime.withTime(13, 0, 0, 0).getMillis(), 1, TimeConverter.extractDate(currentTime.getMillis())});
                return mc;
            }

            @Override
            public Uri insert(Uri uri, ContentValues values) {
                return uri;
            }

            @Override
            public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
                return 0;
            }
        });
        countDownManager = new CountDownManager(context);
    }

    @Test
    public void receiverRegisteredForScreenOn(){

        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
    }

    @Test
    public void receiverRegisteredForScreenOff(){
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_OFF));
    }

    @Test
    public void countDownStarted_whenScreenOnAlarmIsActivated(){
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        countDownManager.startCountDown();
//        context.sendBroadcast(new Intent(Intent.ACTION_SCREEN_ON));
        List<ShadowAlarmManager.ScheduledAlarm> scheduledAlarms = shadowAlarmManager.getScheduledAlarms();
        Assert.assertEquals(1, scheduledAlarms.size());
        ShadowAlarmManager.ScheduledAlarm alarm = scheduledAlarms.get(0);
        Assert.assertEquals(DateTimeConstants.MILLIS_PER_MINUTE, alarm.interval);
        ShadowPendingIntent shadowPendingIntent = Shadows.shadowOf(alarm.operation);
        Assert.assertEquals(CountDownManager.ACTION_COUNTDOWN_TICK, shadowPendingIntent.getSavedIntent().getAction());
    }

    @Test
    public void countDownStopped_whenScreenOffAlarmIsCanceled(){
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        countDownManager.startCountDown();
        countDownManager.stopCountDown();
        List<ShadowAlarmManager.ScheduledAlarm> scheduledAlarms = shadowAlarmManager.getScheduledAlarms();
        Assert.assertEquals(0, scheduledAlarms.size());
    }

    @Test
    public void receiverRegisteredForActionCountDownTick(){
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertTrue(assertHasReceiverForIntent(CountDownManager.ACTION_COUNTDOWN_TICK));
    }

    public <T> boolean assertBroadcastReceiverRegistered(Class<T> c) {
        List<ShadowApplication.Wrapper> registeredReceivers = ShadowApplication.getInstance().getRegisteredReceivers();

        Assert.assertFalse(registeredReceivers.isEmpty());

        boolean receiverFound = false;
        for (ShadowApplication.Wrapper wrapper : registeredReceivers) {
            if (!receiverFound)
                receiverFound = c.getSimpleName().equals(
                        wrapper.broadcastReceiver.getClass().getSimpleName());
        }

        return receiverFound; //will be false if not found
    }

    public boolean assertHasReceiverForIntent(String action){
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent intent = new Intent(action);
        boolean firstAss = shadowApplication.hasReceiverForIntent(intent);
        List<BroadcastReceiver> receiversForIntent = shadowApplication.getReceiversForIntent(intent);
        return firstAss && 1 == receiversForIntent.size();
    }

    @Test
    public void whenCountDownDisabled_OnStartCountDown_NothingHappens(){
        sharedPreferences.edit().clear().apply();
        Assert.assertEquals(-1, countDownManager.startCountDown());
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_OFF));
        Assert.assertTrue(assertHasReceiverForIntent(CountDownManager.ACTION_COUNTDOWN_TICK));
        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);
        countDownManager.startCountDown();

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 0, manager.size());

        Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
        Assert.assertNull(notification);
    }

    @Test
    public void startCountDown_Notifies(){
        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);
        countDownManager.startCountDown();

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 1, manager.size());

        Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
        Assert.assertNotNull(notification);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        Assert.assertNotNull("Expected shadow notification object", shadowNotification);
        Assert.assertEquals("Workday countdown", shadowNotification.getContentTitle());

    }

    @Test
    public void whenClockIn_CountDownStarts(){
        Shadows.shadowOf(RuntimeEnvironment.application).getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).edit().clear().apply();

        ClockManager cm = new ClockManager(context);
        cm.clockIn(1);

        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 1, manager.size());

        Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
        Assert.assertNotNull(notification);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        Assert.assertNotNull("Expected shadow notification object", shadowNotification);
        Assert.assertEquals("Workday countdown", shadowNotification.getContentTitle());
    }

    @Test
    public void whenClockOut_CountDownStops(){
        ClockManager cm = new ClockManager(context);
        cm.clockIn(1);
        cm.clockOut();

        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 0, manager.size());

        Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
        Assert.assertNull(notification);
    }


    @Test
    public void notificationCounts(){
        NotificationManager notificationManager = (NotificationManager) RuntimeEnvironment.application.getSystemService(Context.NOTIFICATION_SERVICE);
        countDownManager.startCountDown();

        ShadowNotificationManager manager = Shadows.shadowOf(notificationManager);

        int hoursLeft = ((currentTime.getHourOfDay()-1)-8); //extra -1 for the lunch break
        long startDuration = (8-hoursLeft)*DateTimeConstants.MILLIS_PER_HOUR;
        for(int i = 0; i<hoursLeft*DateTimeConstants.MINUTES_PER_HOUR; i++){
            int remainder = hoursLeft * DateTimeConstants.MILLIS_PER_HOUR - i*DateTimeConstants.MILLIS_PER_MINUTE;
            context.sendBroadcast(new Intent(CountDownManager.ACTION_COUNTDOWN_TICK));

            Assert.assertEquals("Expected one notification", 1, manager.size());

            Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
            Assert.assertNotNull(notification);

            ShadowNotification shadowNotification = Shadows.shadowOf(notification);
            Assert.assertNotNull("Expected shadow notification object", shadowNotification);
            Assert.assertEquals("Workday countdown", shadowNotification.getContentTitle());

            int nProgress = shadowNotification.getProgress().progress;
            Assert.assertEquals(startDuration + i * DateTimeConstants.MILLIS_PER_MINUTE, nProgress, 10 * DateTimeConstants.MILLIS_PER_SECOND);

            TimeConverter.CURRENT_TIME += DateTimeConstants.MILLIS_PER_MINUTE;

        }

    }
}
