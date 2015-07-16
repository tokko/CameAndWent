package com.tokko.cameandwent.cameandwent.peaccounting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.tokko.cameandwent.cameandwent.peaccounting.classes.PayrollEvent;
import com.tokko.cameandwent.cameandwent.peaccounting.classes.PayrollEventType;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;
import java.sql.Date;

public class PeAccountingOnClockOutReceiver extends BroadcastReceiver{
    public PeAccountingOnClockOutReceiver(){
    }
    
    @Override
    public void onReceive(Context context, Intent intent){
        long date = TimeConverter.extractDate(TimeConverter.getCurrentTime().getMillis());
        Cursor cursor = context.getContentResolver().query(CameAndWentProvider
                                                                   .URI_DURATIONS_PER_DAY,
                                                           null,
                                                           String.format("%s=?",
                                                                         CameAndWentProvider.DATE),
                                                           new String[]{String.valueOf(date)},
                                                           null);
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String tag = cursor.getString(cursor.getColumnIndex(CameAndWentProvider.TAG));
            Cursor tags = context.getContentResolver().query(CameAndWentProvider.URI_TAGS,
                                                             null,
                                                             String.format("%s=?",
                                                                           CameAndWentProvider.TAG),
                                                             new String[]{tag}, null);
            tags.moveToFirst();
            long tagId = tags.getLong(tags.getColumnIndex(CameAndWentProvider.ID));
            tags.close();
            long duration = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION));
            PayrollEvent payrollEvent = new PayrollEvent();
            payrollEvent.setType(PayrollEventType.WORK_HOUR);
            payrollEvent.setHours(new BigDecimal(duration / DateTimeConstants.MILLIS_PER_HOUR));
            payrollEvent.setDate(new Date(date));
            payrollEvent.setId((int) tagId);

        }
    }
}
