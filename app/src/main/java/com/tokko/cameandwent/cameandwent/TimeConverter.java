package com.tokko.cameandwent.cameandwent;

import org.joda.time.DateTime;
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
        DateTime dt = new DateTime(time);
        dt = dt.withMillisOfSecond(0);
        dt = dt.withSecondOfMinute(0);
        dt = dt.withHourOfDay(hour);
        dt = dt.withMinuteOfHour(minute);
        return dt.getMillis();
      }

    public static int getHour(){
        return new DateTime().getHourOfDay();
    }

    public static int getMinute(){
        return new DateTime().getMinuteOfHour();
    }


    public static int currentTimeInMillisToCurrentHours(long millis){
       return new DateTime(millis).getHourOfDay();
    }

    public static int currentTimeInMillisToCurrentMinutes(long millis){
        return new DateTime(millis).getMinuteOfHour();
    }

    public static long extractDate(long time){
        DateTime dt = new DateTime(time);
        dt = dt.withTime(0, 0, 0, 0);
        return dt.getMillis();
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
       return new DateTime(time).getWeekOfWeekyear();
    }
}
