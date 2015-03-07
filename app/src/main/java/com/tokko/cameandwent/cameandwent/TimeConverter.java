package com.tokko.cameandwent.cameandwent;

import com.google.inject.Singleton;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.concurrent.TimeUnit;

@Singleton
public class TimeConverter {
    public static long CURRENT_TIME = -1;

    public static long hourAndMinuteToMillis(long date, String time) {
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        int minute = Integer.valueOf(split[1]);
        return hourAndMinuteToMillis(date, hour, minute);
    }

    public static DateTime getCurrentTime(){
        if(CURRENT_TIME > -1)
            return new DateTime(CURRENT_TIME);
        return new DateTime();
    }

    public static long hourAndMinuteToMillis(int hour, int minute) {
        return hourAndMinuteToMillis(getCurrentTime().getMillis(), hour, minute);
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
        return getCurrentTime().getHourOfDay();
    }

    public static int getMinute(){
        return getCurrentTime().getMinuteOfHour();
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
        return weeks*DateTimeConstants.MILLIS_PER_WEEK;
    }

    public static long timeIntervalAsLong(String time) {
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        int minute = Integer.valueOf(split[1]);
        return hoursAsLong(hour) + minutesAsLong(minute);
    }

    public static long hoursAsLong(int hour){
        return hour* DateTimeConstants.MILLIS_PER_HOUR;
    }

    public static long minutesAsLong(int minute){
        return minute*DateTimeConstants.MILLIS_PER_MINUTE;
    }

    public static double longAsHour(long time){
        return time/DateTimeConstants.MILLIS_PER_HOUR;
    }

    public static int extractWeek(long time) {
       return new DateTime(time).getWeekOfWeekyear();
    }

    public static int extractMonth(long time) {
        return new DateTime(time).getMonthOfYear();
    }

    public static DateTime parseDate(String date){
        String[] split = date.split("-");
        int year = Integer.valueOf(split[0]);
        int month  = Integer.valueOf(split[1]);
        int day = Integer.valueOf(split[2]);
        return new DateTime(year, month, day, 0, 0, 0, 0);
    }

    public static String formatInterval(long l)
    {
        String prefix = "";
        if(l < 0){
            l *= -1;
            prefix = "-";
        }
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        return String.format(prefix+"%02d:%02d", hr, min);
    }
}
