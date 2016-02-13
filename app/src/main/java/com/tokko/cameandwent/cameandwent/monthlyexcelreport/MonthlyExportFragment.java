package com.tokko.cameandwent.cameandwent.monthlyexcelreport;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import roboguice.fragment.RoboFragment;

public class MonthlyExportFragment extends RoboFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String EXTRA_MONTH = "extra_month";
    private int month;

    public MonthlyExportFragment newInstance(int month){
        MonthlyExportFragment f = new MonthlyExportFragment();
        Bundle b = new Bundle();
        b.putInt(EXTRA_MONTH, month);
        f.setArguments(b);
        return f;
    }

    public MonthlyExportFragment newInstance(){
        return newInstance(TimeConverter.getCurrentTime().getMonthOfYear());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            month = savedInstanceState.getInt(EXTRA_MONTH);
        else if(getArguments() != null)
            month = getArguments().getInt(EXTRA_MONTH);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_MONTH, month);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
        String selection = String.format("%s=?", CameAndWentProvider.MONTH_OF_YEAR);
        String[]selectionArgs = new String[]{String.valueOf(groupCursor.getInt(groupCursor.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)))};
        if(tag != null){
            selection += String.format(" AND %s=?", CameAndWentProvider.TAG);
            selectionArgs = new String[]{selectionArgs[0], tag};
        }
        return getActivity().getContentResolver().query(CameAndWentProvider.URI_MONTHLY_SUMMARY, null, selection, selectionArgs, CameAndWentProvider.WEEK_OF_YEAR);
        */
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
