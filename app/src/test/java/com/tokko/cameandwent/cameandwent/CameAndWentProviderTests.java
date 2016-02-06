package com.tokko.cameandwent.cameandwent;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowPreferenceManager;

@Config(sdk = Constants.SDK_VERSION, constants = BuildConfig.class, manifest = "/app/src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class CameAndWentProviderTests {

    private CameAndWentProvider mProvider;
    private ContentResolver mContentResolver;
    private SharedPreferences sharedPreferences;

    @Before
    public void setup(){
        mProvider = new CameAndWentProvider();
        mContentResolver = RuntimeEnvironment.application.getContentResolver();
        mProvider.onCreate();
        ShadowContentResolver.registerProvider(CameAndWentProvider.AUTHORITY, mProvider);
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.getApplicationContext());
        sharedPreferences.edit().clear().apply();
        mProvider.seed();
    }

    @Test
    public void testCame(){
        int pre = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null).getCount();
        ContentValues cv = new ContentValues();
        long currentTime = TimeConverter.getCurrentTime().getMillis();
        cv.put(CameAndWentProvider.CAME, currentTime);
        Uri postInsertUri = mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        long id = ContentUris.parseId(postInsertUri);
        Assert.assertTrue(-1 != id);
        Cursor post = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertEquals(pre + 1, post.getCount()); //new entry + break, why do this change randomly?
        post.close();
        post = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        Assert.assertNotNull(post);
        Assert.assertEquals(1, post.getCount());
        Assert.assertTrue(post.moveToFirst());
        Assert.assertEquals(0, post.getLong(post.getColumnIndex(CameAndWentProvider.WENT)));
        Assert.assertEquals(TimeConverter.extractDate(currentTime), post.getLong(post.getColumnIndex(CameAndWentProvider.DATE)));
        Assert.assertEquals(currentTime, post.getLong(post.getColumnIndex(CameAndWentProvider.CAME)));
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
        Assert.assertEquals(1, updated);
    }

    @Test
    public void testGetDurations(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_DURATIONS_PER_DAY, null, null, null, null);
        long duration = TimeConverter.hoursAsLong(8);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Assert.assertEquals(duration, c.getLong(c.getColumnIndex(CameAndWentProvider.DURATION)));
            DateTime dt = new DateTime(c.getLong(c.getColumnIndex(CameAndWentProvider.DATE)));
            Assert.assertEquals(dt.getWeekOfWeekyear(), c.getInt(c.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)));
            Assert.assertEquals(dt.getMonthOfYear(), c.getInt(c.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)));
        }
        c.close();
    }

    @Test
    public void testGetLogEntries(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertNotNull(c);
        Assert.assertEquals(CameAndWentProvider.SEED_ENTRIES, c.getCount());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long came = c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            long went = c.getLong(c.getColumnIndex(CameAndWentProvider.WENT));
            long date = c.getLong(c.getColumnIndex(CameAndWentProvider.DATE));
            String tag = c.getString(c.getColumnIndex(CameAndWentProvider.TAG));
            Assert.assertTrue(came < went);
            Assert.assertEquals(date, TimeConverter.extractDate(came));
            Assert.assertEquals(date, TimeConverter.extractDate(went));
            Assert.assertEquals(new DateTime(went).getHourOfDay() == 17 ? "TAG2" : "TAG1", tag);
        }
        c.close();
        Cursor noBreak = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(0)}, null);
        Cursor isBreak = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ISBREAK), new String[]{String.valueOf(1)}, null);
        Assert.assertEquals(CameAndWentProvider.SEED_ENTRIES * 2 / 3, noBreak.getCount());
        Assert.assertEquals(noBreak.getColumnCount(), noBreak.getCount() / 3, isBreak.getCount());
        noBreak.close();
        isBreak.close();
    }



    @Test
    public void testDeleteLogAllEntries(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertEquals(CameAndWentProvider.SEED_ENTRIES, c.getCount());
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_LOG_ENTRIES, null, null);
        Assert.assertEquals(c.getCount(), deleted);
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_DURATIONS, null, null, null, null);
        Assert.assertEquals(0, c.getCount());
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertEquals(0, c.getCount());
        c.close();
    }

    @Test
    public void testDeleteLogEntry(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertTrue(c.moveToFirst());
        long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_LOG_ENTRIES, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        Assert.assertEquals(1, deleted);
        c.close();
        c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null, null);
        Assert.assertNotNull(c);
        Assert.assertEquals(0, c.getCount());
        c.close();
    }

    @Test
    public void testUpdateEntry(){
        Cursor toEdit = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertNotNull(toEdit);
        Assert.assertTrue(toEdit.moveToLast());
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
        Assert.assertEquals(1, updated);

        Cursor afterUpdate = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        Assert.assertNotNull(afterUpdate);
        Assert.assertEquals(1, afterUpdate.getCount());
        Assert.assertTrue(afterUpdate.moveToFirst());
        Assert.assertEquals(id, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.ID)));
        Assert.assertEquals(came, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.CAME)));
        Assert.assertEquals(date, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.DATE)));
        Assert.assertEquals(newWent, afterUpdate.getLong(afterUpdate.getColumnIndex(CameAndWentProvider.WENT)));
        Assert.assertEquals(tag, afterUpdate.getString(afterUpdate.getColumnIndex(CameAndWentProvider.TAG)));
        toEdit.close();
        afterUpdate.close();
    }

    @Test
    public void testGetLogEntriesWhenTagsAreEmpty_TagIsNull(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_TAGS, null, null);
        Assert.assertEquals(5, deleted);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getCount(), c1.getCount());
        for(Assert.assertTrue(c.moveToFirst()); !c.isAfterLast(); c.moveToNext())
            Assert.assertNull(c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        c.close();
        c1.close();
    }

    @Test
    public void testGetLogEntriesWhenNoTagsConnected_ReturnsProperData(){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.TAG, -1);
        int updated = mContentResolver.update(CameAndWentProvider.URI_LOG_ENTRIES, cv, null, null);
        Assert.assertEquals(CameAndWentProvider.SEED_ENTRIES, updated);
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        Assert.assertEquals(CameAndWentProvider.SEED_ENTRIES, c.getCount());
        for(Assert.assertTrue(c.moveToFirst()); !c.isAfterLast(); c.moveToNext()){
            Assert.assertEquals(null, c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        }
        c.close();
    }
    @Test
    public void updateTimeTable_AllFieldsUpdated(){
        Cursor toEdit = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT, null, null, null, null);
        Assert.assertNotNull(toEdit);
        Assert.assertTrue(toEdit.moveToLast());
        long id = toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.ID));
        DateTime dt = new DateTime(toEdit.getLong(toEdit.getColumnIndex(CameAndWentProvider.DATE)));
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.DATE, dt.withFieldAdded(DurationFieldType.months(), 1).getMillis());
        int updated = mContentResolver.update(CameAndWentProvider.URI_LOG_ENTRIES, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        Assert.assertEquals(1, updated);
        toEdit.close();

        Cursor post = mContentResolver.query(CameAndWentProvider.URI_DURATIONS, null,String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(dt.getMillis())}, null, null );
        Assert.assertNotNull(post);
        Assert.assertEquals(2, post.getCount());
        for(Assert.assertTrue(post.moveToFirst()); !post.isAfterLast(); post.moveToNext()) {
            Assert.assertEquals(dt.getWeekOfWeekyear(), post.getInt(post.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)));
            Assert.assertEquals(dt.getMonthOfYear(), post.getInt(post.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)));
            Assert.assertEquals(dt.getMillis(), post.getLong(post.getColumnIndex(CameAndWentProvider.DATE)));
        }
        post.close();
    }

	@Test
	public void snapup_durationsAreProperlySnappedUp(){
		ShadowPreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application.getApplicationContext()).edit().putBoolean("use_snapup", true).apply();
        mProvider.recreateDurationsView();
        mProvider.seed();
		Cursor c = mContentResolver.query(CameAndWentProvider.URI_DURATIONS, null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long duration = c.getLong(c.getColumnIndex(CameAndWentProvider.DURATION));
			Assert.assertTrue(duration % (DateTimeConstants.MILLIS_PER_HOUR / 2) == 0);
		}
        c.close();
	}

    @Test
    public void getTags(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        Assert.assertNotNull(c);
        Assert.assertEquals(5, c.getCount());
        Assert.assertEquals(8, c.getColumnNames().length);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.ID) > -1);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.TAG) > -1);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.LONGITUDE) > -1);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.LATITUDE) > -1);
        int suffix = 1;
        String tag = "TAG";
        for (Assert.assertTrue(c.moveToFirst()); !c.isAfterLast(); c.moveToNext()){
            Assert.assertEquals(tag + suffix++, c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
            Assert.assertEquals(-1D, c.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE)), 0);
            Assert.assertEquals(-1D, c.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE)), 0);
        }
        c.close();
    }

    @Test
    public void getTags_Selection(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.TAG), new String[]{"TAG2"}, null);
        Assert.assertNotNull(c);
        Assert.assertEquals(1, c.getCount());
        Assert.assertEquals(8, c.getColumnNames().length);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.ID) > -1);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.TAG) > -1);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.LONGITUDE) > -1);
        Assert.assertTrue(c.getColumnIndex(CameAndWentProvider.LATITUDE) > -1);
        Assert.assertTrue(c.moveToFirst());
        Assert.assertEquals("TAG2", c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        c.close();
    }

    @Test
    public void getTags_Projection(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, new String[]{CameAndWentProvider.TAG}, null, null, null);
        Assert.assertNotNull(c);
        Assert.assertEquals(1, c.getColumnNames().length);
        Assert.assertEquals(-1, c.getColumnIndex(CameAndWentProvider.ID));
        Assert.assertEquals(0, c.getColumnIndex(CameAndWentProvider.TAG));
        Assert.assertEquals(-1, c.getColumnIndex(CameAndWentProvider.LONGITUDE));
        Assert.assertEquals(-1, c.getColumnIndex(CameAndWentProvider.LATITUDE));
        c.close();
    }

    @Test
    public void getTags_Sortorder(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, CameAndWentProvider.TAG + " DESC");
        Assert.assertNotNull(c);
        int suffix = 5;
        String tag = "TAG";
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Assert.assertEquals(tag + suffix--, c.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
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
        Assert.assertEquals(5, c.getCount());
        Uri uri = mContentResolver.insert(CameAndWentProvider.URI_TAGS, cv);

        long id = ContentUris.parseId(uri);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        Assert.assertNotNull(c1);
        Assert.assertEquals(c.getCount() + 1, c1.getCount());
        c1.close();

        c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        Assert.assertTrue(c1.moveToFirst());
        Assert.assertEquals(tag, c1.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        Assert.assertEquals(longitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE)), 0);
        Assert.assertEquals(latitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE)), 0);
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
        Assert.assertEquals(1, updated);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        Assert.assertTrue(c1.moveToFirst());
        Assert.assertEquals(tag, c1.getString(c.getColumnIndex(CameAndWentProvider.TAG)));
        Assert.assertEquals(longitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE)), 0);
        Assert.assertEquals(latitude, c1.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE)), 0);
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
        Assert.assertEquals(0, updated);
    }


    @Test
    public void deleteTag(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        c.moveToLast();
        long id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));

        int deleted = mContentResolver.delete(CameAndWentProvider.URI_TAGS, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
        Assert.assertEquals(1, deleted);

        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        Assert.assertEquals(c.getCount() - 1, c1.getCount());
        c.close();
        c1.close();
    }

    @Test
    public void deleteTag_InvalidId_DoesNothing(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        int deleted = mContentResolver.delete(CameAndWentProvider.URI_TAGS, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(-1)});
        Assert.assertEquals(0, deleted);
        Cursor c1 = mContentResolver.query(CameAndWentProvider.URI_TAGS, null, null, null, null);
        Assert.assertEquals(c.getCount(), c1.getCount());
        c.close();
        c1.close();
    }

    @Test
    public void getLogEntryForEdit_AlwaysReturnsOne(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT, null, null, null, null);
        Assert.assertNotNull(c);
        Assert.assertTrue(c.moveToFirst());
        Assert.assertEquals(1, c.getCount());
    }

    @Test
    public void getLogEntryForEdit_ReturnsCorrectRow(){
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{"1"}, null);
        Assert.assertNotNull(c);
        Assert.assertTrue(c.moveToFirst());
        Assert.assertEquals(1, c.getInt(c.getColumnIndex(CameAndWentProvider.ID)));
    }

    @Test
    public void clean_RemovesDurationsShorterThanOneMinute(){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, TimeConverter.CURRENT_TIME);
        cv.put(CameAndWentProvider.WENT, TimeConverter.CURRENT_TIME + DateTimeConstants.MILLIS_PER_MINUTE - 1);
        cv.put(CameAndWentProvider.TAG, 1);
        int pre = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null).getCount();
        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        mContentResolver.insert(CameAndWentProvider.URI_CAME, cv);
        int post = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null).getCount();
        Assert.assertEquals(pre + 4, post);
        mProvider.clean();
        Cursor c = mContentResolver.query(CameAndWentProvider.URI_LOG_ENTRIES, null, null, null, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            Assert.assertFalse(c.getLong(c.getColumnIndex(CameAndWentProvider.WENT)) - c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)) < DateTimeConstants.MILLIS_PER_MINUTE);
        c.close();
    }
}
