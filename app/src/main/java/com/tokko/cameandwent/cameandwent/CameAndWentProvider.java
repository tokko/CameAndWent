package com.tokko.cameandwent.cameandwent;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

import java.util.ArrayList;

public class CameAndWentProvider extends ContentProvider {
    static final int WEEKS_BACK = 5;

    static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".CameAndWentProvider";
    private static final String URI_TEMPLATE = "content://" + AUTHORITY + "/";

    private static final String DATABASE_NAME = "cameandwent";

    private static final String TABLE_LOG_NAME = "cameandwent";
    private static final String VIEW_DURATION = "durations";

    public static final String ID = "_id";
    public static final String CAME = "came";
    public static final String WENT = "went";
    public static final String ISBREAK = "isbreak";
    public static final String WEEK_OF_YEAR = "weekofyear";
    public static final String MONTH_OF_YEAR = "monthofyear";

    public static final String DATE = "date";
    public static final String DURATION = "duration";

    private static final String ACTION_CAME = "ACTION_CAME";
    private static final String ACTION_WENT = "ACTION_WENT";
    private static final String ACTION_GET_LOG_ENTRIES = "ACTION_GET_LOG_ENTRIES";
    private static final String ACTION_DELETE_ALL = "ACTION_DELETE_ALL";
    private static final String ACTION_GET_DETAILS = "ACTION_GET_DETAILS";
    private static final String ACTION_DELETE_DETAIL = "ACTION_DELETE_DETAIL";
    private static final String ACTION_UPDATE_PARTICULAR_LOG_ENTRY = "ACTION_UPDATE_PARTICULAR_LOG_ENTRY";
    private static final String ACTION_GET_MONTHLY_SUMMARY = "ACTION_GET_MONTHLY_SUMMARY";
    private static final String ACTION_GET_WEEKS = "ACTION_GET_WEEKS";
    private static final String ACTION_GET_MONTHS = "ACTION_GET_MONTHS";

    private static final int KEY_CAME = 0;
    private static final int KEY_WENT = 1;
    private static final int KEY_GET_LOG_ENTRIES = 2;
    private static final int KEY_DELETE_ALL = 3;
    private static final int KEY_GET_DETAILS = 4;
    private static final int KEY_DELETE_DETAIL = 4;
    private static final int KEY_UPDATE_PARTICULAR_LOG_ENTRY = 5;
    private static final int KEY_GET_MONTHLY_SUMMARY = 6;
    private static final int KEY_GET_GET_WEEKS = 7;
    private static final int KEY_GET_GET_MONTHS = 8;

    public static final Uri URI_CAME = Uri.parse(URI_TEMPLATE + ACTION_CAME);
    public static final Uri URI_WENT = Uri.parse(URI_TEMPLATE + ACTION_WENT);
    public static final Uri URI_GET_LOG_ENTRIES = Uri.parse(URI_TEMPLATE + ACTION_GET_LOG_ENTRIES);
    public static final Uri URI_DELETE_ALL = Uri.parse(URI_TEMPLATE + ACTION_DELETE_ALL);
    public static final Uri URI_GET_DETAILS = Uri.parse(URI_TEMPLATE + ACTION_GET_DETAILS);
    public static final Uri URI_DELETE_DETAIL = Uri.parse(URI_TEMPLATE + ACTION_DELETE_DETAIL);
    public static final Uri URI_UPDATE_PARTICULAR_LOG_ENTRY = Uri.parse(URI_TEMPLATE + ACTION_UPDATE_PARTICULAR_LOG_ENTRY);
    public static final Uri URI_GET_MONTHLY_SUMMARY = Uri.parse(URI_TEMPLATE + ACTION_GET_MONTHLY_SUMMARY);
    public static final Uri URI_GET_GET_WEEKS = Uri.parse(URI_TEMPLATE + ACTION_GET_WEEKS);
    public static final Uri URI_GET_GET_MONTHS = Uri.parse(URI_TEMPLATE + ACTION_GET_MONTHS);

    private static UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ACTION_CAME, KEY_CAME);
        uriMatcher.addURI(AUTHORITY, ACTION_WENT, KEY_WENT);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_LOG_ENTRIES, KEY_GET_LOG_ENTRIES);
        uriMatcher.addURI(AUTHORITY, ACTION_DELETE_ALL, KEY_DELETE_ALL);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_DETAILS, KEY_GET_DETAILS);
        uriMatcher.addURI(AUTHORITY, ACTION_DELETE_DETAIL, KEY_DELETE_DETAIL);
        uriMatcher.addURI(AUTHORITY, ACTION_UPDATE_PARTICULAR_LOG_ENTRY, KEY_UPDATE_PARTICULAR_LOG_ENTRY);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_MONTHLY_SUMMARY, KEY_GET_MONTHLY_SUMMARY);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_WEEKS, KEY_GET_GET_WEEKS);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_MONTHS, KEY_GET_GET_MONTHS);
    }

    private DatabaseOpenHelper db;
    @Override
    public boolean onCreate() {
        db = new DatabaseOpenHelper(getContext());
        return true;
    }

    public static final String SEED_METHOD = "seed";
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if(method.equals(SEED_METHOD)){
            seed();
        }
        return super.call(method, arg, extras);
    }

    public static int SEED_ENTRIES = 0;
    private void seed(){
        DateTime dtNow = new DateTime();

        DateTime dt = getSeedDateTime();
        ArrayList<ContentValues> cvs = new ArrayList<>();
        for(; dt.getDayOfYear() <= dtNow.getDayOfYear(); dt = dt.withFieldAdded(DurationFieldType.days(), 1)){
            if(dt.getDayOfWeek() == DateTimeConstants.SATURDAY || dt.getDayOfWeek() == DateTimeConstants.SUNDAY) continue;
            ContentValues cv = buildSeedValues(dt, 8, 12);
            cvs.add(cv);

            cv = buildSeedValues(dt, 12, 17);
            cvs.add(cv);

            cv = buildSeedValues(dt, 12, 13);
            cv.put(ISBREAK, 1);
            cvs.add(cv);
        }
        SEED_ENTRIES = cvs.size();
        SQLiteDatabase sdb = db.getWritableDatabase();
        sdb.beginTransaction();
        sdb.delete(TABLE_LOG_NAME, null, null);
        for (ContentValues value : cvs)
            sdb.insert(TABLE_LOG_NAME, null, value);
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
    }

    static DateTime getSeedDateTime() {
        DateTime dt = new DateTime();
        dt = dt.withTime(0, 0, 0, 0);
        dt = dt.withFieldAdded(DurationFieldType.weeks(), -WEEKS_BACK+1);
        dt = dt.withDayOfWeek(DateTimeConstants.MONDAY);
        return dt;
    }

    private ContentValues buildSeedValues(DateTime dt, int firstHour, int secondHour) {
        ContentValues cv = new ContentValues();
        cv.put(DATE, TimeConverter.extractDate(dt.getMillis()));
        cv.put(WEEK_OF_YEAR, TimeConverter.extractWeek(dt.getMillis()));
        cv.put(MONTH_OF_YEAR, TimeConverter.extractMonth(dt.getMillis()));
        dt = dt.withHourOfDay(firstHour);
        cv.put(CAME, dt.getMillis());
        dt = dt.withHourOfDay(secondHour);
        cv.put(WENT, dt.getMillis());
        return cv;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor c;
        switch (uriMatcher.match(uri)){
            case KEY_GET_GET_MONTHS:
                c = sdb.query(TABLE_LOG_NAME, new String[]{ID, MONTH_OF_YEAR}, null, null, MONTH_OF_YEAR, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_GET_MONTHS);
                return c;
            case KEY_GET_GET_WEEKS:
                c = sdb.query(TABLE_LOG_NAME, new String[]{ID, WEEK_OF_YEAR}, null, null, WEEK_OF_YEAR, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_GET_WEEKS);
                return c;
            case KEY_GET_LOG_ENTRIES:
                c = sdb.query(VIEW_DURATION, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_LOG_ENTRIES);
                return c;
            case KEY_GET_DETAILS:
                c = sdb.query(TABLE_LOG_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_DETAILS);
                return c;
            case KEY_GET_MONTHLY_SUMMARY:
                c = sdb.rawQuery("SELECT " + ID + ", " + WEEK_OF_YEAR  + ", SUM(" + DURATION + ") AS " + DURATION +
                        " FROM " + VIEW_DURATION + " WHERE " + MONTH_OF_YEAR + "=? GROUP BY " + WEEK_OF_YEAR + " ORDER BY " + WEEK_OF_YEAR + " ASC", selectionArgs);
                c = sdb.query(VIEW_DURATION, new String[]{ID, WEEK_OF_YEAR, "SUM(" + DURATION + ") AS " + DURATION}, selection, selectionArgs, WEEK_OF_YEAR, null, WEEK_OF_YEAR + " ASC", null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_DETAILS);
                return c;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }


    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case KEY_CAME:
                long came = values.getAsLong(CAME);
                long date = populateContentValues(values);
                long id = sdb.insert(TABLE_LOG_NAME, null, values);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                int numberOfEventsToday = query(URI_GET_DETAILS, null, String.format("%S=?", DATE), new String[]{String.valueOf(date)}, null, null).getCount();
                boolean breaksEnabled = sp.getBoolean("breaks_enabled", false);
                if(breaksEnabled && numberOfEventsToday == 1){
                    values.clear();
                    values.put(CAME, TimeConverter.hourAndMinuteToMillis(came, sp.getString("average_break_start", "0:0")));
                    values.put(WENT, values.getAsLong(CAME) + TimeConverter.timeIntervalAsLong(sp.getString("average_break_duration", "0:0")));
                    values.put(DATE, date);
                    values.put(ISBREAK, 1);
                    sdb.insert(TABLE_LOG_NAME, null, values);
                }
                if(id > -1) {
                    getContext().getContentResolver().notifyChange(URI_GET_GET_WEEKS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                    getContext().getContentResolver().notifyChange(URI_GET_DETAILS, null);
                }
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private long populateContentValues(ContentValues values) {
        long fieldData = 0;
        if(values.containsKey(CAME))
            fieldData = values.getAsLong(CAME);
        else if(values.containsKey(WENT))
            fieldData = values.getAsLong(WENT);
        else
            throw new IllegalStateException("FUCK EVERYTHING :<");
        long date = TimeConverter.extractDate(fieldData);
        values.put(DATE, date);
        values.put(WEEK_OF_YEAR, TimeConverter.extractWeek(fieldData));
        values.put(MONTH_OF_YEAR, TimeConverter.extractMonth(fieldData));
        return date;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        int deleted;
        switch (uriMatcher.match(uri)){
            case KEY_DELETE_ALL:
                deleted = sdb.delete(TABLE_LOG_NAME, null, null);
                if(deleted > 0) {
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                    getContext().getContentResolver().notifyChange(URI_GET_DETAILS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_GET_WEEKS, null);
                }
                return deleted;
            case KEY_DELETE_DETAIL:
                deleted = sdb.delete(TABLE_LOG_NAME, selection, selectionArgs);
                if(deleted > 0){
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                    getContext().getContentResolver().notifyChange(URI_GET_DETAILS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_GET_WEEKS, null);

                }
                return deleted;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        int updated;
        switch (uriMatcher.match(uri)){
            case KEY_WENT:
                 updated = sdb.update(TABLE_LOG_NAME, values, selection, selectionArgs);
                if(updated > 0) {
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                    getContext().getContentResolver().notifyChange(URI_GET_DETAILS, null);
                }
                return updated;
            case KEY_UPDATE_PARTICULAR_LOG_ENTRY:
                populateContentValues(values);
                updated = sdb.update(TABLE_LOG_NAME, values, selection, selectionArgs);
                if(updated > 0) {
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                    getContext().getContentResolver().notifyChange(URI_GET_DETAILS, null);
                }
                return updated;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 38;
        private static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOG_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER NOT NULL DEFAULT 0, " +
                WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                CAME + " INTEGER NOT NULL," +
                ISBREAK + " INTEGER NOT NULL DEFAULT 0, " +
                WENT + " INTEGER NOT NULL DEFAULT 0);";

        private static final String DURATION_CALC = "SUM(CASE (" + ISBREAK + ") WHEN 0 THEN (" + WENT + "-" + CAME + ") WHEN 1 THEN -(" + WENT + "-" + CAME +  ") END) AS " + DURATION;
        private static final String CREATE_DURATION_VIEW = "CREATE VIEW " + VIEW_DURATION + " AS SELECT " + ID + ", " + DATE + ", " + WEEK_OF_YEAR + ", " + MONTH_OF_YEAR + ", " + DURATION_CALC + " FROM " + TABLE_LOG_NAME + " GROUP BY " + DATE ;

        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE);
            createDurationView(db);
        }



        private void createDurationView(SQLiteDatabase db) {
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATION);
            db.execSQL(CREATE_DURATION_VIEW);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TRIGGER IF EXISTS break_trigger");
            Cursor c = null;
            for(int version = oldVersion; version <= newVersion; version++){
                switch (version){
                    case 16:
                        db.execSQL("ALTER TABLE " + TABLE_LOG_NAME + " ADD COLUMN " + DATE + " INTEGER NOT NULL DEFAULT 0");
                        break;
                    case 19:
                        db.execSQL("ALTER TABLE " + TABLE_LOG_NAME + " ADD COLUMN " + ISBREAK + " INTEGER NOT NULL DEFAULT 0");
                        break;
                    case 32:
                        db.execSQL("ALTER TABLE " + TABLE_LOG_NAME + " ADD COLUMN " + WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0");
                    case 33:
                        c = db.rawQuery("SELECT * FROM " + TABLE_LOG_NAME, null);
                        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                            ContentValues cv = new ContentValues();
                            cv.put(WEEK_OF_YEAR, TimeConverter.extractWeek(c.getLong(c.getColumnIndex(CAME))));
                            db.update(TABLE_LOG_NAME, cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                        }
                        c.close();
                        break;
                    case 34:
                        db.execSQL("ALTER TABLE " + TABLE_LOG_NAME + " ADD COLUMN " + MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0");
                    case 36:
                    case 37:
                    case 35:
                        c = db.rawQuery("SELECT * FROM " + TABLE_LOG_NAME, null);
                        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                            ContentValues cv = new ContentValues();
                            cv.put(MONTH_OF_YEAR, TimeConverter.extractMonth(c.getLong(c.getColumnIndex(CAME))));
                            db.update(TABLE_LOG_NAME, cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                        }
                        c.close();
                        break;
                }
            }
            if(c != null) c.close();
            onCreate(db);
        }
    }
}
