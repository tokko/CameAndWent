package com.tokko.cameandwent.cameandwent;

import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeConverterTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
}

    public void testHourAndMinuteToMillis_DateAndTime() throws Exception {
        Random r = new Random();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, r.nextInt(100));
        c.add(Calendar.MONTH, r.nextInt(100));
        c.add(Calendar.WEEK_OF_YEAR, r.nextInt(100));
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        long time = TimeConverter.hourAndMinuteToMillis(c.getTimeInMillis(), hour + ":" + minute);
        assertEquals(c.getTimeInMillis(), time);
    }

    public void testHourAndMinuteToMillis_DateHourMinute() throws Exception {
        Random r = new Random();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, r.nextInt(100));
        c.add(Calendar.MONTH, r.nextInt(100));
        c.add(Calendar.WEEK_OF_YEAR, r.nextInt(100));
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        long time = TimeConverter.hourAndMinuteToMillis(c.getTimeInMillis(), hour, minute);
        assertEquals(c.getTimeInMillis(), time);
    }

    public void testMillisToHours() throws Exception {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        assertEquals(hour, TimeConverter.currentTimeInMillisToCurrentHours(c.getTimeInMillis()));
    }

    public void testMillisToMinutes() throws Exception {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        assertEquals(minute, TimeConverter.currentTimeInMillisToCurrentMinutes(c.getTimeInMillis()));
    }

    public void testExtractDate() throws Exception {
        DateTime dt = new DateTime();
        long time = dt.getMillis();
        dt = dt.withTime(0, 0, 0, 0);
        assertEquals(dt.getMillis(), TimeConverter.extractDate(time));
    }

    public void testWeeksToMillis() throws Exception {
        long millisPerWeek = 1000*60*60*24*7;
        assertEquals(millisPerWeek, TimeConverter.weeksToMillis(1));
        assertEquals(millisPerWeek*2, TimeConverter.weeksToMillis(2));
        assertEquals(millisPerWeek*3, TimeConverter.weeksToMillis(3));
        assertEquals(millisPerWeek*4, TimeConverter.weeksToMillis(4));
        assertEquals(millisPerWeek*5, TimeConverter.weeksToMillis(5));
    }

    public void testTimeIntervalAsLong() throws Exception {
        int hour = 3;
        int minute = 4;
        long interval = hour*60*60*1000 + minute*60*1000;
        assertEquals(interval, TimeConverter.timeIntervalAsLong(hour + ":" + minute));
    }

    public void testExtractWeek() throws Exception {
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);
        assertEquals(week, TimeConverter.extractWeek(c.getTimeInMillis()));
    }

    public void testExtractMonth(){
        DateTime dt = new DateTime();
        int week = dt.getMonthOfYear();
        assertEquals(week, TimeConverter.extractMonth(dt.getMillis()));
    }

    public void testParseDate(){
        DateTime dt = new DateTime();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(dt.getMillis()));
        dt = dt.withTime(0, 0, 0, 0);
        DateTime res = TimeConverter.parseDate(date);
        assertEquals(dt, res);
    }
}