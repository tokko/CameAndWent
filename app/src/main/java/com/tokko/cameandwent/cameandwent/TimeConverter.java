package com.tokko.cameandwent.cameandwent;

import java.util.Calendar;
public class TimeConverter {

    public static long hourAndMinuteToMillis(long date, String time) {
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        int minute = Integer.valueOf(split[1]);
        return hourAndMinuteToMillis(date, hour, minute);
    }

    public static long hourAndMinuteToMillis(int hour, int minute) {
        return hourAndMinuteToMillis(System.currentTimeMillis(), hour, minute);
    }
    public static long hourAndMinuteToMillis(long time, int hour, int minute){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTimeInMillis();
    }

    public static int getHour(){
        return getFieldFromTime(Calendar.HOUR_OF_DAY, System.currentTimeMillis());
    }

    private static int getFieldFromTime(int field, long l) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        return c.get(field);
    }

    public static int getMinute(){
        return getFieldFromTime(Calendar.MINUTE, System.currentTimeMillis());
    }


    public static int currentTimeInMillisToCurrentHours(long millis){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int currentTimeInMillisToCurrentMinutes(long millis){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return c.get(Calendar.MINUTE);
    }

    public static long extractDate(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c.getTimeInMillis();
    }

    public static long weeksToMillis(long weeks){
        return weeks*7*24*60*60*1000;
    }

    public static long timeIntervalAsLong(String time) {
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        int minute = Integer.valueOf(split[1]);
        return hoursAsLong(hour) + minutesAsLong(minute);
    }

    public static long hoursAsLong(int hour){
        return hour*60*60*1000;
    }

    public static long minutesAsLong(int minute){
        return minute*60*1000;
    }

    public static double longAsHour(long time){
        return time/(60D*60D*1000D);
    }

    public static int getCurrentWeek(){
        return extractWeek(System.currentTimeMillis());
    }
    public static int extractWeek(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
}
