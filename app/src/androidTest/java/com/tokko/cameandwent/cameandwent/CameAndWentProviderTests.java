package com.tokko.cameandwent.cameandwent;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class CameAndWentProviderTests extends ProviderTestCase2<CameAndWentProvider>{
    private static final int WEEKS_BACK = 1;
    private static final int TIME_INTERVAL_HOURS = 4;
    private static final int TIMES_PER_DAY = 2;

    private int numDetailEntries = 0;
    private int numBreaks = 0;
    private int numNoBreaks = 0;

    public CameAndWentProviderTests() {
        super(CameAndWentProvider.class, CameAndWentProvider.AUTHORITY);
    }

    private class CustomContext extends RenamingDelegatingContext {
        private Context context;

        public CustomContext(Context context) {
            super(context, "PREFIX.");
            this.context = context;
        }

        @Override
        public String getPackageName() {
            return "bengt";
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return new SharedPreferences() {
                @Override
                public Map<String, ?> getAll() {
                    return null;
                }

                @Override
                public String getString(String key, String defValue) {
                    if (key.equals("average_break_start"))
                        return "12:00";
                    else if (key.equals("average_break_duration"))
                        return "00:30";
                    return defValue;
                }

                @Override
                public Set<String> getStringSet(String key, Set<String> defValues) {
                    return null;
                }

                @Override
                public int getInt(String key, int defValue) {
                    return 0;
                }

                @Override
                public long getLong(String key, long defValue) {
                    return 0;
                }

                @Override
                public float getFloat(String key, float defValue) {
                    return 0;
                }

                @Override
                public boolean getBoolean(String key, boolean defValue) {
                    if (key.equals("breaks_enabled")) {
                        return true;
                    }
                    return defValue;
                }

                @Override
                public boolean contains(String key) {
                    return false;
                }

                @Override
                public Editor edit() {
                    return null;
                }

                @Override
                public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

                }

                @Override
                public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

                }

            };
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
       // CustomContext mockContext = new CustomContext(this);
        getMockContentResolver().delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) - WEEKS_BACK);
        c.set(Calendar.MINUTE, 0);
        for (int i = 0; i < WEEKS_BACK*7; i++){
            c.set(Calendar.HOUR_OF_DAY, 8);
            for(int j = 0; j < TIMES_PER_DAY; j++) {
                ContentValues cv = new ContentValues();
                if(j == 1)
                    increaseCalendarByHour(c, TIME_INTERVAL_HOURS);
                cv.put(CameAndWentProvider.CAME, c.getTimeInMillis());
                getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
                numDetailEntries++;
                numNoBreaks++;
                increaseCalendarByHour(c, TIME_INTERVAL_HOURS);
                cv.clear();
                cv.put(CameAndWentProvider.WENT, c.getTimeInMillis());
                getMockContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);
            }
            //break 1h 12-13
            ContentValues cv = new ContentValues();
            c.set(Calendar.HOUR_OF_DAY, 12);
            cv.put(CameAndWentProvider.CAME, c.getTimeInMillis());
            cv.put(CameAndWentProvider.ISBREAK, 1);
            getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
            numDetailEntries++;
            numBreaks++;
            cv.clear();
            increaseCalendarByHour(c, 1);
            cv.put(CameAndWentProvider.WENT, c.getTimeInMillis());
            getMockContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);

            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
        }
    }


    private void increaseCalendarByHour(Calendar c, int hour){
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + hour);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCame(){
        Cursor pre = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, System.currentTimeMillis());
        Uri  postInsertUri = getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
        assertTrue(-1 != ContentUris.parseId(postInsertUri));
        Cursor post = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertEquals(pre.getCount()+1, post.getCount());
        pre.close();
        post.close();
    }

    public void testWent(){
        testCame();
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, System.currentTimeMillis());
        int updated = getMockContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);
        assertEquals(1, updated);
    }

    public void testGetEntries(){
        Cursor c = getMockContentResolver().query(CameAndWentProvider.URI_GET_LOG_ENTRIES, null, null, null, null);
        ArrayList<Long> dates = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            dates.add(c.getLong(c.getColumnIndex(CameAndWentProvider.DATE)));
        assertEquals(WEEKS_BACK * 7, c.getCount());
        c.close();
    }

    public void testGetDetails(){
        Cursor c = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertNotNull(c);
        assertEquals(numDetailEntries, c.getCount());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long came = c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            long went = c.getLong(c.getColumnIndex(CameAndWentProvider.WENT));
            long date = c.getLong(c.getColumnIndex(CameAndWentProvider.DATE));
            assertTrue(came < went);
            assertEquals(date, TimeConverter.extractDate(came));
            assertEquals(date, TimeConverter.extractDate(went));
        }
        c.close();
        Cursor noBreak = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(0)}, null);
        Cursor isBreak = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(1)}, null);
        assertEquals(numNoBreaks, noBreak.getCount());
        assertEquals(numBreaks, isBreak.getCount());
        noBreak.close();
        isBreak.close();
    }

    public void testDeleteDetail(){
        int deleted = getMockContentResolver().delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        assertEquals(numDetailEntries, deleted);
    }

    public void testUpdateEntry(){
        Cursor toEdit = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertNotNull(toEdit);
        assertTrue(toEdit.moveToLast());
        long id = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.ID));
        long came = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.CAME));
        long date = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.DATE));

        long newWent = System.currentTimeMillis() + 10000;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, newWent);

        int updated = getMockContentResolver().update(CameAndWentProvider.URI_UPDATE_PARTICULAR_LOG_ENTRY, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);

        Cursor afterUpdate = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
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

    public void testAutomaticBreaks(){
        getMockContentResolver().delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        long time = 1000*60*60*12;
        long dTime = TimeConverter.extractDate(System.currentTimeMillis())+1000*60*60*12;
        long duration = 1000*60*30;
       // getMockContentResolver().call(CameAndWentProvider.URI_GET_DETAILS, CameAndWentProvider.DROP_TRIGGER_METHOD, null, null);

        ContentValues cv = new ContentValues();
        long came = System.currentTimeMillis();
        cv.put(CameAndWentProvider.CAME, came);
        getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);

        Cursor c = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(System.currentTimeMillis()))},  CameAndWentProvider.CAME + " DESC");

      //  Cursor c = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, CameAndWentProvider.CAME + " ASC");
        assertNotNull(c);
        assertEquals(2, c.getCount());
        assertTrue(c.moveToFirst());
        assertFalse(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1);
        assertEquals(came, c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)));

        assertTrue(c.moveToNext());
        assertTrue(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1);
        assertEquals(dTime, c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)));
        assertEquals(dTime+duration, c.getLong(c.getColumnIndex(CameAndWentProvider.WENT)));

        getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
        c.close();
        c = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(System.currentTimeMillis()))},  CameAndWentProvider.CAME + " DESC");
        assertNotNull(c);
        assertEquals(3, c.getCount());
        c.close();
    }
}
