package com.tokko.cameandwent.cameandwent;

import com.tokko.cameandwent.cameandwent.monthlyexcelreport.TimePerWeekCalculator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class TimePerWeekCalculatorTests extends TestCase {

    @Test
    public void testShit(){
        HashMap<String, Integer> data = new HashMap<>();
        data.put("1", 1);
        data.put("2", 2);
        data.put("3", 3);
        data.put("4", 4);
        data.put("5", 5);
        try {
            WritableWorkbook book = TimePerWeekCalculator.BuildExcel(data, new File(""));
            Sheet sheet = book.getSheet(0);
            int col = 0;
            for(String s : data.keySet()){
                Cell[] column = sheet.getColumn(col++);
                assertEquals(s, column[0].getContents());
                assertEquals(data.get(s), Integer.valueOf(column[1].getContents()));
            }
        } catch (IOException | WriteException ignored) {
        }
    }

    @Test
    public void testIsLastWorkDayOfMonth(){
        DateTime dt = new DateTime().withDate(2015, 2, 27);
        Assert.assertTrue(TimePerWeekCalculator.isLastWorkDayOfMonth(dt));

        dt = new DateTime().withDate(2015, 2, 26);
        Assert.assertFalse(TimePerWeekCalculator.isLastWorkDayOfMonth(dt));
        dt = new DateTime().withDate(2015, 2, 28);
        Assert.assertFalse(TimePerWeekCalculator.isLastWorkDayOfMonth(dt));
        dt = new DateTime().withDate(2015, 2, 25);
        Assert.assertFalse(TimePerWeekCalculator.isLastWorkDayOfMonth(dt));
        dt = new DateTime().withDate(2015, 3, 1);
        Assert.assertFalse(TimePerWeekCalculator.isLastWorkDayOfMonth(dt));
        dt = new DateTime().withDate(2015, 3, 2);
        Assert.assertFalse(TimePerWeekCalculator.isLastWorkDayOfMonth(dt));

    }
}
