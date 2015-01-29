package com.tokko.cameandwent.cameandwent;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by andre_000 on 2015-01-29.
 */
public class ClockManager {
    private Context context;

    public ClockManager(Context context) {
        this.context = context;
    }

    public void clockIn(){
      clock(1);
    }

    public void clockOut(){
        clock(0);
    }

    private void clock(int event){
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.DATE, System.currentTimeMillis());
        cv.put(CameAndWentProvider.ENTERED, event);
        context.getContentResolver().insert(CameAndWentProvider.URI_CAME_OR_WENT, cv);
    }
}
