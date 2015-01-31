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

public class CameAndWentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.tokko.cameandwent.cameandwent.CameAndWentProvider";
    private static final String URI_TEMPLATE = "content://" + AUTHORITY + "/";

    private static final String DATABASE_NAME = "cameandwent";

    private static final String TABLE_NAME = "cameandwent";

    public static final String ID = "_id";
    public static final String CAME = "came";
    public static final String WENT = "went";
    public static final String DURATION = "duration";

    private static final String ACTION_CAME = "ACTION_CAME";
    private static final String ACTION_WENT = "ACTION_WENT";
    private static final String ACTION_GET_ALL = "ACTION_GET_ALL";
    private static final String ACTION_DELETE_ALL = "ACTION_DELETE_ALL";

    private static final int KEY_CAME = 0;
    private static final int KEY_WENT = 1;
    private static final int KEY_GET_ALL = 2;
    private static final int KEY_DELETE_ALL = 3;

    public static final Uri URI_CAME = Uri.parse(URI_TEMPLATE + ACTION_CAME);
    public static final Uri URI_WENT = Uri.parse(URI_TEMPLATE + ACTION_WENT);
    public static final Uri URI_GET_ALL = Uri.parse(URI_TEMPLATE + ACTION_GET_ALL);
    public static final Uri URI_DELETE_ALL = Uri.parse(URI_TEMPLATE + ACTION_DELETE_ALL);

    private static UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ACTION_CAME, KEY_CAME);
        uriMatcher.addURI(AUTHORITY, ACTION_WENT, KEY_WENT);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_ALL, KEY_GET_ALL);
        uriMatcher.addURI(AUTHORITY, ACTION_DELETE_ALL, KEY_DELETE_ALL);
    }

    private DatabaseOpenHelper db;

    @Override
    public boolean onCreate() {
        db = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor c;
        switch (uriMatcher.match(uri)){
            case KEY_GET_ALL:
                //c = sdb.query(TABLE_NAME, null, null, null, null, null, null);
                c = sdb.rawQuery("SELECT *, SUM("+WENT + "-" + CAME + ") AS " + DURATION + " FROM " + TABLE_NAME + " GROUP BY (" + CAME + " - " + CAME + "%(1000*60*60*24)) ORDER BY " + CAME + " DESC", null);
                c.setNotificationUri(getContext().getContentResolver(), URI_GET_ALL);
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
        SQLiteDatabase sdb = db.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case KEY_WENT:
                int updated = sdb.update(TABLE_NAME, values, WENT + " = 0", null);
                if(updated > 0)
                    getContext().getContentResolver().notifyChange(URI_GET_ALL, null);
                return updated;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper{
        private static final int DATABASE_VERSION = 3;
        private static final String CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                CAME + " INTEGER NOT NULL," +
                WENT + " INTEGER NOT NULL DEFAULT 0);";

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
