package com.tokko.cameandwent.cameandwent;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre_000 on 2015-01-28.
 */
public class CameAndWentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.tokko.cameandwent.cameandwent.CameAndWentProvider";
    private static final String URI_TEMPLATE = "content://" + AUTHORITY + "/";

    private static final String DATABASE_NAME = "cameandwent";

    private static final String TABLE_NAME = "cameandwent";

    public static final String ID = "_id";
    public static final String DATE = "date";
    public static final String ENTERED = "entered";

    private static final String ACTION_CAME_OR_WENT = "ACTION_CAME_OR_WENT";
    private static final String ACTION_GET_ALL = "ACTION_GET_ALL";
    private static final String ACTION_DELETE_ALL = "ACTION_DELETE_ALL";

    private static final int KEY_CAME_OR_WENT = 0;
    private static final int KEY_GET_ALL = 1;
    private static final int KEY_DELETE_ALL = 2;

    public static final Uri URI_CAME_OR_WENT = Uri.parse(URI_TEMPLATE + ACTION_CAME_OR_WENT);
    public static final Uri URI_GET_ALL = Uri.parse(URI_TEMPLATE + ACTION_GET_ALL);
    public static final Uri URI_DELETE_ALL = Uri.parse(URI_TEMPLATE + ACTION_DELETE_ALL);

    private static UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ACTION_CAME_OR_WENT, KEY_CAME_OR_WENT);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_ALL, KEY_GET_ALL);
        uriMatcher.addURI(AUTHORITY, ACTION_DELETE_ALL, KEY_DELETE_ALL);
    }

    private DatabaseOpenHelper db;

    @Override
    public boolean onCreate() {
        db = new DatabaseOpenHelper(getContext());
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor c;
        switch (uriMatcher.match(uri)){
            case KEY_GET_ALL:
                c = sdb.query(TABLE_NAME, null, null, null, null, null, null);
                c = transfromCursor(c);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_ALL);
                return c;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private Cursor transfromCursor(Cursor c) {
        HashMap<Long, ArrayList<LogEntry>> map = new HashMap<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            LogEntry logEntry = new LogEntry(c);
            if(!map.containsKey(logEntry.getDay())) map.put(logEntry.getDay(), new ArrayList<LogEntry>());
            map.get(logEntry.getDay()).add(logEntry);
        }

        ArrayList<DisplayLogEntry> displayLogEntries = new ArrayList<>();
        for (long key : map.keySet()){
            ArrayList<LogEntry> list = map.get(key);
            Collections.sort(list);
            if(list.size()%2!=0)
                list.add(new LogEntry(-1, System.currentTimeMillis(), 3));
        }
        //TODO: finish this.

        return null;
    }

    private class DisplayLogEntry implements Comparable<DisplayLogEntry>{



        private long id;
        private long duration;
        private long day;

        @Override
        public int compareTo(DisplayLogEntry another) {
            return (int) (day - another.day);
        }
    }

    private class LogEntry implements Comparable<LogEntry>{
        private int id;
        private long date;
        private int entered;

        private LogEntry(int id, long date, int entered) {
            this.id = id;
            this.date = date;
            this.entered = entered;
        }

        public LogEntry(Cursor c){
            id = c.getInt(c.getColumnIndex(ID));
            date = c.getLong(c.getColumnIndex(DATE));
            entered = c.getInt(c.getColumnIndex(ENTERED));
        }

        @Override
        public int compareTo(LogEntry another) {
            return (int) (date - another.date);
        }
        public long getDay(){
            return date - date%1000*60*60*24;
        }
        @Override
        public boolean equals(Object o) {
            if(o instanceof LogEntry){
                LogEntry another = (LogEntry) o;
                return getDay() == another.getDay();
            }
            return false;
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
            case KEY_CAME_OR_WENT:
                long id = sdb.insert(TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(URI_GET_ALL, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case KEY_DELETE_ALL:
                 int deleted = sdb.delete(TABLE_NAME, null, null);
                if(deleted > 0)
                    getContext().getContentResolver().notifyChange(URI_GET_ALL, null);
                return deleted;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 2;
        private static final String CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                ENTERED + " INTEGER NOT NULL DEFAULT 0);";

        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_NAME);
            onCreate(db);
        }
    }
}
