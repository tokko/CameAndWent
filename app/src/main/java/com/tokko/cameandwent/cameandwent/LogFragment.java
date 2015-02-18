package com.tokko.cameandwent.cameandwent;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LogFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, View.OnLongClickListener, TimePickerDialog.OnTimeSetListener {
    private LogCursorTreeAdapter adapter;
    private ClockManager cm;
    private ToggleButton tb;
    private ExpandableListView listView;
    private LogFragmentHost host;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cm = new ClockManager(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@SuppressWarnings("NullableProblems") LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        (tb = ((ToggleButton) v.findViewById(R.id.clock_button))).setOnClickListener(this);
        tb.setOnLongClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new LogCursorTreeAdapter(null, getActivity(), this);
        listView = (ExpandableListView) getListView();
        listView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Loader loader = getLoaderManager().getLoader(-1);
        if (loader != null && !loader.isReset()) {
            getLoaderManager().restartLoader(-1, null, this);
        } else {
            getLoaderManager().initLoader(-1, null, this);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_log:
                getActivity().getContentResolver().delete(CameAndWentProvider.URI_DELETE_ALL, null, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logfragment_menu, menu);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == -1) {
            CursorLoader cl = new CursorLoader(getActivity());
            cl.setUri(CameAndWentProvider.URI_GET_LOG_ENTRIES);
            cl.setSortOrder(CameAndWentProvider.DATE + " ASC");
            return cl;
        }
        else{
            CursorLoader cl = new CursorLoader(getActivity());
            cl.setUri(CameAndWentProvider.URI_GET_DETAILS);
            cl.setSelection(String.format("%s=?", CameAndWentProvider.DATE));
            cl.setSelectionArgs(new String[]{String.valueOf(args.getLong(CameAndWentProvider.DATE))});
            cl.setSortOrder(CameAndWentProvider.CAME + " ASC");
            return cl;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(loader.getId() == -1) {
            if (cursor.getCount() > 0) {
                int pos = cursor.getPosition();
                cursor.moveToLast();
                tb.setChecked(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION)) <= 0);
                cursor.moveToPosition(pos);
            }
            adapter.setGroupCursor(cursor);
        }
        else{
            adapter.setChildrenCursor(loader.getId(), cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == -1) {
            adapter.setGroupCursor(null);
        }
        else{
            adapter.setChildrenCursor(loader.getId(), null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clock_button:
                ToggleButton b = (ToggleButton) v;
                if(b.isChecked())
                    cm.clockIn();
                else
                    cm.clockOut();
                break;
            case R.id.logentry_deletebutton:
                getActivity().getContentResolver().delete(CameAndWentProvider.URI_DELETE_DETAIL, CameAndWentProvider.ID + "=?", new String[]{String.valueOf((long) v.getTag())});
                break;
            case R.id.logentry_editButton:
                long fakkingId = (long) v.getTag();
                host.onEditLogentry(fakkingId);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            host = (LogFragmentHost) activity;
        }
        catch (ClassCastException e){
            throw new IllegalStateException("Parent must implement LogFragmentHost interface");
        }
    }

    public interface LogFragmentHost{

        public void onEditLogentry(long id);
    }

    @Override
    public boolean onLongClick(View v) {
        TimePickerDialog tbd = new TimePickerDialog(getActivity(), this, 0, 0, true);
        tbd.show();
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(!tb.isChecked()){
            cm.clockIn(TimeConverter.hourAndMinuteToMillis(hourOfDay, minute));
            tb.setChecked(true);
        }
        else{
            tb.setChecked(false);
            cm.clockOut(TimeConverter.hourAndMinuteToMillis(hourOfDay, minute));
        }
    }



    private class LogCursorTreeAdapter extends CursorTreeAdapter{
        private final SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        private final SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        private Context context;
        private View.OnClickListener childClickListener;

        public LogCursorTreeAdapter(Cursor cursor, Context context, View.OnClickListener childClickListener) {
            super(cursor, context, true);
            this.context = context;
            this.childClickListener = childClickListener;
        }

        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            Bundle b = new Bundle();
            long date = groupCursor.getLong(groupCursor.getColumnIndex(CameAndWentProvider.DATE));
            b.putLong(CameAndWentProvider.DATE, date);
            Cursor c = context.getContentResolver().query(CameAndWentProvider.URI_GET_DETAILS, null, String.format("%s=?", CameAndWentProvider.DATE), new String[]{String.valueOf(date)}, CameAndWentProvider.CAME + " ASC");
            return c;
        }

        @Override
        protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
            return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_expandable_list_item_2, null, false);
        }

        @Override
        protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
            long cameTime = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DATE));
            String date = sdf.format(new Date(cameTime));
            long durationTime = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION));
            String duration = formatInterval(durationTime);
        //    if(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION)) <= 0)
          //      duration = "currently at work";
            ((TextView)view.findViewById(android.R.id.text1)).setText("Date: " + date);
            ((TextView)view.findViewById(android.R.id.text2)).setText("Duration: " + duration);
        }

        @Override
        protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
            return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.logdetailsview, null, false);

        }

        @Override
        protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
            long cameTime = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.CAME));
            long wentTime = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.WENT));
            String wentS = time.format(new Date(wentTime));
            boolean isbreak = cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.ISBREAK)) == 1;
            if(wentTime <= 0)
                wentS = "Currently at work";
            ((TextView)view.findViewById(R.id.log_details_came)).setText("Came: " + time.format(new Date(cameTime)));
            ((TextView)view.findViewById(R.id.log_details_went)).setText("Went: " + wentS);
            ((TextView)view.findViewById(R.id.log_details_isbreak)).setText((isbreak ? "Break" : "Work") +": " + formatInterval(wentTime-cameTime));

            View v1 = view.findViewById(R.id.logentry_deletebutton);
            v1.setOnClickListener(childClickListener);
            v1.setTag(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.ID)));

            View v2 = view.findViewById(R.id.logentry_editButton);
            v2.setOnClickListener(childClickListener);
            long fakkingId = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.ID));
            v2.setTag(fakkingId);
        }

        private String formatInterval(long l)
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
}
