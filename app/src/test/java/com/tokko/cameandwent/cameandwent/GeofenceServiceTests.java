package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.tokko.cameandwent.cameandwent.clockmanager.ClockManager;
import com.tokko.cameandwent.cameandwent.services.GeofenceService;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import roboguice.RoboGuice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Config(emulateSdk = 21, constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class GeofenceServiceTests {

    private SharedPreferences sharedPreferences;
    private GeofenceServiceMock service;
        private ClockManager mockClockManager;
    private Intent exitIntent;

    @Before
    public void setup(){
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.getApplicationContext());
        sharedPreferences.edit()
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
        int transition = Geofence.GEOFENCE_TRANSITION_EXIT;
        exitIntent = new Intent(RuntimeEnvironment.application.getApplicationContext(), GeofenceService.class).setAction(GeofenceService.ACTION).putExtra(transitionKey, transition);
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

        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        ShadowAlarmManager.ScheduledAlarm nextAlarm = shadowAlarmManager.getScheduledAlarms().get(0);
        Assert.assertEquals(TimeConverter.CURRENT_TIME + 5* DateTimeConstants.MILLIS_PER_MINUTE, nextAlarm.triggerAtTime);
        Assert.assertEquals(0, nextAlarm.interval);
        ShadowPendingIntent spi = Shadows.shadowOf(nextAlarm.operation);
        Assert.assertEquals(GeofenceService.DELAYED_DEACTIVATION, spi.getSavedIntent().getAction());
    }

    private class GeofenceServiceMock extends GeofenceService{

        @Override
        public void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }
}
