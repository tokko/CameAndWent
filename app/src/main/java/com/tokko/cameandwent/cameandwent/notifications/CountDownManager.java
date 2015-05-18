package com.tokko.cameandwent.cameandwent.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.tokko.cameandwent.cameandwent.ClockManager;
import com.tokko.cameandwent.cameandwent.MainActivity;
import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountDownManager extends BroadcastReceiver{
    public static final String ACTION_COUNTDOWN_TICK = "com.tokko.cameandwent.ACTION_COUNTDOWN_TICK";
    public static final int NOTIFICATION_ID = 4;
    private static final int REQUEST_CODE = 12;
    private Context context;
    private CountDownDatabaseObserver obs;
    private PendingIntent countDownPendingIntent;

    public CountDownManager(){}
    
    public CountDownManager(Context context){
        this.context = context.getApplicationContext();
        obs = new CountDownDatabaseObserver(new Handler(), context);
        countDownPendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, new Intent(ACTION_COUNTDOWN_TICK), 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
           unregisterObservers(context);
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            updateNotification(context);
            registerObservers(context);
        }
        else if(intent.getAction().equals(ACTION_COUNTDOWN_TICK))
            updateNotification(context);
        else
            throw new IllegalStateException("Unknown intent");
    }

    private void registerObservers(Context context) {
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC, TimeConverter.getCurrentTime().getMillis(), DateTimeConstants.MILLIS_PER_MINUTE, countDownPendingIntent);
        context.getApplicationContext().getContentResolver().registerContentObserver(CameAndWentProvider.URI_DURATIONS, false, obs);
    }

    private void unregisterObservers(Context context) {
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.cancel(countDownPendingIntent);
        context.getApplicationContext().getContentResolver().unregisterContentObserver(obs);
    }

    public long startCountDown(){
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("countdown", false)) return -1;
        if(!context.getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).getBoolean(ClockManager.PREF_CLOCKED_IN, false)) return -1;
        long currentTime = TimeConverter.getCurrentTime().getMillis();
        registerObservers(context);
        updateNotification(context);
        return currentTime;
    }
    private NotificationManager getNotificationManager(Context context){
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void stopCountDown(){
        unregisterObservers(context);
        getNotificationManager(context).cancel(NOTIFICATION_ID);
    }

    private void updateNotification(Context context){
        if(!context.getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).getBoolean(ClockManager.PREF_CLOCKED_IN, false)) return;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!defaultPreferences.getBoolean("countdown", false)){
            getNotificationManager(context).cancel(NOTIFICATION_ID);
            return;
        }
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setContentTitle("Workday countdown");
        notificationBuilder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setContentIntent(pendingIntent);
        int duration = (int) TimeConverter.timeIntervalAsLong(defaultPreferences.getString("daily_work_duration", "0:0"));
        int currentDuration = (int) getCurrentDuration(context);
        int remainder = duration - currentDuration;
        long timeToLeave = TimeConverter.getCurrentTime().getMillis() + remainder;
        DateTime dt = new DateTime(timeToLeave);
        String timeToLeaveString = "You may leave by: " + new SimpleDateFormat("HH:mm").format(new Date(timeToLeave));
        if(remainder <= 0)
            timeToLeaveString = "You may leave now";
        notificationBuilder.setProgress(duration, currentDuration, false);
        notificationBuilder.setContentText(String.format("Time remaining: %s\n%s", TimeConverter.formatInterval((long) -remainder), timeToLeaveString));
        getNotificationManager(context).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private long getCurrentDuration(Context context){
        Cursor c = context.getContentResolver().query(CameAndWentProvider.URI_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(TimeConverter.getCurrentTime().getMillis()))}, null, null);
        long time = 0;
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            long went = c.getLong(c.getColumnIndex(CameAndWentProvider.WENT));
            long came =  c.getLong(c.getColumnIndex(CameAndWentProvider.CAME));
            if(c.getInt(c.getColumnIndex(CameAndWentProvider.ISBREAK)) == 0){
                if(went == 0)
                    went = TimeConverter.getCurrentTime().getMillis();
                time += went - came;
            }
            else
                time -= went - came;
        }
        c.close();
        return time;

    }

    public class CountDownDatabaseObserver extends ContentObserver{

        private Context context;

        public CountDownDatabaseObserver(Handler handler,Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            updateNotification(context);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            updateNotification(context);
        }
    }
}
