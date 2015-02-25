package com.tokko.cameandwent.cameandwent;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowPreferenceManager;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CameAndWentProviderRoboTests extends TestCase{

    private CameAndWentProvider mProvider;
    private ContentResolver mContentResolver;
    private ShadowContentResolver mShadowContentResolver;

    @Before
    public void setup(){
        mProvider = new CameAndWentProvider();
        mContentResolver = Robolectric.application.getContentResolver();
        mShadowContentResolver = Robolectric.shadowOf(mContentResolver);
        mProvider.onCreate();
        ShadowContentResolver.registerProvider(CameAndWentProvider.AUTHORITY, mProvider);
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences.edit()
                .putBoolean("breaks_enabled", true)
                .putString("average_break_start", "12:00")
                .putString("average_break_duration", "00:30")
                .apply();
        mShadowContentResolver.call(CameAndWentProvider.URI_GET_MONTHLY_SUMMARY, CameAndWentProvider.SEED_METHOD, null, null);
    }

    @Test
    public void testMonthlySummaryViewCreated(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_MONTHLY_SUMMARY, null, null, null, null);
        assertTrue(c.getCount() > 0);
        String[] names = c.getColumnNames();
        assertEquals(3, names.length);
        assertTrue(contains(names, CameAndWentProvider.ID, CameAndWentProvider.WEEK_OF_YEAR, CameAndWentProvider.DURATION));
        c.close();
    }

    @SafeVarargs
    private final <T> boolean contains(T[] arr, T... elems){
        for(T t : elems)
            if(contains(arr, t)) return true;
        return false;
    }

    private <T> boolean contains(T[] arr, T elem){
        for(T t : arr)
            if(t.equals(elem))
                return true;
        return false;
    }
}
