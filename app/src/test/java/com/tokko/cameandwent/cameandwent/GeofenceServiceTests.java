package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.location.Geofence;
import com.google.inject.AbstractModule;
import com.tokko.cameandwent.cameandwent.clockmanager.ClockManager;
import com.tokko.cameandwent.cameandwent.services.GeofenceService;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;

import roboguice.RoboGuice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Config(sdk = Constants.SDK_VERSION, constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class GeofenceServiceTests {

    private SharedPreferences sharedPreferences;
    private GeofenceServiceMock service;
        private ClockManager mockClockManager;
    private Intent exitIntent;
    private Intent enterIntent;

    @Before
    public void setup(){
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.getApplicationContext());
        sharedPreferences.edit().clear()
                .putBoolean("enabled", true)
                .apply();
        mockClockManager = mock(ClockManager.class);
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(ClockManager.class).toInstance(mockClockManager);
            }
        });
        service = new GeofenceServiceMock();
        RoboGuice.injectMembers(RuntimeEnvironment.application, service);
        String transitionKey = "com.google.android.location.intent.extra.transition";
        exitIntent = new Intent(RuntimeEnvironment.application.getApplicationContext(), GeofenceService.class).setAction(GeofenceService.ACTION).putExtra(transitionKey, Geofence.GEOFENCE_TRANSITION_EXIT);
        enterIntent = new Intent(RuntimeEnvironment.application.getApplicationContext(), GeofenceService.class).setAction(GeofenceService.ACTION).putExtra(transitionKey, Geofence.GEOFENCE_TRANSITION_ENTER);
    }

    @Test
    public void clockoutOnGeofenceExit(){

        service.onHandleIntent(exitIntent);
        verify(mockClockManager).clockOut();
    }

    @Test
    public void delayedClockout_AlarmIsScheduled(){
        sharedPreferences.edit().putBoolean("delayed_clockout", true).apply();
        TimeConverter.CURRENT_TIME = new DateTime(2010, 3, 14, 13, 45, 1).getMillis();
        service.onHandleIntent(exitIntent);
        verify(mockClockManager, never()).clockOut();

        ShadowAlarmManager.ScheduledAlarm nextAlarm = getScheduledAlarms().get(0);
        Assert.assertEquals(TimeConverter.CURRENT_TIME + 5* DateTimeConstants.MILLIS_PER_MINUTE, nextAlarm.triggerAtTime);
        Assert.assertEquals(0, nextAlarm.interval);
        ShadowPendingIntent spi = Shadows.shadowOf(nextAlarm.operation);
        Assert.assertEquals(GeofenceService.DELAYED_DEACTIVATION, spi.getSavedIntent().getAction());
    }

    private List<ShadowAlarmManager.ScheduledAlarm> getScheduledAlarms() {
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        return shadowAlarmManager.getScheduledAlarms();
    }

    @Test
    public void delayedClockout_ClockInCancelsAlarm(){
        delayedClockout_AlarmIsScheduled();
        service.onHandleIntent(enterIntent);
        List<ShadowAlarmManager.ScheduledAlarm> scheduledAlarms = getScheduledAlarms();
        Assert.assertEquals(0, scheduledAlarms.size());
    }

    @Test
    public void delayedAction_Clocksout(){
        Intent delayedIntent = new Intent(GeofenceService.DELAYED_DEACTIVATION);
        service.onHandleIntent(delayedIntent);
        verify(mockClockManager).clockOut();
    }

    private class GeofenceServiceMock extends GeofenceService{

        @Override
        public void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }
}
