package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.tokko.cameandwent.cameandwent.automaticbreaks.AutomaticBreakManager;
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
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

@Config(emulateSdk = 19, constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class AutomaticBreakManagerTests {
    private Context context;

    @Before
    public void setup(){
        context = RuntimeEnvironment.application.getApplicationContext();
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().clear()
                .putBoolean("breaks_enabled", true)
                .putBoolean("enabled", true)
                .putString("average_break_start", "12:0")
                .putString("average_break_duration", "0:30")
                .apply();

        TimeConverter.CURRENT_TIME = TimeConverter.getCurrentTime().withDate(2010, 2, 4).withTime(8, 0, 0, 0).getMillis();
        ShadowContentResolver.registerProvider(CameAndWentProvider.AUTHORITY, new CameAndWentProvider() {

            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                MatrixCursor mc = new MatrixCursor(new String[]{ID, CAME, WENT, ISBREAK, DATE});
                mc.addRow(new Object[]{1, TimeConverter.CURRENT_TIME, 0, 0, TimeConverter.extractDate(TimeConverter.CURRENT_TIME)});
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
    }

    @Test
    public void receiverRegistered(){
        List<ShadowApplication.Wrapper> registeredReceivers = ShadowApplication.getInstance().getRegisteredReceivers();

        Assert.assertFalse(registeredReceivers.isEmpty());

        boolean receiverFound = false;
        for (ShadowApplication.Wrapper wrapper : registeredReceivers) {
            if (!receiverFound)
                receiverFound = AutomaticBreakManager.class.getSimpleName().equals(
                        wrapper.broadcastReceiver.getClass().getSimpleName());
        }
        Assert.assertTrue(receiverFound);
    }

    @Test
    public void receiverForAutomaticBreakRegistered(){
        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent intent = new Intent(AutomaticBreakManager.ACTION_AUTOMATIC_BREAK);
        boolean firstAss = shadowApplication.hasReceiverForIntent(intent);
        List<BroadcastReceiver> receiversForIntent = shadowApplication.getReceiversForIntent(intent);
        Assert.assertTrue(firstAss && 1 == receiversForIntent.size());
    }

    @Test
    public void alarmIsScheduledAtCorrectTime(){
        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        ShadowAlarmManager.ScheduledAlarm nextScheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertEquals(TimeConverter.getCurrentTime().withTime(12, 0, 0, 0).getMillis(), nextScheduledAlarm.triggerAtTime);
    }

    @Test
    public void alarmIsScheduledHasCorrectInterval(){
        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        ShadowAlarmManager.ScheduledAlarm nextScheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertEquals(DateTimeConstants.MILLIS_PER_DAY, nextScheduledAlarm.interval);
    }
}
