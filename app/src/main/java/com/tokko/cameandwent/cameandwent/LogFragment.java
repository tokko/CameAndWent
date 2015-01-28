package com.tokko.cameandwent.cameandwent;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andre_000 on 2015-01-28.
 */
public class LogFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private LogCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LogCursorAdapter(getActivity(), null);

    }

    @Override
    public void onStart() {
        super.onStart();
        setListAdapter(adapter);
        setEmptyText("Log is fakking empty D:");
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(getActivity());
        cl.setUri(CameAndWentProvider.URI_GET_ALL);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        adapter.swapCursor(null);
    }

    private class LogCursorAdapter extends CursorAdapter{

        public LogCursorAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String data = sdf.format(new Date(cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.DATE)))) + " " + (cursor.getInt(cursor.getColumnIndex(CameAndWentProvider.ENTERED))==0?"exited":"entered");
            ((TextView)view.findViewById(android.R.id.text1)).setText(data);
        }
    }
}
