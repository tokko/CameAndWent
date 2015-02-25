package com.tokko.cameandwent.cameandwent;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import roboguice.fragment.RoboDialogFragment;

public class SummaryFragment extends RoboDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String EXTRA_MONTHLY = "extra_monthly";
    private SummaryCursorAdapter adapter;
    private ListView listView;
    private boolean monthly;


    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static SummaryFragment newMonthlyInstance(){
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_MONTHLY, true);
        fragment.setArguments(args);
        return fragment;
    }

    public SummaryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            monthly = savedInstanceState.getBoolean(EXTRA_MONTHLY, false);
        }
        else if(getArguments() != null){
            monthly = getArguments().getBoolean(EXTRA_MONTHLY, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_MONTHLY, monthly);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listView = new ListView(getActivity());
        return listView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!monthly) {
            getDialog().setTitle("Weekly summary");
            adapter = new SummaryCursorAdapter(getActivity());
        }
        else {
            getDialog().setTitle("Monthly summary");
            adapter = new MonthlySummaryAdapter(getActivity());
        }
        listView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(!monthly) {
            CursorLoader cl = new CursorLoader(getActivity());
            cl.setUri(CameAndWentProvider.URI_GET_LOG_ENTRIES);
            cl.setSelection(String.format("%s>=?", CameAndWentProvider.DATE));
            cl.setSelectionArgs(new String[]{String.valueOf(System.currentTimeMillis() - TimeConverter.weeksToMillis(1))});
            return cl;
        }
        else{
            CursorLoader cl = new CursorLoader(getActivity());
            cl.setUri(CameAndWentProvider.URI_GET_LOG_ENTRIES);
            cl.setSelection(String.format("%s>=?", CameAndWentProvider.DATE));
            cl.setSelectionArgs(new String[]{String.valueOf(System.currentTimeMillis() - TimeConverter.weeksToMillis(1))});
            return cl;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private class SummaryCursorAdapter extends CursorAdapter{
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        public SummaryCursorAdapter(Context context) {
            super(context, null, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String date = sdf.format(new Date(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DATE))));
            double duration = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION))/(1000D*60D*60D);
            String durationS = new DecimalFormat("#.00").format(duration) + "h";
            if(duration < 0)
                durationS = "Currently at work";
            ((TextView)view.findViewById(android.R.id.text1)).setText(date + "\n" + durationS);
        }
    }

    private class MonthlySummaryAdapter extends SummaryCursorAdapter{

        public MonthlySummaryAdapter(Context context) {
            super(context);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int week = cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR));
            int duration = cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.DURATION));
            ((TextView)view.findViewById(android.R.id.text1)).setText(String.format("v%d: %dh", week, duration));
        }
    }
}
