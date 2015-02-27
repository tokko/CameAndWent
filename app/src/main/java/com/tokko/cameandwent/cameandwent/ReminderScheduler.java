package com.tokko.cameandwent.cameandwent;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReminderScheduler extends BroadcastReceiver{
    public static final String ACTION_WEEKLY_REMINDER = BuildConfig.APPLICATION_ID+".ACTION_WEEKLY_REMINDER";
    public static final String ACTION_WEEKLY_SUMMARY = BuildConfig.APPLICATION_ID+".ACTION_WEEKLY_SUMMARY";
    private final SharedPreferences defaultPrefs;

    private Context context;

    public ReminderScheduler(Context context){
        this.context = context;
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void scheduleWeeklyReminder(){
        if(!defaultPrefs.getBoolean("enabled", false)) return;
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(context, ReminderScheduler.class).setAction(ACTION_WEEKLY_REMINDER), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(defaultPrefs.getBoolean("weekly_reminders", false)) {
            if(!defaultPrefs.getString("weekly_reminder_time", "").contains(":")) return;
            Calendar c = setTimeAndMinute(defaultPrefs.getString("weekly_reminder_time", "0:0"));
            int weekday = Integer.valueOf(defaultPrefs.getString("weekly_reminder_day", "0"));
            c.set(Calendar.DAY_OF_WEEK, weekday);
            Calendar now = Calendar.getInstance();
            while (c.before(now)) c.add(Calendar.WEEK_OF_YEAR, 1);
            long time = c.getTimeInMillis();
            String timeS = new SimpleDateFormat("yyy-MM-dd HH:mm").format(new Date(c.getTimeInMillis()));
            am.set(AlarmManager.RTC, time, pi);
        }
        else{
            am.cancel(pi);
        }
    }

    private Calendar setTimeAndMinute(String time){
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        int minute = Integer.valueOf(split[1]);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(intent.getAction().equals(ACTION_WEEKLY_REMINDER)){
            if(!defaultPrefs.getBoolean("weekly_reminders", false)) return;
            Notification.Builder nb = new Notification.Builder(context);
            if(defaultPrefs.getBoolean("weekly_reminder_vibrate", false))
                nb.setVibrate(new long[]{0, 1000});
            else
                nb.setVibrate(new long[]{0});
            nb.setContentTitle("Time to submit time report!");
            nb.setSmallIcon(R.drawable.ic_launcher);
            if(defaultPrefs.getBoolean("weekly_reminder_sound", false))
                nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            nb.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_UPDATE_CURRENT));
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(2, nb.build());
        }
        else if(intent.getAction().equals(ACTION_WEEKLY_SUMMARY)){

        }
    }
}
