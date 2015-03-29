package com.tokko.cameandwent.cameandwent.monthlyexcelreport;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class TimePerWeekCalculator {

    public static Uri BuildExcel(HashMap<String, Integer> data, String title) throws IOException, WriteException {
        final File file = new File(Environment.getExternalStorageDirectory(), title);
        BuildExcel(data, file);
        return Uri.fromFile(file);
    }
    public static WritableWorkbook BuildExcel(HashMap<String, Integer> data, File file) throws IOException, WriteException {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            int col = 0;
            int row = 0;
            for(String s : data.keySet()){
                Label label = new Label(col, row, s);
                sheet.addCell(label);

                Number number = new Number(col++, row+1, data.get(s));
                sheet.addCell(number);
            }
            workbook.write();
            return workbook;
    }


    public static String CreateTitle(long date, String name){
        return String.format("Konsultrapport%s-%s.xls", new SimpleDateFormat("yyyyMMdd").format(new Date(date)), name);
    }

    public static HashMap<String, Integer> extractData(Cursor c){
        HashMap<String, Integer> data = new HashMap<>();
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            String week = String.format("V%s", c.getInt(c.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)));
            int duration = (int) (TimeConverter.longAsHour(c.getLong(c.getColumnIndex(CameAndWentProvider.DURATION))) + 0.5);
            data.put(week, duration);
        }
        return data;
    }
}
