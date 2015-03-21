package com.tokko.cameandwent.cameandwent.notifications;

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

import com.tokko.cameandwent.cameandwent.MainActivity;
import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

public class ReminderScheduler extends BroadcastReceiver{
    public static final String ACTION_WEEKLY_REMINDER = "ACTION_WEEKLY_REMINDER";
    public static final String ACTION_MONTHLY_REMINDER = "ACTION_MONTHLY_REMINDER";
    private SharedPreferences defaultPrefs;
    private NotificationManager nm;
    public static final int monthlyReminderNotificationId = 3;
    public static final int weeklyReminderNotificationId = 2;
    private Context context;

    public ReminderScheduler(){}
    public ReminderScheduler(Context context){
        this.context = context;
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public long scheduleWeeklyReminder(){
        return scheduleWeeklyReminder(context);
    }

    public long scheduleWeeklyReminder(Context context){
        if(!defaultPrefs.getBoolean("enabled", false)) return -1;
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(context, ReminderScheduler.class).setAction(ACTION_WEEKLY_REMINDER), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(defaultPrefs.getBoolean("weekly_reminders", false)) {
            if(!defaultPrefs.getString("weekly_reminder_time", "").contains(":")) return -1;
            DateTime dt = setTimeAndMinute(defaultPrefs.getString("weekly_reminder_time", "0:0"));
            int weekday = Integer.valueOf(defaultPrefs.getString("weekly_reminder_day", "0"));
            DateTime now = TimeConverter.getCurrentTime();
            while (dt.isBefore(now)) dt = dt.withFieldAdded(DurationFieldType.days(), 1);
            while (dt.getDayOfWeek() != weekday) dt = dt.withFieldAdded(DurationFieldType.days(), 1);
            long time = dt.getMillis();
            am.set(AlarmManager.RTC, time, pi);
            return time;
        }
        else{
            am.cancel(pi);
            return -1;
        }
    }
    public long scheduleMonthlyReminder(){
        return scheduleMonthlyReminder(context);
    }

    public long scheduleMonthlyReminder(Context context){
        return scheduleMonthlyReminder(context, TimeConverter.getCurrentTime());
    }
    public long scheduleMonthlyReminder(Context context, DateTime dt){
        if(!defaultPrefs.getBoolean("enabled", false)) return -1;
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(context, ReminderScheduler.class).setAction(ACTION_MONTHLY_REMINDER), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(defaultPrefs.getBoolean("monthly_reminders", false)) {
            if(!defaultPrefs.getString("monthly_reminder_time", "").contains(":")) return -1;
            dt = setTimeAndMinute(dt, defaultPrefs.getString("monthly_reminder_time", "0:0"))
                    .dayOfMonth().withMaximumValue();
            DateTime now = TimeConverter.getCurrentTime();
            while(dt.getDayOfWeek() == DateTimeConstants.SUNDAY || dt.getDayOfWeek() == DateTimeConstants.SATURDAY) dt = dt.withFieldAdded(DurationFieldType.days(), -1);
            if(dt.isBefore(now)) return scheduleMonthlyReminder(context, now.withFieldAdded(DurationFieldType.months(), 1));
            long time = dt.getMillis();
            am.set(AlarmManager.RTC, time, pi);
            return dt.getMillis();
        }
        else{
            am.cancel(pi);
            return -1;
        }
    }

    private DateTime setTimeAndMinute(String time) {
        return setTimeAndMinute(TimeConverter.getCurrentTime(), time);
    }
    private DateTime setTimeAndMinute(DateTime dt, String time){
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        int minute = Integer.valueOf(split[1]);
        dt = dt.withHourOfDay(hour);
        dt = dt.withMinuteOfHour(minute);
        return dt;
    }

    public Notification buildWeeklyNotification(Context context){
        if(defaultPrefs == null)
            defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Notification.Builder nb = new Notification.Builder(context);
        if(defaultPrefs.getBoolean("weekly_reminder_vibrate", false))
            nb.setVibrate(new long[]{0, 1000});
        else
            nb.setVibrate(new long[]{0});
        nb.setContentTitle("Time to submit time report!");
        nb.setSmallIcon(R.drawable.ic_launcher);
        if(defaultPrefs.getBoolean("weekly_reminder_sound", false))
            nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        nb.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setAction(MainActivity.ACTION_WEEKLY_SUMMARY), PendingIntent.FLAG_UPDATE_CURRENT));
        return nb.build();
    }

    public Notification buildMonthlyNotification(Context context){
        if(defaultPrefs == null)
            defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Notification.Builder nb = new Notification.Builder(context);
        if(defaultPrefs.getBoolean("monthly_reminder_vibrate", false))
            nb.setVibrate(new long[]{0, 1000});
        else
            nb.setVibrate(new long[]{0});
        nb.setContentTitle("Time to finalize monthly report!");
        nb.setSmallIcon(R.drawable.ic_launcher);
        if(defaultPrefs.getBoolean("monthly_reminder_sound", false))
            nb.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        nb.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setAction(MainActivity.ACTION_MONTHLY_SUMMARY), PendingIntent.FLAG_UPDATE_CURRENT));
        return nb.build();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(intent.getAction().equals(ACTION_WEEKLY_REMINDER)){
            if(!defaultPrefs.getBoolean("weekly_reminders", false)) return;
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(weeklyReminderNotificationId, buildWeeklyNotification(context));
        }
        else if(intent.getAction().equals(ACTION_MONTHLY_REMINDER)){
            if(!defaultPrefs.getBoolean("monthly_reminders", false)) return;
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(monthlyReminderNotificationId, buildMonthlyNotification(context));
        }
        scheduleWeeklyReminder(context);
        scheduleMonthlyReminder(context);
    }
}
