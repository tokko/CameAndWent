package com.tokko.cameandwent.cameandwent;

import android.content.ContentValues;
import android.content.Context;

public class ClockManager {
    private Context context;

    public ClockManager(Context context) {
        this.context = context;
    }

    public void clockIn(){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.CAME, System.currentTimeMillis());
        context.getContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
    }

    public void clockOut(){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, System.currentTimeMillis());
        context.getContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);

    }
}
