package com.tokko.cameandwent.cameandwent;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;

public class ClockManager {
    private Context context;
    private SharedPreferences sp;
    private static final String PREV_SOUNDMODE_KEY = "prevsoundmodekey";
    private  AudioManager am;

    public ClockManager(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
        if(sp.getBoolean("soundmode", false)){
            sp.edit().putInt(PREV_SOUNDMODE_KEY, am.getRingerMode()).apply();
            boolean vibrate = sp.getBoolean("vibrate", false);
            boolean silent = sp.getBoolean("silent", false);
            if(vibrate)
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            else if(silent)
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
        postNotification(1, "Arrived at work");
    }

    public void clockOut(long time) {
        ContentValues cv = new ContentValues();
        cv.put(CameAndWentProvider.WENT, time);
        context.getContentResolver().update(CameAndWentProvider.URI_WENT, cv, null, null);
        if(sp.getBoolean("soundmode", false)){
            am.setRingerMode(sp.getInt(PREV_SOUNDMODE_KEY, AudioManager.RINGER_MODE_NORMAL));
        }
        postNotification(1, "Left work");
    }

    private void postNotification(int id, String s) {
        Notification.Builder nb = new Notification.Builder(context);
        nb.setVibrate(new long[]{0, 1000});
        nb.setContentTitle(s);
        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, nb.build());
    }
}
