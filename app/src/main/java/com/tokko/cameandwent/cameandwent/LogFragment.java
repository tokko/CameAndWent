package com.tokko.cameandwent.cameandwent;

import android.app.ListFragment;
import android.app.LoaderManager;
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
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andre_000 on 2015-01-28.
 */
public class LogFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private LogCursorAdapter adapter;
    private ClockManager cm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LogCursorAdapter(getActivity(), null);
        cm = new ClockManager(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, null);
        ((ToggleButton)v.findViewById(R.id.clock_button)).setOnClickListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        setListAdapter(adapter);
        TextView tv = new TextView(getActivity());
        tv.setText("Log is fakking empty D:");
        getListView().setEmptyView(tv);
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

    private class LogCursorAdapter extends CursorAdapter{

        public LogCursorAdapter(Context context, Cursor c) {
            super(context, c, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String came = sdf.format(new Date(cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.CAME))));
            String went = sdf.format(new Date(cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.WENT))));
            String duration = new SimpleDateFormat("HH:mm:ss").format(new Date(cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.WENT))));
            ((TextView)view.findViewById(android.R.id.text1)).setText("Came: " + came + "\nWent:" + went + "\nDuration: " + duration);
        }
    }
}
