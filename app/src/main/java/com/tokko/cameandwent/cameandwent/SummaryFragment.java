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
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTimeConstants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

public class SummaryFragment extends RoboDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String EXTRA_MONTHLY = "extra_monthly";
    private SummaryCursorAdapter adapter;
    @InjectView(android.R.id.list) private ExpandableListView expandableListView;
    @InjectView(R.id.summaryFragmentTagSpinner) private Spinner tagSpinner;
    private boolean monthly;
    private CursorAdapter spinnerAdapter;

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
        spinnerAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, null, new String[]{CameAndWentProvider.TAG}, new int[]{android.R.id.text1}, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_MONTHLY, monthly);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summaryfragment, null);
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
            tagSpinner.setVisibility(View.VISIBLE);
            tagSpinner.setAdapter(spinnerAdapter);
            adapter = new MonthlySummaryAdapter(getActivity());
        }
        expandableListView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(-1, null, this);
        if(monthly)
            getLoaderManager().initLoader(-2, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(-1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(!monthly) {
            if(id == -1) {
                CursorLoader cl = new CursorLoader(getActivity());
                cl.setUri(CameAndWentProvider.URI_WEEKS);
                return cl;
            }
            else{
                CursorLoader cl = new CursorLoader(getActivity());
                cl.setUri(CameAndWentProvider.URI_LOG_ENTRIES);
                cl.setSelection(String.format("%s=?", CameAndWentProvider.WEEK_OF_YEAR));
                cl.setSelectionArgs(new String[]{String.valueOf(id)});
                return cl;
            }
        }
        else{
            CursorLoader cl = new CursorLoader(getActivity());
            switch (id){
                case -2:
                    cl.setUri(CameAndWentProvider.URI_TAGS);
                    return cl;
                default:
                    cl.setUri(CameAndWentProvider.URI_MONTHS);
                    return cl;
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() >= 0)
            adapter.setChildrenCursor(loader.getId(), data);
        else if(loader.getId() == -1)
            adapter.setGroupCursor(data);
        else if(loader.getId() == -2)
            spinnerAdapter.swapCursor(data);
        if(expandableListView.getCount() > 0)
            expandableListView.expandGroup(expandableListView.getCount() - 1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() > 0)
            adapter.setChildrenCursor(loader.getId(), null);
        else if(loader.getId() == -1)
            adapter.setGroupCursor(null);
        else if(loader.getId() == -2)
            spinnerAdapter.swapCursor(null);
    }

    private class SummaryCursorAdapter extends CursorTreeAdapter{
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        private Context context;

        public SummaryCursorAdapter(Context context) {
            super(null, context);
            this.context = context;
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            int week = groupCursor.getInt(groupCursor.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR));
            return context.getContentResolver().query(CameAndWentProvider.URI_DURATIONS, null, String.format("%s=?", CameAndWentProvider.WEEK_OF_YEAR), new String[]{String.valueOf(week)}, null);
        }

        @Override
        protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
            return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_expandable_list_item_1, null);
        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
            ((TextView)view.findViewById(android.R.id.text1)).setText(String.format("v%d", cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR))));
        }

        @Override
        protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
            return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null);
        }

        @Override
        protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
            String date = sdf.format(new Date(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DATE))));
            double duration = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION))/ (double)DateTimeConstants.MILLIS_PER_HOUR;
            String durationS = new DecimalFormat("#.0").format(duration) + "h";
            if(duration < 0)
                durationS = "Currently at work";
            String tag = cursor.getString(cursor.getColumnIndex(CameAndWentProvider.TAG));
            tag = tag != null ? ("\nTag: " + tag):null;
            ((TextView)view.findViewById(android.R.id.text1)).setText(date + "\n" + durationS + (tag != null ? tag : ""));
        }
    }

    private class MonthlySummaryAdapter extends SummaryCursorAdapter{

        public MonthlySummaryAdapter(Context context) {
            super(context);
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            if(groupCursor.getCount() <= 0) return null;
            return getActivity().getContentResolver().query(CameAndWentProvider.URI_MONTHLY_SUMMARY, null, String.format("%s=?", CameAndWentProvider.MONTH_OF_YEAR), new String[]{String.valueOf(groupCursor.getInt(groupCursor.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)))}, null);
        }

        @Override
        protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
            return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null);
        }

        @Override
        protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
            int week = cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR));
            double duration = TimeConverter.longAsHour(cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.DURATION)));
            ((TextView)view.findViewById(android.R.id.text1)).setText(String.format("v%d: %sh", week, new DecimalFormat("##.0").format(duration)));
        }

        @Override
        protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
            return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_expandable_list_item_1, null);
        }

        @Override
        public void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
            ((TextView)view.findViewById(android.R.id.text1)).setText(String.format("%s", getResources().getStringArray(R.array.months)[cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR))]));
        }
    }
}
