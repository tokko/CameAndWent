package com.tokko.cameandwent.cameandwent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;

public class ClockManager {
    private Context context;
    private SharedPreferences defaultPrefs;
    private static final String PREV_SOUNDMODE_KEY = "prevsoundmodekey";
    private  AudioManager am;
    private SharedPreferences sp;

    public ClockManager(Context context) {
        this.context = context;
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        sp = context.getSharedPreferences("clock", Context.MODE_PRIVATE);
    }

    public void clockIn(){
        clockIn(System.currentTimeMillis());
    }

    public void clockOut(){
       clockOut(System.currentTimeMillis());

    }

    public void clockIn(long time) {
        if(!this.defaultPrefs.getBoolean("clockedIn", false)) {
            ContentValues cv = new ContentValues();
            cv.put(CameAndWentProvider.CAME, time);
            context.getContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
            if (defaultPrefs.getBoolean("soundmode", false)) {
                defaultPrefs.edit().putInt(PREV_SOUNDMODE_KEY, am.getRingerMode()).apply();
                boolean vibrate = defaultPrefs.getBoolean("vibrate", false);
                boolean silent = defaultPrefs.getBoolean("silent", false);
                if (vibrate)
                    am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                else if (silent)
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            this.sp.edit().putBoolean("clockedIn", true).apply();
            postNotification(1, "Arrived at work");
        }
    }

    public void clockOut(long time) {
        if(this.sp.getBoolean("clockedIn", false)) {
            ContentValues cv = new ContentValues();
            cv.put(CameAndWentProvider.WENT, time);
            context.getContentResolver().update(CameAndWentProvider.URI_WENT, cv, CameAndWentProvider.WENT + " = 0 " , null);
            if (defaultPrefs.getBoolean("soundmode", false)) {
                am.setRingerMode(defaultPrefs.getInt(PREV_SOUNDMODE_KEY, AudioManager.RINGER_MODE_NORMAL));
            }
            this.sp.edit().putBoolean("clockedIn", false).commit();
            postNotification(1, "Left work");
        }
    }

    private void postNotification(int id, String s) {
        Notification.Builder nb = new Notification.Builder(context);
        nb.setVibrate(new long[]{0, 1000});
        nb.setContentTitle(s);
        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        nb.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, nb.build());
    }
}
