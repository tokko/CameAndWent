package com.tokko.cameandwent.cameandwent.locationtags;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.tokko.cameandwent.cameandwent.CameAndWentProvider;

public class SetTagFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private ListView lv;
    private CursorAdapter adapter;


    public static SetTagFragment newInstance(){
        return new SetTagFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{CameAndWentProvider.TAG}, new int[]{android.R.id.text1}, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lv = new ListView(getActivity());
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);
        return lv;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Tag Everything");
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(100, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(100);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(getActivity());
        cl.setUri(CameAndWentProvider.URI_TAGS);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        final Context context = getActivity();
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("Tag all entries");
        adb.setMessage("This will tag ALL current entries with this tag. This can not be undone. Are you sure?");
        adb.setPositiveButton("#yolo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues cv = new ContentValues();
                cv.put(CameAndWentProvider.TAG, id);
                context.getContentResolver().update(CameAndWentProvider.URI_LOG_ENTRIES, cv, null, null);
                dismiss();
            }
        });
        adb.setNegativeButton("No, don't do it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        adb.show();
        dismiss();
    }
}
