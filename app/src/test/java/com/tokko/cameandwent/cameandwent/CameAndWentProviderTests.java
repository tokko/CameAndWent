package com.tokko.cameandwent.cameandwent;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowPreferenceManager;

@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class CameAndWentProviderTests extends TestCase{

    private CameAndWentProvider mProvider;
    private ContentResolver mContentResolver;
    private SharedPreferences sharedPreferences;

    @Before
    public void setup(){
        mProvider = new CameAndWentProvider();
        mContentResolver = Robolectric.application.getContentResolver();
        mProvider.onCreate();
        ShadowContentResolver.registerProvider(CameAndWentProvider.AUTHORITY, mProvider);
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext());
        sharedPreferences.edit().clear().apply();
        mProvider.seed();
    }


    @Test
    public void testMonthlySummary_CorrectData(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_MONTHLY_SUMMARY, null, null, null, CameAndWentProvider.WEEK_OF_YEAR);
        assertEquals(CameAndWentProvider.WEEKS_BACK, c.getCount());
        long duration = DateTimeConstants.MILLIS_PER_HOUR*40;
        int[] weeks = new int[52];
        for (c.moveToFirst(); !c.isLast(); c.moveToNext()){
            weeks[c.getInt(c.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR))]++;
            assertEquals(duration, c.getLong(c.getColumnIndex(CameAndWentProvider.DURATION)));
        }
        c.close();
        for (int week : weeks)
            assertFalse(weeks[week] > 1);
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
        int pre = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null).getCount();
        ContentValues cv = new ContentValues();
        long currentTime = TimeConverter.getCurrentTime().getMillis();
        cv.put(CameAndWentProvider.CAME, currentTime);
        Uri postInsertUri = mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        long id = ContentUris.parseId(postInsertUri);
        assertTrue(-1 != id);
        Cursor post = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertEquals(pre+1, post.getCount()); //new entry + break, why do this change randomly?
        post.close();
        post = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        assertNotNull(post);
        assertEquals(1, post.getCount());
        assertTrue(post.moveToFirst());
        assertEquals(0, post.getLong(post.getColumnIndex(CameAndWentProvider.WENT)));
        assertEquals(TimeConverter.extractDate(currentTime), post.getLong(post.getColumnIndex(CameAndWentProvider.DATE)));
        assertEquals(currentTime, post.getLong(post.getColumnIndex(CameAndWentProvider.CAME)));
        post.close();
    }

    @Test
    public void getWeeks_FetchesAllWeeks(){
        DateTime dt = CameAndWentProvider.getSeedDateTime();
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_WEEKS, null, null, null, CameAndWentProvider.WEEK_OF_YEAR);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext(), dt = dt.withFieldAdded(DurationFieldType.weeks(), 1))
            assertEquals(dt.getWeekOfWeekyear(), c.getInt(c.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)));
        c.close();
    }

    @Test
    public void getMonths_FetchesAllMonths(){
        DateTime dt = CameAndWentProvider.getSeedDateTime();
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_MONTHS, null, null, null, CameAndWentProvider.MONTH_OF_YEAR);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext(), dt = dt.withFieldAdded(DurationFieldType.months(), 1))
            assertEquals(dt.getMonthOfYear(), c.getInt(c.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)));
        c.close();
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
    public void testGetDurations(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_DURATIONS_PER_DAY, null, null, null, null);
        long duration = TimeConverter.hoursAsLong(8);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            assertEquals(duration, c.getLong(c.getColumnIndex(CameAndWentProvider.DURATION)));
            DateTime dt = new DateTime(c.getLong(c.getColumnIndex(CameAndWentProvider.DATE)));
            assertEquals(dt.getWeekOfWeekyear(), c.getInt(c.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)));
            assertEquals(dt.getMonthOfYear(), c.getInt(c.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)));
        }
        c.close();
    }

    @Test
    public void testGetLogEntries(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertNotNull(c);
        assertEquals(CameAndWentProvider.SEED_ENTRIES, c.getCount());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long came = c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            long went = c.getLong(c.getColumnIndex(CameAndWentProvider.WENT));
            long date = c.getLong(c.getColumnIndex(CameAndWentProvider.DATE));
            String tag = c.getString(c.getColumnIndex(CameAndWentProvider.TAG));
            assertTrue(came < went);
            assertEquals(date, TimeConverter.extractDate(came));
            assertEquals(date, TimeConverter.extractDate(went));
            assertEquals(new DateTime(went).getHourOfDay() == 17 ? "TAG2": "TAG1", tag);
        }
        c.close();
        Cursor noBreak = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(0)}, null);
        Cursor isBreak = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(1)}, null);
        assertEquals(CameAndWentProvider.SEED_ENTRIES*2/3, noBreak.getCount());
        assertEquals(noBreak.getColumnCount(), noBreak.getCount()/3, isBreak.getCount());
        noBreak.close();
        isBreak.close();
    }



    @Test
    public void testDeleteLogAllEntries(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertEquals(CameAndWentProvider.SEED_ENTRIES, c.getCount());
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_LOG_ENTRIES, null, null);
        assertEquals(c.getCount(), deleted);
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_DURATIONS, null, null, null, null);
        assertEquals(0, c.getCount());
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertEquals(0, c.getCount());
        c.close();
    }

    @Test
    public void testDeleteLogEntry(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertTrue(c.moveToFirst());
        long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_LOG_ENTRIES, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, deleted);
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null, null);
        assertNotNull(c);
        assertEquals(0, c.getCount());
        c.close();
    }

    @Test
    public void testUpdateEntry(){
        Cursor toEdit = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertNotNull(toEdit);
        assertTrue(toEdit.moveToLast());
        long id = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.ID));
        long came = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.CAME));
        long date = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.DATE));
        String tag = "TAG3";
        int intTag = 3;
        long newWent = System.currentTimeMillis() + 10000;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, newWent);
        cv.put(CameAndWentProvider.TAG, intTag);
        int updated = mContentResolver.update(CameAndWentProvider.URI_LOG_ENTRIES, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);

        Cursor afterUpdate = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        assertNotNull(afterUpdate);
        assertEquals(1, afterUpdate.getCount());
        assertTrue(afterUpdate.moveToFirst());
        assertEquals(id, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.ID)));
        assertEquals(came, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.CAME)));
        assertEquals(date, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.DATE)));
        assertEquals(newWent, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.WENT)));
        assertEquals(tag, afterUpdate.getString(afterUpdate.getColumnIndex(CameAndWentProvider.TAG)));
        toEdit.close();
        afterUpdate.close();
    }

    @Test
    public void testGetLogEntriesWhenTagsAreEmpty_TagIsNull(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_TAGS, null, null);
        assertEquals(5, deleted);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertNotNull(c);
        assertEquals(c.getCount(), c1.getCount());
        for(assertTrue(c.moveToFirst()); !c.isAfterLast(); c.moveToNext())
            assertNull(c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        c.close();
        c1.close();
    }

    @Test
    public void testGetLogEntriesWhenNoTagsConnected_ReturnsProperData(){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.TAG, -1);
        int updated = mContentResolver.update(CameAndWentProvider.URI_LOG_ENTRIES, cv, null, null);
        assertEquals(CameAndWentProvider.SEED_ENTRIES, updated);
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        assertEquals(CameAndWentProvider.SEED_ENTRIES, c.getCount());
        for(assertTrue(c.moveToFirst()); !c.isAfterLast(); c.moveToNext()){
            assertEquals(null, c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        }
        c.close();
    }
    @Test
    public void updateTimeTable_AllFieldsUpdated(){
        Cursor toEdit = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT, null, null, null, null);
        assertNotNull(toEdit);
        assertTrue(toEdit.moveToLast());
        long id = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.ID));
        DateTime dt = new DateTime(toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.DATE)));
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.DATE, dt.withFieldAdded(DurationFieldType.months(), 1).getMillis());
        int updated = mContentResolver.update(CameAndWentProvider.URI_LOG_ENTRIES, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);
        toEdit.close();

        Cursor post = mContentResolver.query(CameAndWentProvider.URI_DURATIONS, null,String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(dt.getMillis())}, null, null );
        assertNotNull(post);
        assertEquals(2, post.getCount());
        for(assertTrue(post.moveToFirst()); !post.isAfterLast(); post.moveToNext()) {
            assertEquals(dt.getWeekOfWeekyear(), post.getInt(post.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)));
            assertEquals(dt.getMonthOfYear(), post.getInt(post.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)));
            assertEquals(dt.getMillis(), post.getLong(post.getColumnIndex(CameAndWentProvider.DATE)));
        }
        post.close();
    }

    @Test
    public void testAutomaticBreaks(){
        sharedPreferences.edit()
                .putBoolean("breaks_enabled", true)
                .putString("average_break_start", "12:00")
                .putString("average_break_duration", "00:30")
                .apply();
        mContentResolver.delete(CameAndWentProvider.URI_LOG_ENTRIES, null, null);
        long dTime = TimeConverter.getCurrentTime().withTime(12, 0, 0, 0).getMillis();
        long duration = DateTimeConstants.MILLIS_PER_HOUR/2;

        ContentValues cv = new ContentValues();
        long came = System.currentTimeMillis();
        cv.put(CameAndWentProvider.CAME, came);
        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);

        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(TimeConverter.getCurrentTime().getMillis()))},  CameAndWentProvider.ISBREAK + " DESC");

        assertNotNull(c);
        assertEquals(2, c.getCount());
        assertTrue(c.moveToFirst());
        assertTrue(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1);
        assertEquals(dTime, c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)));
        assertEquals(dTime+duration, c.getLong(c.getColumnIndex(CameAndWentProvider.WENT)));

        assertTrue(c.moveToNext());
        assertFalse(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1);
        assertEquals(came, c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)));

        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(TimeConverter.getCurrentTime().getMillis()))},  CameAndWentProvider.CAME + " DESC");
        assertNotNull(c);
        assertEquals(3, c.getCount());
        c.close();
    }
	
	@Test
	public void snapup_durationsAreProperlySnappedUp(){
		ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application.getApplicationContext()).edit().putBoolean("use_snapup", true).apply();
        mProvider.recreateDurationsView();
        mProvider.seed();
		Cursor c = mContentResolver.query(CameAndWentProvider.URI_DURATIONS, null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long duration = c.getLong(c.getColumnIndex(CameAndWentProvider.DURATION));
			assertTrue(duration%(DateTimeConstants.MILLIS_PER_HOUR/2) == 0);
		}
        c.close();
	}

    @Test
    public void getTags(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        assertNotNull(c);
        assertEquals(5, c.getCount());
        assertEquals(4, c.getColumnNames().length);
        assertTrue(c.getColumnIndex(CameAndWentProvider.ID) > -1);
        assertTrue(c.getColumnIndex(CameAndWentProvider.TAG) > -1);
        assertTrue(c.getColumnIndex(CameAndWentProvider.LONGITUDE) > -1);
        assertTrue(c.getColumnIndex(CameAndWentProvider.LATITUDE) > -1);
        int suffix = 1;
        String tag = "TAG";
        for (assertTrue(c.moveToFirst()); !c.isAfterLast(); c.moveToNext()){
            assertEquals(tag + suffix++, c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
            assertEquals(-1D, c.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE)));
            assertEquals(-1D, c.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE)));
        }
        c.close();
    }

    @Test
    public void getTags_Selection(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.TAG), new String[]{"TAG2"}, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        assertEquals(4, c.getColumnNames().length);
        assertTrue(c.getColumnIndex(CameAndWentProvider.ID) > -1);
        assertTrue(c.getColumnIndex(CameAndWentProvider.TAG) > -1);
        assertTrue(c.getColumnIndex(CameAndWentProvider.LONGITUDE) > -1);
        assertTrue(c.getColumnIndex(CameAndWentProvider.LATITUDE) > -1);
        assertTrue(c.moveToFirst());
        assertEquals("TAG2", c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        c.close();
    }

    @Test
    public void getTags_Projection(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, new String[]{CameAndWentProvider.TAG}, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getColumnNames().length);
        assertEquals(-1, c.getColumnIndex(CameAndWentProvider.ID));
        assertEquals(0, c.getColumnIndex(CameAndWentProvider.TAG));
        assertEquals(-1, c.getColumnIndex(CameAndWentProvider.LONGITUDE));
        assertEquals(-1, c.getColumnIndex(CameAndWentProvider.LATITUDE));
        c.close();
    }

    @Test
    public void getTags_Sortorder(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, CameAndWentProvider.TAG + " DESC");
        assertNotNull(c);
        int suffix = 5;
        String tag = "TAG";
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            assertEquals(tag + suffix--, c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        }
        c.close();
    }

    @Test
    public void createTag(){
        String tag = "bananfisk2000";
        double longitude = 2.34;
        double latitude = 3.56;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.TAG, tag);
        cv.put(CameAndWentProvider.LONGITUDE, longitude);
        cv.put(CameAndWentProvider.LATITUDE, latitude);

        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        assertEquals(5, c.getCount());
        Uri uri = mContentResolver.insert(CameAndWentProvider.URI_TAGS, cv);

        long id = ContentUris.parseId(uri);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        assertNotNull(c1);
        assertEquals(c.getCount()+1, c1.getCount());
        c1.close();

        c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        assertTrue(c1.moveToFirst());
        assertEquals(tag, c1.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        assertEquals(longitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE)));
        assertEquals(latitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE)));
        c.close();
        c1.close();
    }

    @Test
    public void updateTag(){
        String tag = "bananfisk2000";
        double longitude;
        double latitude = 3.56;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.TAG, tag);
        cv.put(CameAndWentProvider.LATITUDE, latitude);

        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);

        c.moveToLast();
        long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
        longitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE));

        int updated = mContentResolver.update(CameAndWentProvider.URI_TAGS, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, updated);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        assertTrue(c1.moveToFirst());
        assertEquals(tag, c1.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        assertEquals(longitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE)));
        assertEquals(latitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE)));
        c.close();
        c1.close();
    }

    @Test
    public void updateTag_InvalidId_DoesNothing(){
        String tag = "bananfisk2000";
        double latitude = 3.56;
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.TAG, tag);
        cv.put(CameAndWentProvider.LATITUDE, latitude);
        int updated = mContentResolver.update(CameAndWentProvider.URI_TAGS, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(-1)});
        assertEquals(0, updated);
    }


    @Test
    public void deleteTag(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        c.moveToLast();
        long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));

        int deleted = mContentResolver.delete(CameAndWentProvider.URI_TAGS, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        assertEquals(1, deleted);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        assertEquals(c.getCount()-1, c1.getCount());
        c.close();
        c1.close();
    }

    @Test
    public void deleteTag_InvalidId_DoesNothing(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_TAGS, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(-1)});
        assertEquals(0, deleted);
        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        assertEquals(c.getCount(), c1.getCount());
        c.close();
        c1.close();
    }

    @Test
    public void getLogEntryForEdit_AlwaysReturnsOne(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT, null, null, null, null);
        assertNotNull(c);
        assertTrue(c.moveToFirst());
        assertEquals(1, c.getCount());
    }

    @Test
    public void getLogEntryForEdit_ReturnsCorrectRow(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{"1"}, null);
        assertNotNull(c);
        assertTrue(c.moveToFirst());
        assertEquals(1, c.getInt(c.getColumnIndex(CameAndWentProvider.ID)));
    }
}
