package com.tokko.cameandwent.cameandwent;

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
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LogFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, View.OnLongClickListener, TimePickerDialog.OnTimeSetListener {
    private LogCursorAdapter adapter;
    private ClockManager cm;
    private ToggleButton tb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LogCursorAdapter(getActivity(), null);
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
    public void onStart() {
        super.onStart();
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(getActivity());
        cl.setUri(CameAndWentProvider.URI_GET_ALL);
        return cl;
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.getCount() > 0) {
            int pos = cursor.getPosition();
            cursor.moveToLast();
            tb.setChecked(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.WENT)) == 0);
            cursor.moveToPosition(pos);
        }
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        adapter.swapCursor(null);
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
        }
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
            cm.clockIn(hourAndMinuteToMillis(hourOfDay, minute));
            tb.setChecked(true);
        }
        else{
            tb.setChecked(false);
            cm.clockOut(hourAndMinuteToMillis(hourOfDay, minute));
        }
    }

    private long hourAndMinuteToMillis(int hour, int minute){
        long now = System.currentTimeMillis();
        long currentDate = now - now%(1000*60*60*24);
        return currentDate +  hour*60*60*1000 + minute*60*1000;
    }
    private class LogCursorAdapter extends CursorAdapter{

        public LogCursorAdapter(Context context, Cursor c) {
            super(context, c, true);
        }
        private final SimpleDateFormat year = new SimpleDateFormat("yyyy");
        private final SimpleDateFormat month= new SimpleDateFormat("MM");
        private final SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            long cameTime = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.CAME));
            String came = sdf.format(new Date(cameTime));
            long durationTime = cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.DURATION));
            String duration = formatInterval(durationTime);
            if(cursor.getLong(cursor.getColumnIndex(CameAndWentProvider.WENT)) == 0)
                duration = "currently at work";
            ((TextView)view.findViewById(android.R.id.text1)).setText("Date: " + came + "\nDuration: " + duration);
        }
        private String formatInterval(final long l)
        {
            final long hr = TimeUnit.MILLISECONDS.toHours(l);
            final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
            final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
            return String.format("%02d:%02d:%02d", hr, min, sec);
        }
    }
}
