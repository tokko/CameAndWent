package com.tokko.cameandwent.cameandwent;

import android.content.ContentValues;
import android.content.Context;

public class ClockManager {
    private Context context;

    public ClockManager(Context context) {
        this.context = context;
    }

    public void clockIn(){
        clockIn(System.currentTimeMillis());
    }

    public void clockOut(){
       clockOut(System.currentTimeMillis());

    }

    public void clockIn(long time) {
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, time);
        context.getContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
    }

    public void clockOut(long time) {
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, time);
        context.getContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);
    }
}
