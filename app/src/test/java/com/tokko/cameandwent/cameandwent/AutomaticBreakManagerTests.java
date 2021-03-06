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

import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

@Config(sdk = Constants.SDK_VERSION, constants = BuildConfig.class, manifest = Constants.MANIFEST_PATH)
@RunWith(RobolectricGradleTestRunner.class)
public class AutomaticBreakManagerTests {
    private Context context;
    SharedPreferences sharedPreferences;
    private final boolean[] called = new boolean[1];
    @Before
    public void setup(){
        context = RuntimeEnvironment.application.getApplicationContext();
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
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
                MatrixCursor mc = new MatrixCursor(new String[]{ID, CAME, WENT, ISBREAK, DATE, TAG});
                mc.addRow(new Object[]{1, TimeConverter.CURRENT_TIME, 0, 0, TimeConverter.extractDate(TimeConverter.CURRENT_TIME), 1});
                return mc;
            }

            @Override
            public Uri insert(Uri uri, ContentValues values) {
                called[0] = true;
                Assert.assertEquals(TimeConverter.getCurrentTime().withTime(12, 0, 0, 0).getMillis(), values.getAsLong(CAME).longValue());
                Assert.assertEquals(TimeConverter.getCurrentTime().withTime(12, 30, 0, 0).getMillis(), values.getAsLong(WENT).longValue());
                Assert.assertEquals(TimeConverter.extractDate(TimeConverter.getCurrentTime().getMillis()), values.getAsLong(DATE).longValue());
                Assert.assertEquals(1, values.getAsInteger(ISBREAK).intValue());
                Assert.assertEquals(1, values.getAsInteger(TAG).intValue());
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
    public void alarmIsScheduledAtCorrectTime_WhenCurrentTimeIsAfterBreakTime(){
        TimeConverter.CURRENT_TIME = TimeConverter.getCurrentTime().withTime(13, 0 ,0 ,0).getMillis();
        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        ShadowAlarmManager.ScheduledAlarm nextScheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertEquals(TimeConverter.getCurrentTime().withTime(12, 0, 0, 0).withFieldAdded(DurationFieldType.days(), 1).getMillis(), nextScheduledAlarm.triggerAtTime);
    }

    @Test
    public void alarmIsScheduledHasCorrectInterval(){
        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        ShadowAlarmManager.ScheduledAlarm nextScheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertEquals(DateTimeConstants.MILLIS_PER_DAY, nextScheduledAlarm.interval);
    }

    @Test
    public void alarmIsNotScheduledWhenDisabled(){
        sharedPreferences.edit().remove("breaks_enabled").apply();
        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        List<ShadowAlarmManager.ScheduledAlarm> nextScheduledAlarm = shadowAlarmManager.getScheduledAlarms();
        Assert.assertTrue(nextScheduledAlarm.isEmpty());
    }

    @Test
    public void alarmIsCanceledWhenDisabled(){
        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        ShadowAlarmManager.ScheduledAlarm nextScheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertNotNull(nextScheduledAlarm);
        sharedPreferences.edit().remove("breaks_enabled").apply();

        AutomaticBreakManager.scheduleAutomaticBreaks(context);
        nextScheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        Assert.assertNull(nextScheduledAlarm);
    }

    @Test
    public void breakIsInserted(){
        new AutomaticBreakManager().onReceive(context, null);
        Assert.assertTrue(called[0]);
    }

    @Test
    public void breakNotInsertedWhenNotClockedIn(){
        final boolean[] called = {false};
        ShadowContentResolver.registerProvider(CameAndWentProvider.AUTHORITY, new CameAndWentProvider() {
            @Override
            public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                MatrixCursor mc = new MatrixCursor(new String[]{ID, CAME, WENT, ISBREAK, DATE, TAG});
                mc.addRow(new Object[]{1, TimeConverter.CURRENT_TIME, TimeConverter.CURRENT_TIME, 0, TimeConverter.extractDate(TimeConverter.CURRENT_TIME), 1});
                return mc;
            }

            @Override
            public Uri insert(Uri uri, ContentValues values) {
                called[0] = true;
                return super.insert(uri, values);
            }
        });
        Assert.assertFalse(called[0]);
    }
}
