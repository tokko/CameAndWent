package com.tokko.cameandwent.cameandwent.providers;

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

import com.tokko.cameandwent.cameandwent.BuildConfig;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CameAndWentProvider extends ContentProvider {
    public static final int WEEKS_BACK = 40;

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID+".CameAndWentProvider";
    private static final String URI_TEMPLATE = "content://" + AUTHORITY + "/";

    public static final String DATABASE_NAME = "cameandwent";

    private static final String TABLE_LOG_NAME = "log";
    private static final String VIEW_DURATIONS = "durations";
    private static final String TIME_TABLE = "timetable";
    private static final String TABLE_TAGS_NAME = "tags";
    private static final String VIEW_TIME_TABLE_DURATIONS =  "timetabledurations";
    private static final String VIEW_LOG = "logview";
    private static final String VIEW_DURATIONS_PER_DAY = "durationsperday";

    public static final String ID = "_id";
    public static final String CAME = "came";
    public static final String WENT = "went";
    public static final String ISBREAK = "isbreak";
    public static final String WEEK_OF_YEAR = "weekofyear";
    public static final String MONTH_OF_YEAR = "monthofyear";
    public static final String YEAR = "year";
    public static final String TAG = "tag";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String RECIPIENT = "recipient";
    public static final String NAME = "name";
    public static final String REMINDER = "reminder";
    public static final String TITLE_PREFIX = "titleprefix";

    public static final String DATE = "date";
    public static final String DURATION = "duration";

    private static final String ACTION_CAME = "ACTION_CAME";
    private static final String ACTION_WENT = "ACTION_WENT";

    //"TABLES"
    private static final String GET_LOG_ENTRIES = "GET_LOG_ENTRIES";
    private static final String GET_WEEKS = "GET_WEEKS";
    private static final String GET_MONTHS = "GET_MONTHS";
    private static final String GET_DURATIONS = "GET_DURATIONS";
    private static final String GET_MONTHLY_SUMMARY = "GET_MONTHLY_SUMMARY";
    private static final String GET_TAGS = "GET_TAGS";
    private static final String GET_LOG_ENTRY_FOR_EDIT = "GET_LOG_ENTRY_FOR_EDIT";
    private static final String GET_DURATIONS_PER_DAY = "GET_DURATIONS_PER_DAY";

    private static final int KEY_CAME = 0;
    private static final int KEY_WENT = 1;

    private static final int KEY_LOG_ENTRIES = 2;
    private static final int KEY_WEEKS = 7;
    private static final int KEY_MONTHS = 8;
    private static final int KEY_DURATIONS = 9;
    private static final int KEY_MONTHLY_SUMMARY = 10;
    private static final int KEY_TAGS = 11;
    private static final int KEY_GET_LOG_ENTRY_FOR_EDIT = 12;
    private static final int KEY_DURATIONS_PER_DAY = 13;

    public static final Uri URI_CAME = makeUri(ACTION_CAME, KEY_CAME);
    public static final Uri URI_WENT = makeUri(ACTION_WENT, KEY_WENT);

    public static final Uri URI_LOG_ENTRIES = makeUri(GET_LOG_ENTRIES, KEY_LOG_ENTRIES);
    public static final Uri URI_WEEKS = makeUri(GET_WEEKS, KEY_WEEKS);
    public static final Uri URI_MONTHS =makeUri(GET_MONTHS, KEY_MONTHS);
    public static final Uri URI_DURATIONS = makeUri(GET_DURATIONS, KEY_DURATIONS);
    public static final Uri URI_MONTHLY_SUMMARY = makeUri(GET_MONTHLY_SUMMARY, KEY_MONTHLY_SUMMARY);
    public static final Uri URI_TAGS = makeUri(GET_TAGS, KEY_TAGS);
    public static final Uri URI_GET_LOG_ENTRY_FOR_EDIT = makeUri(GET_LOG_ENTRY_FOR_EDIT, KEY_GET_LOG_ENTRY_FOR_EDIT);
    public static final Uri URI_DURATIONS_PER_DAY = makeUri(GET_DURATIONS_PER_DAY, KEY_DURATIONS_PER_DAY);

    private static UriMatcher uriMatcher;

    private static Uri makeUri(String action, int key){
        if(uriMatcher == null) uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, action, key);
        return Uri.parse(URI_TEMPLATE + action);
    }

    private DatabaseOpenHelper db;
    @Override
    public boolean onCreate() {
        db = new DatabaseOpenHelper(getContext());
        return true;
    }

    public static final String SEED_METHOD = "seed";
    public static final String RECREATE_METHOD = "recreate";
    public static final String MIGRATE_METHOD = "migrate";
    public static final String CLEAN_METHOD = "clean";
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        switch (method) {
            case SEED_METHOD:
                seed();
                break;
            case RECREATE_METHOD:
                recreateDurationsView();
                break;
            case MIGRATE_METHOD:
                migrateData(db.getWritableDatabase());
                break;
             case CLEAN_METHOD:
                clean();
                break;
        }
        return super.call(method, arg, extras);
    }

    public void clean(){
        SQLiteDatabase sdb = db.getWritableDatabase();
        sdb.beginTransaction();
        Cursor c = query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s>?", WENT), new String[]{"0"}, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
            if(c.getLong(c.getColumnIndex(CameAndWentProvider.WENT)) - c.getLong(c.getColumnIndex(CameAndWentProvider.CAME)) < DateTimeConstants.MILLIS_PER_MINUTE)
                sdb.delete(TABLE_LOG_NAME, String.format("%s=?", ID), new String[]{String.valueOf(c.getLong(c.getColumnIndex(ID)))});
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        c.close();
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
        getContext().getContentResolver().notifyChange(URI_DURATIONS, null);
        getContext().getContentResolver().notifyChange(URI_MONTHLY_SUMMARY, null);
        getContext().getContentResolver().notifyChange(URI_MONTHS, null);
        getContext().getContentResolver().notifyChange(URI_WEEKS, null);
        getContext().getContentResolver().notifyChange(URI_LOG_ENTRIES, null);
    }

    public void recreateDurationsView() {
        db.recreateViews(db.getWritableDatabase());
    }

    public static int SEED_ENTRIES = 0;
    public void seed() {
        seed(db.getWritableDatabase());
    }
    public void seed(SQLiteDatabase sdb){
        Log.d("Provider", "Seeding");
        DateTime dtNow = new DateTime();

        ArrayList<ContentValues> logEntries = new ArrayList<>();
        ArrayList<ContentValues> timeTables = new ArrayList<>();
        for(DateTime dt = getSeedDateTime(); dt.getMillis() <= dtNow.getMillis(); dt = dt.withFieldAdded(DurationFieldType.days(), 1)){
            if(dt.getDayOfWeek() == DateTimeConstants.SATURDAY || dt.getDayOfWeek() == DateTimeConstants.SUNDAY) continue;
            ContentValues cv = new ContentValues();
            cv.put(DATE, TimeConverter.extractDate(dt.getMillis()));
            cv.put(WEEK_OF_YEAR, TimeConverter.extractWeek(dt.getMillis()));
            cv.put(MONTH_OF_YEAR, TimeConverter.extractMonth(dt.getMillis()));
            timeTables.add(cv);

            cv = buildSeedValues(dt, 8, 12, 1);
            logEntries.add(cv);

            boolean useSnapup = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("use_snapup", false);
            cv = buildSeedValues(dt, 12, 17, useSnapup?15:0, 2);
            logEntries.add(cv);

            cv = buildSeedValues(dt, 12, 13, 1);
            cv.put(ISBREAK, 1);
            logEntries.add(cv);
        }
        SEED_ENTRIES = logEntries.size();
        sdb.beginTransaction();
        sdb.delete(TABLE_TAGS_NAME, null, null);
        sdb.delete(TABLE_LOG_NAME, null, null);
        sdb.delete(TIME_TABLE, null, null);
        for(int i=1; i<=5; i++){
            ContentValues cv = new ContentValues();
            cv.put(ID, i);
            cv.put(TAG, "TAG" + i);
            sdb.insertOrThrow(TABLE_TAGS_NAME, null, cv);
        }

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
        getContext().getContentResolver().notifyChange(URI_LOG_ENTRIES, null);
        getContext().getContentResolver().notifyChange(URI_DURATIONS, null);
    }

    public static DateTime getSeedDateTime() {
        DateTime dt = TimeConverter.getCurrentTime();
        dt = dt.withTime(0, 0, 0, 0);
        dt = dt.withFieldAdded(DurationFieldType.weeks(), -WEEKS_BACK+1);
        dt = dt.withDayOfWeek(DateTimeConstants.MONDAY);
        return dt;
    }

    private ContentValues buildSeedValues(DateTime dt, int cameHour, int wentHour, int tag) {
        return buildSeedValues(dt, cameHour, wentHour, 0, tag);
    }

    private ContentValues buildSeedValues(DateTime dt, int cameHour, int wentHour, int wentMinute, int tag) {
        ContentValues cv = new ContentValues();
        dt = dt.withHourOfDay(cameHour);
        cv.put(DATE, TimeConverter.extractDate(dt.getMillis()));
        cv.put(CAME, dt.getMillis());
        dt = dt.withHourOfDay(wentHour).withMinuteOfHour(wentMinute);
        cv.put(WENT, dt.getMillis());
        cv.put(TAG, tag);
        return cv;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor c;
        switch (uriMatcher.match(uri)){
            case KEY_MONTHS:
                c = sdb.query(TIME_TABLE, new String[]{ID, MONTH_OF_YEAR}, null, null, YEAR + ", " + MONTH_OF_YEAR, null, DATE + " ASC", null);
                c.setNotificationUri(getContext().getContentResolver(), URI_MONTHS);
                return c;
            case KEY_WEEKS:
                c = sdb.query(TIME_TABLE, new String[]{ID, WEEK_OF_YEAR, DATE, YEAR}, null, null, YEAR + ", " +WEEK_OF_YEAR, null, DATE + " ASC", null);
                c.setNotificationUri(getContext().getContentResolver(), URI_WEEKS);
                return c;
            case KEY_DURATIONS_PER_DAY:
                c = sdb.query(VIEW_DURATIONS_PER_DAY, projection, selection, selectionArgs, DATE, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_DURATIONS_PER_DAY);
                return c;
            case KEY_GET_LOG_ENTRY_FOR_EDIT:
                c = sdb.query(TABLE_LOG_NAME, projection, selection, selectionArgs, null, null, sortOrder, "1");
                c.setNotificationUri(getContext().getContentResolver(), URI_LOG_ENTRIES);
                return c;
            case KEY_LOG_ENTRIES:
                c = sdb.query(VIEW_LOG, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), URI_LOG_ENTRIES);
                return c;
            case KEY_MONTHLY_SUMMARY:
                c = sdb.query(VIEW_TIME_TABLE_DURATIONS, new String[]{ID, DATE, WEEK_OF_YEAR, "SUM("+DURATION+") AS " + DURATION}, selection, selectionArgs, WEEK_OF_YEAR, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_MONTHLY_SUMMARY);
                return c;
            case KEY_DURATIONS:
                c = sdb.query(VIEW_TIME_TABLE_DURATIONS, projection, selection, selectionArgs, null, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_DURATIONS);
                return c;
            case KEY_TAGS:
                c = sdb.query(TABLE_TAGS_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
                c.setNotificationUri(getContext().getContentResolver(), URI_TAGS);
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
                if(id > -1) {
                   notifyAllUris();
                }
                return ContentUris.withAppendedId(uri, id);
            case KEY_TAGS:
                id = sdb.insert(TABLE_TAGS_NAME, null, values);
                if(id > -1){
                    notifyAllUris();
                }
                return ContentUris.withAppendedId(uri, id);
            case KEY_LOG_ENTRIES:
                id = sdb.insert(TABLE_LOG_NAME, null, values);
                if(id > -1){
                    notifyAllUris();
                }
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private void notifyAllUris() {
        Field[] fields = this.getClass().getFields();
        try {
            for (Field field : fields) {
                if (field.getName().startsWith("URI_")) {
                    getContext().getContentResolver().notifyChange((Uri) field.get(this), null);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private long populateContentValuesWithTimeInfo(long came, ContentValues values) {
        long date = TimeConverter.extractDate(came);
        values.put(DATE, date);
        values.put(WEEK_OF_YEAR, TimeConverter.extractWeek(came));
        values.put(MONTH_OF_YEAR, TimeConverter.extractMonth(came));
        values.put(YEAR, TimeConverter.extractYear(came));
        return date;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        int deleted;
        switch (uriMatcher.match(uri)){
            case KEY_LOG_ENTRIES:
                deleted = sdb.delete(TABLE_LOG_NAME, selection, selectionArgs);
                if(deleted > 0){
                    notifyAllUris();

                }
                return deleted;
            case KEY_TAGS:
                deleted = sdb.delete(TABLE_TAGS_NAME, selection, selectionArgs);
                if(deleted > 0){
                    notifyAllUris();

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
                    notifyAllUris();

                }
                return updated;
            case KEY_LOG_ENTRIES:
                ContentValues timeTableValues = null;
                if(values.containsKey(DATE)){
                    timeTableValues = new ContentValues();
                    populateContentValuesWithTimeInfo(values.getAsLong(DATE), timeTableValues);
                }
                else if(values.containsKey(CAME)){
                    timeTableValues = new ContentValues();
                    populateContentValuesWithTimeInfo(values.getAsLong(CAME), timeTableValues);
                }
                else if(values.containsKey(WENT)){
                    timeTableValues = new ContentValues();
                    populateContentValuesWithTimeInfo(values.getAsLong(WENT), timeTableValues);
                }
                if(timeTableValues != null)
                    sdb.insert(TIME_TABLE, null, timeTableValues);
                updated = sdb.update(TABLE_LOG_NAME, values, selection, selectionArgs);
                if(updated > 0) {
                    notifyAllUris();

                }
                return updated;
            case KEY_TAGS:
                updated = sdb.update(TABLE_TAGS_NAME, values, selection, selectionArgs);
                if(updated > 0) {
                    notifyAllUris();

                }
                return updated;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }


    private class DatabaseOpenHelper extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 70;
        private static final String CREATE_TIME_TABLE = "CREATE TABLE IF NOT EXISTS " + TIME_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER UNIQUE ON CONFLICT REPLACE, " +
                YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                WEEK_OF_YEAR + " INTEGER NOT NULL DEFAULT 0, " +
                MONTH_OF_YEAR + " INTEGER NOT NULL DEFAULT 0);";

        private static final String CREATE_LOG = "CREATE TABLE IF NOT EXISTS " + TABLE_LOG_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " INTEGER NOT NULL DEFAULT 0, " +
                CAME + " INTEGER NOT NULL," +
                ISBREAK + " INTEGER NOT NULL DEFAULT 0, " +
                WENT + " INTEGER NOT NULL DEFAULT 0, " +
                TAG + " INTEGER DEFAULT NULL, " +
                "FOREIGN KEY(" + DATE + ") REFERENCES " + TIME_TABLE +"(" + DATE + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + TAG + ") REFERENCES " + TABLE_TAGS_NAME +"(" + ID + "));";

        private static final String CREATE_TAGS = "CREATE TABLE IF NOT EXISTS " + TABLE_TAGS_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                TAG + " TEXT UNIQUE ON CONFLICT IGNORE, " +
                RECIPIENT + " TEXT NOT NULL DEFAULT 'placeholder@placeholder.placeholder', " +
                NAME + " TEXT NOT NULL DEFAULT 'placeholder', " +
                REMINDER + " INTEGER NOT NULL DEFAULT 0," +
                TITLE_PREFIX + " TEXT NOT NULL DEFAULT 'placeholder', " +
                LATITUDE + " INTEGER NOT NULL DEFAULT -1, " +
                LONGITUDE + " INTEGER NOT NULL DEFAULT -1);";

        private static final String CREATE_TIME_TABLE_DURATION_JOIN_VIEW = "CREATE VIEW " + VIEW_TIME_TABLE_DURATIONS + " AS SELECT * FROM " + TIME_TABLE + " tt JOIN " + VIEW_DURATIONS +" vd USING(" + DATE + ") GROUP BY " + DATE + ", " + TAG;

        private static final String CREATE_LOG_VIEW = "CREATE VIEW " + VIEW_LOG + " AS SELECT l."+ID+", l."+CAME+", l."+WENT+", l."+ISBREAK+", l."+DATE+ ", tags."+TAG+" FROM " + TABLE_LOG_NAME + " l LEFT OUTER JOIN "+TABLE_TAGS_NAME+" tags ON l."+TAG+"=tags."+ID;

        private static final String CREATE_VIEW_DURATIONS = "CREATE VIEW IF NOT EXISTS " + VIEW_DURATIONS + " AS SELECT tt." + ID + ", tt." + DATE + ", %s, tt." + TAG + " FROM " + VIEW_LOG + " tt GROUP BY " + DATE + ", " + TAG;

        private static final String CREATE_VIEW_DURATIONS_PER_DAY = "CREATE VIEW IF NOT EXISTS " + VIEW_DURATIONS_PER_DAY + " AS SELECT " + ID + ", " + DATE  + ", " + WEEK_OF_YEAR + ", " + MONTH_OF_YEAR + ", " + TAG + ", SUM("+DURATION+") AS " + DURATION + " FROM " + VIEW_TIME_TABLE_DURATIONS + " GROUP BY " + DATE;

        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TIME_TABLE);
            db.execSQL(CREATE_LOG);
            db.execSQL(CREATE_TAGS);
            recreateViews(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            if(BuildConfig.BUILD_TYPE.equals("mock")){
                db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATIONS);
                db.execSQL("DROP VIEW IF EXISTS " + VIEW_LOG);
                db.execSQL("DROP VIEW IF EXISTS " + VIEW_TIME_TABLE_DURATIONS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE);
                onCreate(db);
                seed(db);
            }
        }

        public void recreateViews(SQLiteDatabase db){
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_TIME_TABLE_DURATIONS);
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATIONS);
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_LOG);
            db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATIONS_PER_DAY);
            db.execSQL(CREATE_LOG_VIEW);
            db.execSQL(String.format(CREATE_VIEW_DURATIONS, getDurationCalculation()));
            db.execSQL(CREATE_TIME_TABLE_DURATION_JOIN_VIEW);
            db.execSQL(CREATE_VIEW_DURATIONS_PER_DAY);
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
            //if(BuildConfig.DEBUG) return;
            db.execSQL("DROP TRIGGER IF EXISTS break_trigger");
            Cursor c = null;
            db.beginTransaction();
            try {
                for (int version = oldVersion; version <= newVersion; version++) {
                    switch (version) {
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
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
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
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                ContentValues cv = new ContentValues();
                                cv.put(MONTH_OF_YEAR, TimeConverter.extractMonth(c.getLong(c.getColumnIndex(CAME))));
                                db.update(TABLE_LOG_NAME, cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                            }
                            c.close();
                            break;
                        case 38:
                            db.execSQL("DROP VIEW IF EXISTS " + VIEW_DURATIONS);
                            break;
                        case 45:
                            db.execSQL(CREATE_LOG);
                            db.execSQL(CREATE_TIME_TABLE);
                            //recreateViews(db);
                            migrateData(db);
                            break;
                        case 49:
                            db.execSQL("ALTER TABLE " + TABLE_LOG_NAME + " ADD COLUMN " + TAG + " INTEGER DEFAULT NULL REFERENCES " + TIME_TABLE + "(" + DATE + ") ON DELETE CASCADE");
                            break;
                        case 60:
                            db.execSQL("ALTER TABLE " + TABLE_TAGS_NAME + " ADD COLUMN " + RECIPIENT + " TEXT NOT NULL DEFAULT 'placeholder@placeholder.placeholder'");
                            db.execSQL("ALTER TABLE " + TABLE_TAGS_NAME + " ADD COLUMN " + NAME + " TEXT NOT NULL DEFAULT 'placeholder' ");
                            db.execSQL("ALTER TABLE " + TABLE_TAGS_NAME + " ADD COLUMN " + REMINDER + " INTEGER NOT NULL DEFAULT 0 ");
                            db.execSQL("ALTER TABLE " + TABLE_TAGS_NAME + " ADD COLUMN " + TITLE_PREFIX + " TEXT NOT NULL DEFAULT 'placeholder' ");
                            break;
                        case 66:
                            db.execSQL("ALTER TABLE " + TIME_TABLE + " ADD COLUMN " + YEAR + " INTEGER NOT NULL DEFAULT 0");
                            c = db.rawQuery("SELECT * FROM " + TIME_TABLE, null);
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                ContentValues cv = new ContentValues();
                                cv.put(YEAR, TimeConverter.extractYear(c.getLong(c.getColumnIndex(DATE))));
                                db.update(TIME_TABLE, cv, String.format("%s=?", ID), new String[]{String.valueOf(c.getInt(c.getColumnIndex(ID)))});
                            }
                            break;
                    }
                }
                db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE);
                db.execSQL(CREATE_TIME_TABLE);
                c = db.rawQuery("SELECT * FROM " + TABLE_LOG_NAME, null);
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    ContentValues cv = new ContentValues();
                    populateContentValuesWithTimeInfo(c.getLong(c.getColumnIndex(DATE)), cv);
                    db.insert(TIME_TABLE, null, cv);
                }
                if (c != null) c.close();
            }catch (Exception e){
                db.endTransaction();
                throw new IllegalStateException("SQL database not upgraded properly");
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            onCreate(db);
        }
    }
}
