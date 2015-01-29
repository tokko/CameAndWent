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

    private static final int KEY_CAME_OR_WENT = 0;
    private static final int KEY_GET_ALL = 1;

    public static final Uri URI_CAME_OR_WENT = Uri.parse(URI_TEMPLATE + ACTION_CAME_OR_WENT);
    public static final Uri URI_GET_ALL = Uri.parse(URI_TEMPLATE + ACTION_GET_ALL);

    private static UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ACTION_CAME_OR_WENT, KEY_CAME_OR_WENT);
        uriMatcher.addURI(AUTHORITY, ACTION_GET_ALL, KEY_GET_ALL);
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
                c = sdb.query(TABLE_NAME, null, null, null, null, null, DATE + " ASC");
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
        return 0;
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
