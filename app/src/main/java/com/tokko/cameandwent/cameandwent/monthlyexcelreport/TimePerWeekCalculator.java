package com.tokko.cameandwent.cameandwent.monthlyexcelreport;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;

import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class TimePerWeekCalculator extends BroadcastReceiver{
    public static final String EXTRA_TAG = "EXTRA_TAG";
    public static final String ACTION_SEND = "ACTION_SEND";
    public static final String ACTION_PREVIEW = "ACTION_PREVIEW";

    public static Uri BuildExcel(HashMap<String, Integer> data, String title) {
        final File file = new File(Environment.getExternalStorageDirectory(), title);
        WritableWorkbook workbook = null;
        try {
            workbook = BuildExcel(data, new FileOutputStream(file));
            workbook.write();
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }
    public static WritableWorkbook BuildExcel(HashMap<String, Integer> data, OutputStream outPutStream) throws IOException, WriteException {
            WritableWorkbook workbook = Workbook.createWorkbook(outPutStream);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            int col = 0;
            int row = 0;
            for(String s : data.keySet()){
                Label label = new Label(col, row, s);
                sheet.addCell(label);

                Number number = new Number(col++, row+1, data.get(s));
                sheet.addCell(number);
            }
            return workbook;
    }

    public static boolean isLastWorkDayOfMonth(DateTime dt){
        int day = dt.getDayOfMonth();
        dt = dt.dayOfMonth().withMaximumValue();
        while(dt.getDayOfWeek() == DateTimeConstants.SUNDAY || dt.getDayOfWeek() == DateTimeConstants.SATURDAY) dt = dt.withFieldAdded(DurationFieldType.days(), -1);
        return day == dt.getDayOfMonth();
    }

    public static String CreateTitle(String prefix, long date, String name){
        return String.format("%s%s-%s.xls", prefix, new SimpleDateFormat("yyyyMMdd").format(new Date(date)), name);
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
    public static Intent getDefaultViewIntent(Context context, Uri uri)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
        /*
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Let's probe the intent exactly in the same way as the VIEW action
        String name=(new File(uri.getPath())).getName();
        intent.setDataAndType(uri, getMimeType(name));
        final List<ResolveInfo> lri = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(lri.size() > 0)
            return intent;
        return null;
        */
    }

    public static String getMimeType(String filename)
    {
        String[] s = filename.split(".");
        String extension = s[s.length-1];
        if (extension.length() > 0)
        {
            String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
            if (webkitMimeType != null)
                return webkitMimeType;
        }
        return "*/*";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int tagId = intent.getIntExtra(EXTRA_TAG, -1);
        if(tagId == -1) throw new IllegalStateException("Invalid tag");
        if(intent.getAction().equals(ACTION_SEND)){
           // String recipient = tag.getString(tag.getColumnIndex(CameAndWentProvider.RECIPIENT));
        }
        else if(intent.getAction().equals(ACTION_PREVIEW)){
            Uri uri = BuildExcel(context, TimeConverter.getCurrentTime(), tagId);
            Intent viewIntent = getDefaultViewIntent(context, uri);
            viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewIntent);
        }
    }

    public static Uri BuildExcel(Context context, DateTime dt, int tagId){
        Cursor tag = context.getContentResolver().query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(tagId)}, null, null);
        if(!tag.moveToFirst()) throw new IllegalStateException("No tag found");
        String prefix = tag.getString(tag.getColumnIndex(CameAndWentProvider.TITLE_PREFIX));
        String name = tag.getString(tag.getColumnIndex(CameAndWentProvider.NAME));
        String title = CreateTitle(prefix, dt.getMillis(), name);
        String tagName = tag.getColumnName(tag.getColumnIndex(CameAndWentProvider.TAG));
        Cursor dataC = context.getContentResolver().query(CameAndWentProvider.URI_MONTHLY_SUMMARY, null, String.format("%s=? AND %s=?", CameAndWentProvider.MONTH_OF_YEAR, CameAndWentProvider.TAG), new String[]{String.valueOf(dt.getMonthOfYear()), tagName}, null, null);
        HashMap<String, Integer> data = extractData(dataC);
        data.clear();
        for(int i = 0; i < 100; i++)
            data.put(i+"", i);
        Uri uri = BuildExcel(data, title);
        tag.close();
        dataC.close();
        return uri;
    }
    private static int code = 0;

    public static void showNotifications(Context context){
        Cursor c = context.getContentResolver().query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.REMINDER), new String[]{"1"}, null, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, new Intent(ACTION_SEND).putExtra(EXTRA_TAG, c.getLong(c.getColumnIndex(CameAndWentProvider.ID))), PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent previewPendingIntent = PendingIntent.getBroadcast(context, -code, new Intent(ACTION_PREVIEW).putExtra(EXTRA_TAG, c.getLong(c.getColumnIndex(CameAndWentProvider.ID))), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder nb = new NotificationCompat.Builder(context);
            nb.setContentTitle("Send report?");
            nb.setContentText(String.format("Time to submit report to: %s", c.getString(c.getColumnIndex(CameAndWentProvider.TAG))));
            nb.setSmallIcon(R.drawable.ic_launcher);
            nb.setAutoCancel(true);
            nb.addAction(R.drawable.ic_launcher, "Send", pendingIntent);
            nb.addAction(R.drawable.ic_launcher, "Preview", previewPendingIntent);
            Notification notification = nb.build();
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(code++, notification);
        }
        c.close();

    }
}
