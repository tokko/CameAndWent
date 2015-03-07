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

    private static final String OLD_TABLE_LOG_NAME = "cameandwent";
    private static final String TABLE_LOG_NAME = "log";
    private static final String VIEW_DURATION = "durations";
    private static final String TIME_TABLE = "timetable";

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
    private static final String ACTION_GET_MONTHLY_SUMMARY = "ACTION_GET_MONTHLY_SUMMARY";
    private static final String ACTION_GET_WEEKS = "ACTION_GET_WEEKS";
    private static final String ACTION_GET_MONTHS = "ACTION_GET_MONTHS";

    private static final int KEY_CAME = 0;
    private static final int KEY_WENT = 1;
    private static final int KEY_GET_LOG_ENTRIES = 2;
    private static final int KEY_DELETE_LOG_ENTRY = 4;
    private static final int KEY_UPDATE_PARTICULAR_LOG_ENTRY = 5;
    private static final int KEY_GET_MONTHLY_SUMMARY = 6;
    private static final int KEY_GET_GET_WEEKS = 7;
    private static final int KEY_GET_GET_MONTHS = 8;

    public static final Uri URI_CAME = Uri.parse(URI_TEMPLATE + ACTION_CAME);
    public static final Uri URI_WENT = Uri.parse(URI_TEMPLATE + ACTION_WENT);
    public static final Uri URI_GET_LOG_ENTRIES = Uri.parse(URI_TEMPLATE + ACTION_GET_LOG_ENTRIES);
    public static final Uri URI_DELETE_LOG_ENTRY = Uri.parse(URI_TEMPLATE + ACTION_DELETE_LOG_ENTRY);
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
        uriMatcher.addURI(AUTHORITY, ACTION_DELETE_LOG_ENTRY, KEY_DELETE_LOG_ENTRY);
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
    public void seed(){
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

            cv = buildSeedValues(dt, 12, 17, PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("use_snapup", false)?15:0);
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
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_GET_WEEKS);
                return c;
            case KEY_GET_LOG_ENTRIES:
                c = sdb.query(TABLE_LOG_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_LOG_ENTRIES);
                return c;
           case KEY_GET_MONTHLY_SUMMARY:
              //  c = sdb.query(TABLE_LOG_NAME, new String[]{ID, WEEK_OF_YEAR, getDurationCalculation()}, selection, selectionArgs, WEEK_OF_YEAR, null, WEEK_OF_YEAR + " ASC", null);
                c = sdb.rawQuery("SELECT " + join(new String[]{"tt."+ID, WEEK_OF_YEAR, DURATION}) + " FROM " + VIEW_DURATION + " vd LEFT JOIN " + TIME_TABLE + " tt ON vd." + DATE + "=tt." + DATE + " " + (selection!=null?" WHERE " + prefix("tt.", selection, ID, DATE):"") + " GROUP BY " + WEEK_OF_YEAR + " ORDER BY " + WEEK_OF_YEAR + " ASC", prefix("tt.", selectionArgs, ID, DATE));
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_MONTHLY_SUMMARY);
                return c;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private String prefix(String prefix, String s, String... subs){
        if(s == null) return null;
        for(String sub : subs){
            s = s.replace(sub, prefix+sub);
        }
        return s;
    }
    private String[] prefix(String prefix, String[] values, String... subs){
        if(values == null) return null;
        for (int i = 0; i<values.length; i++)
            values[i] = contains(subs, values[i]) ? prefix + values[i] : values[i];
        return values;
    }

    private <T> boolean contains(T[] arr, T elem){
        for (T anArr : arr) if (anArr.equals(elem)) return true;
        return false;
    }
    private <T> String join(T[] t) {
        return join(", ", t, "*");
    }
    private <T> String join(String delim, T[] t, String def){
        if(t == null || t.length == 0) return def;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<t.length; i++){
            sb.append(t[i]);
            if(i < t.length-1) sb.append(delim);
        }
        return sb.toString();
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
                    getContext().getContentResolver().notifyChange(URI_GET_GET_WEEKS, null);
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
                }
                return updated;
            case KEY_UPDATE_PARTICULAR_LOG_ENTRY:
                ContentValues timeTableValues = new ContentValues();
                populateContentValuesWithTimeInfo(values.containsKey(CAME)?values.getAsLong(CAME):values.getAsLong(WENT), timeTableValues);
                sdb.update(TIME_TABLE, timeTableValues, String.format("%s=?", DATE), new String[]{String.valueOf(values.getAsLong(DATE))});
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
        private static final int DATABASE_VERSION = 40;
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
                DATE + " INTEGER UNIQUE ON CONFLICT REPLACE, " +
                WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0);";

        private static final String CREATE_LOG = "CREATE TABLE IF NOT EXISTS " + TABLE_LOG_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER NOT NULL DEFAULT 0, " +
                CAME + " INTEGER NOT NULL," +
                ISBREAK + " INTEGER NOT NULL DEFAULT 0, " +
                WENT + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + DATE + ") REFERENCES " + TIME_TABLE +"(" + DATE+") ON DELETE CASCADE);";

        private static final String CREATE_TIME_TABLE_DURATION_JOIN_VIEW = "CREATE VIEW " + "timetabledurations" + " AS SELECT * FROM " + TIME_TABLE + " NATURAL JOIN " + VIEW_DURATION +";";
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
            db.execSQL(CREATE_TIME_TABLE_DURATION_JOIN_VIEW);
        }

        public void recreateDurationsView(SQLiteDatabase db){
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATION);
            db.execSQL("CREATE VIEW IF NOT EXISTS " + VIEW_DURATION + " AS SELECT " + ID + ", " + DATE + ", " + getDurationCalculation() + " FROM " + TABLE_LOG_NAME + " GROUP BY " + DATE);
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
                        db.execSQL("ALTER TABLE " + OLD_TABLE_LOG_NAME + " ADD COLUMN " + DATE + " INTEGER NOT NULL DEFAULT 0");
                        break;
                    case 19:
                        db.execSQL("ALTER TABLE " + OLD_TABLE_LOG_NAME + " ADD COLUMN " + ISBREAK + " INTEGER NOT NULL DEFAULT 0");
                        break;
                    case 32:
                        db.execSQL("ALTER TABLE " + OLD_TABLE_LOG_NAME + " ADD COLUMN " + WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0");
                    case 33:
                        c = db.rawQuery("SELECT * FROM " + OLD_TABLE_LOG_NAME, null);
                        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                            ContentValues cv = new ContentValues();
                            cv.put(WEEK_OF_YEAR, TimeConverter.extractWeek(c.getLong(c.getColumnIndex(CAME))));
                            db.update(OLD_TABLE_LOG_NAME, cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                        }
                        c.close();
                        break;
                    case 34:
                        db.execSQL("ALTER TABLE " + OLD_TABLE_LOG_NAME + " ADD COLUMN " + MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0");
                    case 36:
                    case 37:
                    case 35:
                        c = db.rawQuery("SELECT * FROM " + OLD_TABLE_LOG_NAME, null);
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
                }
            }
            if(c != null) c.close();
            onCreate(db);
        }
    }
}
