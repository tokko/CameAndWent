package com.tokko.cameandwent.cameandwent.automaticbreaks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

public class AutomaticBreakManager extends BroadcastReceiver {
    private static final String ACTION_AUTOMATIC_BREAK = "ACTION_AUTOMATIC_BREAK";

    public static void scheduleAutomaticBreaks(Context context){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(ACTION_AUTOMATIC_BREAK), PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        AlarmManager am = ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE));
        if(!pref.getBoolean("breaks_enabled", false)) {
            am.cancel(pendingIntent);
            return;
        }
        String[] startTime = pref.getString("average_break_start", "0:0").split(":");
        int startHour = Integer.valueOf(startTime[0]);
        int startMinute = Integer.valueOf(startTime[1]);
        DateTime dt = TimeConverter.getCurrentTime().withTime(startHour, startMinute, 0, 0);
        if(dt.isBefore(TimeConverter.getCurrentTime())) dt = dt.withFieldAdded(DurationFieldType.days(), 1);

        am.setRepeating(AlarmManager.RTC, dt.getMillis(), DateTimeConstants.MILLIS_PER_DAY, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if(!pref.getBoolean("breaks_enabled", false)) {
            return;
        }
        Cursor c = context.getContentResolver().query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=0", CameAndWentProvider.WENT), null, null);
        if(c.moveToFirst()){
            if(c.getCount() != 1) throw new IllegalStateException("Only one checkin at a time plx.");
            long came = c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            if(came > TimeConverter.getCurrentTime().getMillis()) throw new IllegalStateException("Break before current clockin, wtf?");
            String[] breakStart = pref.getString("average_break_start", "0:0").split(":");
            long breakDuration = TimeConverter.timeIntervalAsLong(pref.getString("average_break_duration", "0:0"));
            int startHour = Integer.valueOf(breakStart[0]);
            int startMinute = Integer.valueOf(breakStart[1]);
            long startTime = TimeConverter.getCurrentTime().withTime(startHour, startMinute, 0, 0).getMillis();
            ContentValues cv = new ContentValues();
            cv.put(CameAndWentProvider.CAME, startTime);
            cv.put(CameAndWentProvider.WENT, startTime + breakDuration);
            cv.put(CameAndWentProvider.ISBREAK, 1);
            cv.put(CameAndWentProvider.DATE, TimeConverter.extractDate(startTime));
            cv.put(CameAndWentProvider.TAG, c.getInt(c.getColumnIndex(CameAndWentProvider.TAG)));
            context.getContentResolver().insert(CameAndWentProvider.URI_LOG_ENTRIES, cv);
        }
    }
}
