package com.tokko.cameandwent.cameandwent;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CountDownManagerTest {
    private final DateTime currentTime = new DateTime().withTime(13, 0, 0, 0).withDate(2010, 4, 20);
    private Context context;
    private SharedPreferences sharedPreferences;
    private CountDownManager countDownManager;

    @Before
    public void setup(){
        TimeConverter.CURRENT_TIME = currentTime.getMillis();
        context = Robolectric.application.getApplicationContext();
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear()
                .putBoolean("countdown", true)
                .putString("daily_work_duration", "8:0")
                .apply();

        ShadowContentResolver.registerProvider(CameAndWentProvider.AUTHORITY, new CameAndWentProvider(){
            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                MatrixCursor mc = new MatrixCursor(new String[]{ID, CAME, WENT, ISBREAK, DATE});
                mc.addRow(new Object[]{1, currentTime.withTime(8, 0, 0, 0).getMillis(), 0, 0, TimeConverter.extractDate(currentTime.getMillis())});
                mc.addRow(new Object[]{2, currentTime.withTime(12, 0, 0, 0).getMillis(), currentTime.withTime(13, 0, 0, 0).getMillis(), 1, TimeConverter.extractDate(currentTime.getMillis())});
                return mc;
            }
        });
        countDownManager = new CountDownManager(context);
    }

    @Test
    public void startCountDown_receiverRegisteredForScreenOn(){
        countDownManager.startCountDown();
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
    }

    @Test
    public void startCountDown_receiverRegisteredForScreenOff(){
        countDownManager.startCountDown();
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_OFF));
    }

    @Test
    public void stopCountDown_receiverRegisteredForScreenOn(){
        countDownManager.startCountDown();
        countDownManager.stopCountDown();
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
    }

    @Test
    public void stopCountDown_receiverRegisteredForScreenOff(){
        countDownManager.startCountDown();
        countDownManager.stopCountDown();
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_SCREEN_OFF));
    }

    @Test
    public void default_receiverNotRegisteredForScreenOn(){
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
    }

    @Test
    public void default_receiverNotRegisteredForScreenOff(){
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_SCREEN_OFF));
    }

    @Test
    public void whenScreenOn_receiverRegisteredForTimeTick(){
        countDownManager.startCountDown();
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
        context.sendBroadcast(new Intent(Intent.ACTION_SCREEN_ON));
        assertBroadcastReceiverRegistered(CountDownManager.TickReceiver.class);
        Assert.assertTrue(assertHasReceiverForIntent(Intent.ACTION_TIME_TICK));
    }

    @Test
    public void whenScreenOff_receiverRegisteredForTimeTick(){
        countDownManager.startCountDown();
        context.sendBroadcast(new Intent(Intent.ACTION_SCREEN_ON));
        context.sendBroadcast(new Intent(Intent.ACTION_SCREEN_OFF));
        assertBroadcastReceiverRegistered(CountDownManager.class);
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_TIME_TICK));
    }

    public <T> boolean assertBroadcastReceiverRegistered(Class<T> c) {
        List<ShadowApplication.Wrapper> registeredReceivers = Robolectric.getShadowApplication().getRegisteredReceivers();

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
        ShadowApplication shadowApplication = Robolectric.getShadowApplication();
        Intent intent = new Intent(action);
        boolean firstAss = shadowApplication.hasReceiverForIntent(intent);
        List<BroadcastReceiver> receiversForIntent = shadowApplication.getReceiversForIntent(intent);
        return firstAss && 1 == receiversForIntent.size();
    }

    @Test
    public void whenCountDownDisabled_OnStartCountDown_NothingHappens(){
        sharedPreferences.edit().clear().apply();
        Assert.assertEquals(-1, countDownManager.startCountDown());
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_SCREEN_ON));
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_SCREEN_OFF));
        Assert.assertFalse(assertHasReceiverForIntent(Intent.ACTION_TIME_TICK));
        NotificationManager notificationManager = (NotificationManager) Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);
        countDownManager.startCountDown();

        ShadowNotificationManager manager = Robolectric.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 0, manager.size());

        Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
        Assert.assertNull(notification);
    }

    @Test
    public void startCountDown_Notifies(){
        NotificationManager notificationManager = (NotificationManager) Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);
        countDownManager.startCountDown();

        ShadowNotificationManager manager = Robolectric.shadowOf(notificationManager);
        Assert.assertEquals("Expected one notification", 1, manager.size());

        Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
        Assert.assertNotNull(notification);
        ShadowNotification shadowNotification = Robolectric.shadowOf(notification);
        Assert.assertNotNull("Expected shadow notification object", shadowNotification);
        Assert.assertEquals("Workday countdown", shadowNotification.getContentTitle());

    }
    @Test
    public void notificationCounts(){
        NotificationManager notificationManager = (NotificationManager) Robolectric.application.getSystemService(Context.NOTIFICATION_SERVICE);
        countDownManager.startCountDown();

        ShadowNotificationManager manager = Robolectric.shadowOf(notificationManager);

        long duration = 8* DateTimeConstants.MILLIS_PER_HOUR;
        int hoursLeft = ((currentTime.getHourOfDay()-1)-8); //extra -1 for the lunch break
        for(int i = 0; i<hoursLeft*DateTimeConstants.MINUTES_PER_HOUR; i++){
            long remainder = hoursLeft * DateTimeConstants.MILLIS_PER_HOUR - i*DateTimeConstants.MILLIS_PER_MINUTE;
            context.sendBroadcast(new Intent(Intent.ACTION_TIME_TICK));
            int progress = (int) ((double)remainder/duration)*100;
            String progressString = String.format("Time remaining: %s", TimeConverter.formatInterval(remainder));
            Assert.assertEquals("Expected one notification", 1, manager.size());

            Notification notification = manager.getNotification(CountDownManager.NOTIFICATION_ID);
            Assert.assertNotNull(notification);
            ShadowNotification shadowNotification = Robolectric.shadowOf(notification);
            Assert.assertNotNull("Expected shadow notification object", shadowNotification);
            Assert.assertEquals("Workday countdown", shadowNotification.getContentTitle());
            Assert.assertEquals(progressString, shadowNotification.getContentText());
            Assert.assertEquals(progress, shadowNotification.getProgress().progress);
            TimeConverter.CURRENT_TIME += DateTimeConstants.MILLIS_PER_MINUTE;

        }

    }
}
