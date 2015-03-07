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
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

import java.util.ArrayList;

public class CameAndWentProvider extends ContentProvider {
    static final int WEEKS_BACK = 5;

    static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".CameAndWentProvider";
    private static final String URI_TEMPLATE = "content://" + AUTHORITY + "/";

    private static final String DATABASE_NAME = "cameandwent";

    private static final String TABLE_LOG_NAME = "log";
    private static final String VIEW_DURATION = "durations";
    private static final String TIME_TABLE = "timetable";
    private static final String VIEW_TIME_TABLE_DURATIONS =  "timetabledurations";

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
    private static final String ACTION_DELETE_LOG_ENTRY = "ACTION_DELETE_LOG_ENTRY";
    private static final String ACTION_UPDATE_PARTICULAR_LOG_ENTRY = "ACTION_UPDATE_PARTICULAR_LOG_ENTRY";
    private static final String ACTION_GET_WEEKS = "ACTION_GET_WEEKS";
    private static final String ACTION_GET_MONTHS = "ACTION_GET_MONTHS";
    private static final String ACTION_GET_DURATIONS = "ACTION_GET_DURATIONS";
    private static final String ACTION_GET_MONTHLY_SUMMARY = "ACTION_GET_MONTHLY_SUMMARY";

    private static final int KEY_CAME = 0;
    private static final int KEY_WENT = 1;
    private static final int KEY_GET_LOG_ENTRIES = 2;
    private static final int KEY_DELETE_LOG_ENTRY = 4;
    private static final int KEY_UPDATE_PARTICULAR_LOG_ENTRY = 5;
    private static final int KEY_GET_GET_WEEKS = 7;
    private static final int KEY_GET_GET_MONTHS = 8;
    private static final int KEY_GET_GET_DURATIONS = 9;
    private static final int KEY_GET_GET_MONTHLY_SUMMARY = 10;

    public static final Uri URI_CAME = Uri.parse(URI_TEMPLATE + ACTION_CAME);
    public static final Uri URI_WENT = Uri.parse(URI_TEMPLATE + ACTION_WENT);
    public static final Uri URI_GET_LOG_ENTRIES = Uri.parse(URI_TEMPLATE + ACTION_GET_LOG_ENTRIES);
    public static final Uri URI_DELETE_LOG_ENTRY = Uri.parse(URI_TEMPLATE + ACTION_DELETE_LOG_ENTRY);
    public static final Uri URI_UPDATE_PARTICULAR_LOG_ENTRY = Uri.parse(URI_TEMPLATE + ACTION_UPDATE_PARTICULAR_LOG_ENTRY);
    public static final Uri URI_GET_WEEKS = Uri.parse(URI_TEMPLATE + ACTION_GET_WEEKS);
    public static final Uri URI_GET_GET_MONTHS = Uri.parse(URI_TEMPLATE + ACTION_GET_MONTHS);
    public static final Uri URI_GET_DURATIONS = Uri.parse(URI_TEMPLATE + ACTION_GET_DURATIONS);
    public static final Uri URI_GET_MONTHLY_SUMMARY = Uri.parse(URI_TEMPLATE + ACTION_GET_MONTHLY_SUMMARY);

    private static UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ACTION_CAME, KEY_CAME);
        uriMatcher.addURI(AUTHORITY, ACTION_WENT, KEY_WENT);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_LOG_ENTRIES, KEY_GET_LOG_ENTRIES);
        uriMatcher.addURI(AUTHORITY, ACTION_DELETE_LOG_ENTRY, KEY_DELETE_LOG_ENTRY);
        uriMatcher.addURI(AUTHORITY, ACTION_UPDATE_PARTICULAR_LOG_ENTRY, KEY_UPDATE_PARTICULAR_LOG_ENTRY);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_WEEKS, KEY_GET_GET_WEEKS);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_MONTHS, KEY_GET_GET_MONTHS);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_DURATIONS, KEY_GET_GET_DURATIONS);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_MONTHLY_SUMMARY, KEY_GET_GET_MONTHLY_SUMMARY);
    }

    private DatabaseOpenHelper db;
    @Override
    public boolean onCreate() {
        db = new DatabaseOpenHelper(getContext());
        return true;
    }

    public static final String SEED_METHOD = "seed";
    public static final String RECRETE_METHOD = "recreate";
    public static final String MIGRATE_METHOD = "migrate";
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if(method.equals(SEED_METHOD)){
            seed();
        }
        else if(method.equals(RECRETE_METHOD)){
            recreateDurationsView();
        }
        else if(method.equals(MIGRATE_METHOD)){
            migrateData(db.getWritableDatabase());
        }
        return super.call(method, arg, extras);
    }

    public void migrateData(SQLiteDatabase db){
        Cursor oldData = db.rawQuery("SELECT * FROM cameandwent;", null);
        Log.d("Provider", oldData.getCount()+"");
        for (oldData.moveToFirst(); !oldData.isAfterLast(); oldData.moveToNext()){
            ContentValues cv = new ContentValues();
            populateContentValuesWithTimeInfo(oldData.getLong(oldData.getColumnIndex(DATE)), cv);
            db.insert(TIME_TABLE, null, cv);
            cv.clear();
            cv.put(DATE, oldData.getLong(oldData.getColumnIndex(DATE)));
            cv.put(CAME, oldData.getLong(oldData.getColumnIndex(CAME)));
            cv.put(WENT, oldData.getLong(oldData.getColumnIndex(WENT)));
            cv.put(ISBREAK, oldData.getLong(oldData.getColumnIndex(ISBREAK)));
            db.insert(TABLE_LOG_NAME, null, cv);
        }
        oldData.close();
        getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
        getContext().getContentResolver().notifyChange(URI_GET_WEEKS, null);
        getContext().getContentResolver().notifyChange(URI_GET_DURATIONS, null);
        getContext().getContentResolver().notifyChange(URI_GET_GET_MONTHS, null);
    }

    public void recreateDurationsView() {
        db.recreateDurationsView(db.getWritableDatabase());
    }

    public static int SEED_ENTRIES = 0;
    public void seed(){
        Log.d("Provider", "Seeding");
        DateTime dtNow = new DateTime();

        DateTime dt = getSeedDateTime();
        ArrayList<ContentValues> logEntries = new ArrayList<>();
        ArrayList<ContentValues> timeTables = new ArrayList<>();
        for(; dt.getDayOfYear() <= dtNow.getDayOfYear(); dt = dt.withFieldAdded(DurationFieldType.days(), 1)){
            if(dt.getDayOfWeek() == DateTimeConstants.SATURDAY || dt.getDayOfWeek() == DateTimeConstants.SUNDAY) continue;
            ContentValues cv = new ContentValues();
            cv.put(DATE, TimeConverter.extractDate(dt.getMillis()));
            cv.put(WEEK_OF_YEAR, TimeConverter.extractWeek(dt.getMillis()));
            cv.put(MONTH_OF_YEAR, TimeConverter.extractMonth(dt.getMillis()));
            timeTables.add(cv);

            cv = buildSeedValues(dt, 8, 12);
            logEntries.add(cv);

            boolean useSnapup = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("use_snapup", false);
            cv = buildSeedValues(dt, 12, 17, useSnapup?15:0);
            logEntries.add(cv);

            cv = buildSeedValues(dt, 12, 13);
            cv.put(ISBREAK, 1);
            logEntries.add(cv);
        }
        SEED_ENTRIES = logEntries.size();
        SQLiteDatabase sdb = db.getWritableDatabase();
        sdb.beginTransaction();
        sdb.delete(TABLE_LOG_NAME, null, null);
        sdb.delete(TIME_TABLE, null, null);
        for (ContentValues value : timeTables) {
            long id = sdb.insertOrThrow(TIME_TABLE, null, value);
            if(id < 0) throw new IllegalStateException("Insert failed");
        }
        for (ContentValues value : logEntries) {
            long id = sdb.insertOrThrow(TABLE_LOG_NAME, null, value);
            if(id < 0) throw new IllegalStateException("Insert failed");
        }
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
    }

    static DateTime getSeedDateTime() {
        DateTime dt = TimeConverter.getCurrentTime();
        dt = dt.withTime(0, 0, 0, 0);
        dt = dt.withFieldAdded(DurationFieldType.weeks(), -WEEKS_BACK+1);
        dt = dt.withDayOfWeek(DateTimeConstants.MONDAY);
        return dt;
    }

    private ContentValues buildSeedValues(DateTime dt, int cameHour, int wentHour) {
        return buildSeedValues(dt, cameHour, wentHour, 0);
    }
    private ContentValues buildSeedValues(DateTime dt, int cameHour, int wentHour, int wentMinute) {
        ContentValues cv = new ContentValues();
        dt = dt.withHourOfDay(cameHour);
        cv.put(DATE, TimeConverter.extractDate(dt.getMillis()));
        cv.put(CAME, dt.getMillis());
        dt = dt.withHourOfDay(wentHour).withMinuteOfHour(wentMinute);
        cv.put(WENT, dt.getMillis());
        return cv;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor c;
        switch (uriMatcher.match(uri)){
            case KEY_GET_GET_MONTHS:
                c = sdb.query(TIME_TABLE, new String[]{ID, MONTH_OF_YEAR}, null, null, MONTH_OF_YEAR, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_GET_MONTHS);
                return c;
            case KEY_GET_GET_WEEKS:
                c = sdb.query(TIME_TABLE, new String[]{ID, WEEK_OF_YEAR}, null, null, WEEK_OF_YEAR, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_WEEKS);
                return c;
            case KEY_GET_LOG_ENTRIES:
                c = sdb.query(TABLE_LOG_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_LOG_ENTRIES);
                return c;
            case KEY_GET_GET_MONTHLY_SUMMARY:
                c = sdb.query(VIEW_TIME_TABLE_DURATIONS, new String[]{ID, DATE, WEEK_OF_YEAR, "SUM("+DURATION+") AS " + DURATION}, selection, selectionArgs, WEEK_OF_YEAR, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_MONTHLY_SUMMARY);
                return c;
            case KEY_GET_GET_DURATIONS:
                c = sdb.query(VIEW_TIME_TABLE_DURATIONS, projection, selection, selectionArgs, null, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_DURATIONS);
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
                ContentValues timeTableValues = new ContentValues();
                long came = values.getAsLong(CAME);
                long date = populateContentValuesWithTimeInfo(came, timeTableValues);
                sdb.insert(TIME_TABLE, null, timeTableValues);
                values.put(DATE, date);
                long id = sdb.insert(TABLE_LOG_NAME, null, values);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                int numberOfEventsToday = query(URI_GET_LOG_ENTRIES, null, String.format("%s=?", DATE), new String[]{String.valueOf(date)}, null, null).getCount();
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
                    getContext().getContentResolver().notifyChange(URI_GET_WEEKS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                }
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private long populateContentValuesWithTimeInfo(long came, ContentValues values) {
        long date = TimeConverter.extractDate(came);
        values.put(DATE, date);
        values.put(WEEK_OF_YEAR, TimeConverter.extractWeek(came));
        values.put(MONTH_OF_YEAR, TimeConverter.extractMonth(came));
        return date;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        int deleted;
        switch (uriMatcher.match(uri)){
            case KEY_DELETE_LOG_ENTRY:
                deleted = sdb.delete(TABLE_LOG_NAME, selection, selectionArgs);
                if(deleted > 0){
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                    getContext().getContentResolver().notifyChange(URI_GET_WEEKS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_DURATIONS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_GET_MONTHS, null);
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
                    getContext().getContentResolver().notifyChange(URI_GET_DURATIONS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_GET_MONTHS, null);
                    getContext().getContentResolver().notifyChange(URI_GET_WEEKS, null);
                }
                return updated;
            case KEY_UPDATE_PARTICULAR_LOG_ENTRY:
                ContentValues timeTableValues = new ContentValues();
                populateContentValuesWithTimeInfo(values.containsKey(DATE)?values.getAsLong(DATE):values.containsKey(CAME)?values.getAsLong(CAME):values.getAsLong(WENT), timeTableValues);
                sdb.insert(TIME_TABLE, null, timeTableValues);
                updated = sdb.update(TABLE_LOG_NAME, values, selection, selectionArgs);
                if(updated > 0) {
                    getContext().getContentResolver().notifyChange(URI_GET_LOG_ENTRIES, null);
                }
                return updated;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }


    private class DatabaseOpenHelper extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 46;
        /*
        private static final String CREATE = "CREATE TABLE IF NOT EXISTS " + OLD_TABLE_LOG_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER NOT NULL DEFAULT 0, " +
                WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                CAME + " INTEGER NOT NULL," +
                ISBREAK + " INTEGER NOT NULL DEFAULT 0, " +
                WENT + " INTEGER NOT NULL DEFAULT 0);";
*/
        private static final String CREATE_TIME_TABLE = "CREATE TABLE IF NOT EXISTS " + TIME_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER UNIQUE ON CONFLICT IGNORE, " +
                WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0);";

        private static final String CREATE_LOG = "CREATE TABLE IF NOT EXISTS " + TABLE_LOG_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER NOT NULL DEFAULT 0, " +
                CAME + " INTEGER NOT NULL," +
                ISBREAK + " INTEGER NOT NULL DEFAULT 0, " +
                WENT + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + DATE + ") REFERENCES " + TIME_TABLE +"(" + DATE+") ON DELETE CASCADE);";

        private static final String CREATE_TIME_TABLE_DURATION_JOIN_VIEW = "CREATE VIEW " + VIEW_TIME_TABLE_DURATIONS + " AS SELECT * FROM " + TIME_TABLE + " tt JOIN " + VIEW_DURATION +" vd ON tt." + DATE + "=vd."+DATE;
        private static final String CREATE_CLEANUP_TRIGGER = "CREATE TRIGGER IF NOT EXISTS cleanup AFTER DELETE ON " + TABLE_LOG_NAME + " WHEN NOT EXISTS(SELECT * FROM " + TABLE_LOG_NAME + " WHERE " + DATE + "=old."+DATE+") BEGIN DELETE FROM " + TIME_TABLE + " WHERE " + DATE + "=old."+DATE + "; END";
        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(CREATE);
            db.execSQL(CREATE_TIME_TABLE);
            db.execSQL(CREATE_LOG);
            recreateDurationsView(db);

        }

        public void recreateDurationsView(SQLiteDatabase db){
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_TIME_TABLE_DURATIONS);
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATION);
            db.execSQL("CREATE VIEW IF NOT EXISTS " + VIEW_DURATION + " AS SELECT " + ID + ", " + DATE + ", " + getDurationCalculation() + " FROM " + TABLE_LOG_NAME + " GROUP BY " + DATE);
            db.execSQL(CREATE_TIME_TABLE_DURATION_JOIN_VIEW);
        }

        private String getDurationCalculation() {
            String diff = "(" + WENT + "-" + CAME + ")";
            String durationCalculation = "SUM(CASE (" + ISBREAK + ") WHEN 0 THEN " + diff + " WHEN 1 THEN -" + diff + " END)";
            String snapUp = String.format("%s + (%d - (%s%%%d))", durationCalculation, DateTimeConstants.MILLIS_PER_HOUR / 2, durationCalculation, DateTimeConstants.MILLIS_PER_HOUR / 2);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            return (sp.getBoolean("use_snapup", false)?snapUp:durationCalculation) + " AS " + DURATION;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TRIGGER IF EXISTS break_trigger");
            Cursor c = null;
            for(int version = oldVersion; version <= newVersion; version++){
                switch (version){
                    case 16:
                        db.execSQL("ALTER TABLE cameandwent ADD COLUMN " + DATE + " INTEGER NOT NULL DEFAULT 0");
                        break;
                    case 19:
                        db.execSQL("ALTER TABLE cameandwent ADD COLUMN " + ISBREAK + " INTEGER NOT NULL DEFAULT 0");
                        break;
                    case 32:
                        db.execSQL("ALTER TABLE cameandwent ADD COLUMN " + WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0");
                    case 33:
                        c = db.rawQuery("SELECT * FROM cameandwent", null);
                        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                            ContentValues cv = new ContentValues();
                            cv.put(WEEK_OF_YEAR, TimeConverter.extractWeek(c.getLong(c.getColumnIndex(CAME))));
                            db.update("cameandwent", cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                        }
                        c.close();
                        break;
                    case 34:
                        db.execSQL("ALTER TABLE cameandwent ADD COLUMN " + MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0");
                    case 36:
                    case 37:
                    case 35:
                        c = db.rawQuery("SELECT * FROM cameandwent", null);
                        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                            ContentValues cv = new ContentValues();
                            cv.put(MONTH_OF_YEAR, TimeConverter.extractMonth(c.getLong(c.getColumnIndex(CAME))));
                            db.update(TABLE_LOG_NAME, cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                        }
                        c.close();
                        break;
                    case 38:
                        db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATION);
                        break;
                    case 46:
                        db.execSQL(CREATE_LOG);
                        db.execSQL(CREATE_TIME_TABLE);
                        recreateDurationsView(db);
                        migrateData(db);
                        break;
                }
            }
            if(c != null) c.close();
            onCreate(db);
        }
    }
}
