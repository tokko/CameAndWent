package com.tokko.cameandwent.cameandwent;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

public class CountDownManager extends BroadcastReceiver{
    public static final int NOTIFICATION_ID = 4;
    private Context context;
    private IntentFilter intentFilter;
    private TickReceiver tickReceiver;
    private CountDownDatabaseObserver obs;

    public CountDownManager(Context context){
        this.context = context;
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        obs = new CountDownDatabaseObserver(new Handler(), context);
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
        else
            throw new IllegalStateException("Unknown intent");
    }

    private void registerObservers(Context context) {
        if(tickReceiver == null) {
            tickReceiver = new TickReceiver();
            context.registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        }

        context.getContentResolver().registerContentObserver(CameAndWentProvider.URI_GET_LOG_ENTRIES, false, obs);
        context.getContentResolver().registerContentObserver(CameAndWentProvider.URI_GET_DURATIONS, false, obs);

    }

    private void unregisterObservers(Context context) {
        if(tickReceiver != null) {
            context.unregisterReceiver(tickReceiver);
            tickReceiver = null;
        }
        context.getContentResolver().unregisterContentObserver(obs);
    }

    public long startCountDown(){
        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("countdown", false)) return -1;
        long currentTime = TimeConverter.getCurrentTime().getMillis();
        context.registerReceiver(this, intentFilter);
        registerObservers(context);
        updateNotification(context);
        return currentTime;
    }
    private NotificationManager getNotificationManager(Context context){
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void stopCountDown(){
        unregisterObservers(context);
        try {
            context.unregisterReceiver(this);
        }
        catch (Exception e){}
        getNotificationManager(context).cancel(NOTIFICATION_ID);
    }

    private void updateNotification(Context context){
        if(!context.getSharedPreferences(ClockManager.CLOCK_PREFS, Context.MODE_PRIVATE).getBoolean(ClockManager.PREF_CLOCKED_IN, false)) return;
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!defaultPreferences.getBoolean("countdown", false)){
            getNotificationManager(context).cancel(NOTIFICATION_ID);
            return;
        }
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setContentTitle("Workday countdown");
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilder.setOngoing(true);
        int duration = (int) TimeConverter.timeIntervalAsLong(defaultPreferences.getString("daily_work_duration", "0:0"));
        int currentDuration = (int) getCurrentDuration(context);
        int remainder = duration - currentDuration;
        notificationBuilder.setProgress(duration, currentDuration, false);
        notificationBuilder.setContentText(String.format("Time remaining: %s", TimeConverter.formatInterval((long)remainder)));
        getNotificationManager(context).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private long getCurrentDuration(Context context){
        Cursor c = context.getContentResolver().query(CameAndWentProvider.URI_GET_LOG_ENTRIES, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(TimeConverter.extractDate(TimeConverter.getCurrentTime().getMillis()))}, null, null);
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
    public class TickReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
                updateNotification(context);
            }
            else
                throw new IllegalStateException("Unknown action");
        }
    }
}
