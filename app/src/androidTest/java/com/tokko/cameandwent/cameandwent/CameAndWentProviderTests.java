package com.tokko.cameandwent.cameandwent;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.ProviderTestCase2;

import java.util.Calendar;

public class CameAndWentProviderTests extends ProviderTestCase2<CameAndWentProvider>{
    private static final int WEEKS_BACK = 4;
    private static final int TIME_INTERVAL_HOURS = 4;
    private static final int TIMES_PER_DAY = 2;
    public CameAndWentProviderTests() {
        super(CameAndWentProvider.class, CameAndWentProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) - WEEKS_BACK);
        c.set(Calendar.MINUTE, 0);
        for (int i = 0; i < 4*7; i++){
            c.set(Calendar.HOUR_OF_DAY, 8);
            for(int j = 0; j < TIMES_PER_DAY; j++) {
                ContentValues cv = new ContentValues();
                if(j == 1)
                    increaseCalendarByHour(c, TIME_INTERVAL_HOURS);
                cv.put(CameAndWentProvider.CAME, c.getTimeInMillis());
                getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);

                increaseCalendarByHour(c, TIME_INTERVAL_HOURS);
                cv.clear();
                cv.put(CameAndWentProvider.WENT, c.getTimeInMillis());
                getMockContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);
            }
            ContentValues cv = new ContentValues();
            c.set(Calendar.HOUR_OF_DAY, 12);
            cv.put(CameAndWentProvider.CAME, c.getTimeInMillis());
            cv.put(CameAndWentProvider.ISBREAK, 1);
            getMockContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
            cv.clear();
            increaseCalendarByHour(c, 1);
            cv.put(CameAndWentProvider.WENT, c.getTimeInMillis());
            getMockContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);
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
        fail();
    }

    public void testWent(){
        fail();
    }

    public void testGetEntries(){
        fail();
    }

    public void testGetDetails(){
        Cursor c = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, null, null, null);
        assertNotNull(c);
        assertEquals((1+TIMES_PER_DAY)*WEEKS_BACK*7, c.getCount());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long came = c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            long went = c.getLong(c.getColumnIndex(CameAndWentProvider.WENT));
            long date = c.getLong(c.getColumnIndex(CameAndWentProvider.DATE));
            assertTrue(came < went);
            assertEquals(date, TimeConverter.extractDate(came));
            assertEquals(date, TimeConverter.extractDate(went));
        }
        c.close();
        c = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(0)}, null);
        Cursor c1 = getMockContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(1)}, null);
        assertEquals(c.getCount()/3, c1.getCount());
        c.close();
        c1.close();
    }

    public void testDeleteDetail(){
        fail();
    }

    public void testUpdateEntry(){
        fail();
    }
}
