package com.tokko.cameandwent.cameandwent;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.RenamingDelegatingContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class CameAndWentProviderTests extends ProviderTestCase2<CameAndWentProvider>{
    private int numDetailEntries = 0;
    private int numBreaks = 0;
    private int numNoBreaks = 0;
    private CameAndWentProvider provider;

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
      // provider = new CameAndWentProvider(new CustomContext(getContext()));
        provider.call(CameAndWentProvider.SEED_METHOD, null, null);
    }


    private void increaseCalendarByHour(Calendar c, int hour){
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + hour);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCame(){
        Cursor pre = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, System.currentTimeMillis());
        Uri  postInsertUri = provider.insert(CameAndWentProvider.URI_CAME, cv);
        assertTrue(-1 != ContentUris.parseId(postInsertUri));
        Cursor post = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertEquals(pre.getCount()+1, post.getCount());
        pre.close();
        post.close();
    }

    public void testWent(){
        testCame();
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, System.currentTimeMillis());
        int updated = provider.update(CameAndWentProvider.URI_WENT, cv, null, null);
        assertEquals(1, updated);
    }

    public void testGetEntries(){
        Cursor c = provider.query(CameAndWentProvider.URI_GET_LOG_ENTRIES, null, null, null, null);
        ArrayList<Long> dates = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            dates.add(c.getLong(c.getColumnIndex(CameAndWentProvider.DATE)));
      //  assertEquals(WEEKS_BACK * 7, c.getCount());
        c.close();
    }

    public void testGetDetails(){
        Cursor c = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
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
        Cursor noBreak = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(0)}, null);
        Cursor isBreak = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(1)}, null);
        assertEquals(numNoBreaks, noBreak.getCount());
        assertEquals(numBreaks, isBreak.getCount());
        noBreak.close();
        isBreak.close();
    }

    public void testDeleteDetail(){
        int deleted = provider.delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        assertEquals(numDetailEntries, deleted);
    }

    public void testUpdateEntry(){
        Cursor toEdit = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertNotNull(toEdit);
        assertTrue(toEdit.moveToLast());
        long id = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.ID));
        long came = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.CAME));
        long date = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.DATE));

        long newWent = System.currentTimeMillis() + 10000;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, newWent);

        int updated = provider.update(CameAndWentProvider.URI_UPDATE_PARTICULAR_LOG_ENTRY, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);

        Cursor afterUpdate = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
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
        provider.delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
        long time = 1000*60*60*12;
        long dTime = TimeConverter.extractDate(System.currentTimeMillis())+1000*60*60*12;
        long duration = 1000*60*30;
       // provider.call(CameAndWentProvider.URI_GET_DETAILS, CameAndWentProvider.DROP_TRIGGER_METHOD, null, null);

        ContentValues cv = new ContentValues();
        long came = System.currentTimeMillis();
        cv.put(CameAndWentProvider.CAME, came);
        provider.insert(CameAndWentProvider.URI_CAME, cv);

        Cursor c = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(System.currentTimeMillis()))},  CameAndWentProvider.CAME + " DESC");

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

        provider.insert(CameAndWentProvider.URI_CAME, cv);
        c.close();
        c = provider.query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(System.currentTimeMillis()))},  CameAndWentProvider.CAME + " DESC");
        assertNotNull(c);
        assertEquals(3, c.getCount());
        c.close();
    }

    public void testCreateMonthlySummaryView(){
        Cursor c = provider.query(CameAndWentProvider.URI_GET_MONTHLY_SUMMARY, null, null, null, null);
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
