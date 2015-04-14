package com.tokko.cameandwent.cameandwent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tokko.cameandwent.cameandwent.notifications.CountDownManager;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

public class ClockManager {
    public static final String CLOCK_PREFS = "clock";
    public static final String PREF_CLOCKED_IN = "clockedIn";
    private Context context;
    private SharedPreferences defaultPrefs;
    private static final String PREV_SOUNDMODE_KEY = "prevsoundmodekey";
    private  AudioManager am;
    private SharedPreferences sp;
    private CountDownManager countDownManager;

    public ClockManager(Context context) {
        this.context = context;
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        sp = context.getSharedPreferences(CLOCK_PREFS, Context.MODE_PRIVATE);
        countDownManager = new CountDownManager(context);
    }

    public void clockIn(long id){
        clockIn(TimeConverter.getCurrentTime().getMillis(), id);
    }

    public void clockOut(){
       clockOut(TimeConverter.getCurrentTime().getMillis());

    }

    public void clockIn(long time, long tagId) {
        if(!defaultPrefs.getBoolean("enabled", false)) return;
        if(!this.sp.getBoolean(PREF_CLOCKED_IN, false)) {
            this.sp.edit().putBoolean(PREF_CLOCKED_IN, true).apply();
            ContentValues cv = new ContentValues();
            cv.put(CameAndWentProvider.CAME, time);
            cv.put(CameAndWentProvider.TAG, tagId);
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
            postNotification(1, "Arrived at work");
            countDownManager.startCountDown();
        }
    }

    public void clockOut(long time) {
        if(this.sp.getBoolean(PREF_CLOCKED_IN, false)) {
            this.sp.edit().putBoolean(PREF_CLOCKED_IN, false).commit();
            ContentValues cv = new ContentValues();
            cv.put(CameAndWentProvider.WENT, time);
            context.getContentResolver().update(CameAndWentProvider.URI_WENT, cv, CameAndWentProvider.WENT + " = 0 " , null);
            if (defaultPrefs.getBoolean("soundmode", false)) {
                am.setRingerMode(defaultPrefs.getInt(PREV_SOUNDMODE_KEY, AudioManager.RINGER_MODE_NORMAL));
            }
            postNotification(1, "Left work");
            countDownManager.stopCountDown();
        }
    }

    private void postNotification(int id, String s) {
        if(!defaultPrefs.getBoolean("notifications_enabled", false)) return;
        Notification.Builder nb = new Notification.Builder(context);
        if(defaultPrefs.getBoolean("notifications_vibrate", false))
            nb.setVibrate(new long[]{0, 1000});
        else
            nb.setVibrate(new long[]{0});
        nb.setContentTitle(s);
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.ic_launcher);
        if(defaultPrefs.getBoolean("notifications_sound", false))
            nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        nb.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, nb.build());
    }
}
