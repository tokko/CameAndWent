package com.tokko.cameandwent.cameandwent.summaries;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

public class SummaryFragment extends RoboDialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {
    private static final String EXTRA_MONTHLY = "extra_monthly";
    private static final String ARG_TAG = "arg_tag";
    private SummaryCursorAdapter adapter;
    @InjectView(android.R.id.list) private ExpandableListView expandableListView;
    @InjectView(R.id.summaryFragmentTagSpinner) private Spinner tagSpinner;
    private boolean monthly;
    private CursorAdapter spinnerAdapter;
    private String tag;

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
            tagSpinner.setOnItemSelectedListener(this);
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
                cl.setUri(CameAndWentProvider.URI_MONTHS);
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
            if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("summary_last_week", false) && expandableListView.getCount() > 2)
                expandableListView.expandGroup(expandableListView.getCount() - 2);
            else    expandableListView.expandGroup(expandableListView.getCount() - 1);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = spinnerAdapter.getCursor();
        int pos = c.getPosition();
        c.moveToPosition(position);
        tag = c.getString(c.getColumnIndex(CameAndWentProvider.TAG));
        c.moveToPosition(pos);
        getLoaderManager().destroyLoader(-1);
        getLoaderManager().initLoader(-1, null, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class SummaryCursorAdapter extends CursorTreeAdapter{
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM-EE");
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
            DateTime date = new DateTime(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DATE)));
            DateTime dateOfMonday = TimeConverter.getMondayOfWeek(date);
            DateTime dateOfFriday = TimeConverter.getFridayOfWeek(date);
            String monday = new SimpleDateFormat("dd/MM").format(new Date(dateOfMonday.getMillis()));
            String friday = new SimpleDateFormat("dd/MM").format(new Date(dateOfFriday.getMillis()));
            ((TextView)view.findViewById(android.R.id.text1)).setText(String.format("v%d, %s-%s", cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.WEEK_OF_YEAR)), monday, friday));
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
            String selection = String.format("%s=?", CameAndWentProvider.MONTH_OF_YEAR);
            String[]selectionArgs = new String[]{String.valueOf(groupCursor.getInt(groupCursor.getColumnIndex(CameAndWentProvider.MONTH_OF_YEAR)))};
            if(tag != null){
                selection += String.format(" AND %s=?", CameAndWentProvider.TAG);
                selectionArgs = new String[]{selectionArgs[0], tag};
            }
            return getActivity().getContentResolver().query(CameAndWentProvider.URI_MONTHLY_SUMMARY, null, selection, selectionArgs, CameAndWentProvider.WEEK_OF_YEAR);
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
