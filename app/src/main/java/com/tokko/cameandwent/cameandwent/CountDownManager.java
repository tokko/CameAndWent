package com.tokko.cameandwent.cameandwent;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

public class CountDownManager extends BroadcastReceiver{
    public static final int NOTIFICATION_ID = 4;
    private Context context;
    private IntentFilter intentFilter;
    private TickReceiver tickReceiver;

    public CountDownManager(Context context){
        this.context = context;
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
           unregisterTickReceiver(context);
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            updateNotification(context);
            registerTimeTickReceiver(context);
        }
        else
            throw new IllegalStateException("Unknown intent");
    }

    private void unregisterTickReceiver(Context context) {
        if(tickReceiver != null) {
            context.unregisterReceiver(tickReceiver);
            tickReceiver = null;
        }
    }

    private void registerTimeTickReceiver(Context context) {
        if(tickReceiver == null) {
            tickReceiver = new TickReceiver();
            context.registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
    }

    public long startCountDown(){
        long currentTime = TimeConverter.getCurrentTime().getMillis();
        context.registerReceiver(this, intentFilter);
        registerTimeTickReceiver(context);
        updateNotification(context);
        return currentTime;
    }
    private NotificationManager getNotificationManager(Context context){
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void stopCountDown(){
        getNotificationManager(context).cancel(NOTIFICATION_ID);
        context.unregisterReceiver(this);
        unregisterTickReceiver(context);
    }

    private void updateNotification(Context context){
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(!defaultPreferences.getBoolean("countdown", false)){
            getNotificationManager(context).cancel(NOTIFICATION_ID);
            return;
        }
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setContentTitle("Workday countdown");
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilder.setOngoing(true);
        long duration = TimeConverter.timeIntervalAsLong(defaultPreferences.getString("daily_work_duration", "0:0"));
        long currentDuration = getCurrentDuration(context);
        long remainder = duration - currentDuration;
        double progress = ((double)currentDuration/duration);
        notificationBuilder.setProgress(100, ((int)progress*100), false);
        notificationBuilder.setContentText(String.format("Time remaining: %s", TimeConverter.formatInterval(remainder)));
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
                time += came - went;
            }
            else
                time += -(came - went);
        }
        c.close();
        return time;

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
