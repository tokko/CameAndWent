package com.tokko.cameandwent.cameandwent;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.ArrayList;

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


    @Test
    public void testCame(){
        int pre = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null).getCount();
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, System.currentTimeMillis());
        Uri postInsertUri = mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        long id = ContentUris.parseId(postInsertUri);
        assertTrue(-1 != id);
        Cursor post = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertEquals(pre+2, post.getCount()); //new entry + break
        post.close();
    }

    @Test
    public void testWent(){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, System.currentTimeMillis());
        Uri postInsertUri = mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        long id = ContentUris.parseId(postInsertUri);
        cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, System.currentTimeMillis());
        int updated = mContentResolver.update(CameAndWentProvider.URI_WENT, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);
    }

    @Test
    public void testGetEntries(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRIES, null, null, null, null);
        ArrayList<Long> dates = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            dates.add(c.getLong(c.getColumnIndex(CameAndWentProvider.DATE)));
        //  assertEquals(WEEKS_BACK * 7, c.getCount());
        c.close();
    }

    @Test
    public void testGetDetails(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertNotNull(c);
        assertEquals(CameAndWentProvider.WEEKS_BACK*7*3, c.getCount());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long came = c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            long went = c.getLong(c.getColumnIndex(CameAndWentProvider.WENT));
            long date = c.getLong(c.getColumnIndex(CameAndWentProvider.DATE));
            assertTrue(came < went);
            assertEquals(date, TimeConverter.extractDate(came));
            assertEquals(date, TimeConverter.extractDate(went));
        }
        c.close();
        Cursor noBreak = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(0)}, null);
        Cursor isBreak = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(1)}, null);
        assertEquals(CameAndWentProvider.WEEKS_BACK*7*2, noBreak.getCount());
        assertEquals(CameAndWentProvider.WEEKS_BACK*7, isBreak.getCount());
        noBreak.close();
        isBreak.close();
    }

    @Test
    public void testDeleteDetail(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertEquals(CameAndWentProvider.WEEKS_BACK*7*3, c.getCount());
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        assertEquals(CameAndWentProvider.WEEKS_BACK*7*3, deleted);
    }

    @Test
    public void testUpdateEntry(){
        Cursor toEdit = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertNotNull(toEdit);
        assertTrue(toEdit.moveToLast());
        long id = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.ID));
        long came = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.CAME));
        long date = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.DATE));

        long newWent = System.currentTimeMillis() + 10000;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, newWent);

        int updated = mContentResolver.update(CameAndWentProvider.URI_UPDATE_PARTICULAR_LOG_ENTRY, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);

        Cursor afterUpdate = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        assertNotNull(afterUpdate);
        assertEquals(1, afterUpdate.getCount());
        assertTrue(afterUpdate.moveToFirst());
        assertEquals(id, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.ID)));
        assertEquals(came, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.CAME)));
        assertEquals(date, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.DATE)));
        assertEquals(newWent, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.WENT)));
        toEdit.close();
        afterUpdate.close();
    }

    @Test
    public void testAutomaticBreaks(){
        mContentResolver.delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        long time = 1000*60*60*12;
        long dTime = TimeConverter.extractDate(System.currentTimeMillis())+1000*60*60*12;
        long duration = 1000*60*30;
        // provider.call(CameAndWentProvider.URI_GET_DETAILS, CameAndWentProvider.DROP_TRIGGER_METHOD, null, null);

        ContentValues cv = new ContentValues();
        long came = System.currentTimeMillis();
        cv.put(CameAndWentProvider.CAME, came);
        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);

        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(System.currentTimeMillis()))},  CameAndWentProvider.CAME + " DESC");

        //  Cursor c = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, CameAndWentProvider.CAME + " ASC");
        assertNotNull(c);
        assertEquals(2, c.getCount());
        assertTrue(c.moveToFirst());
        assertFalse(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1);
        assertEquals(came, c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)));

        assertTrue(c.moveToNext());
        assertTrue(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1);
        assertEquals(dTime, c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)));
        assertEquals(dTime+duration, c.getLong(c.getColumnIndex(CameAndWentProvider.WENT)));

        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(System.currentTimeMillis()))},  CameAndWentProvider.CAME + " DESC");
        assertNotNull(c);
        assertEquals(3, c.getCount());
        c.close();
    }
}
