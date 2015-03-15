package com.tokko.cameandwent.cameandwent.locationtags;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.tokko.cameandwent.cameandwent.CameAndWentProvider;

import roboguice.fragment.RoboListFragment;

public class LocationTagListFragment extends RoboListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter adapter;
    private LocationTagListFragmentCallbacks host;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{CameAndWentProvider.TAG}, new int[]{android.R.id.text1}, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(adapter);
    }

    @Override
    public void onStop() {
        getLoaderManager().destroyLoader(0);
        super.onStop();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(getActivity());
        cl.setUri(CameAndWentProvider.URI_GET_TAGS);
        return cl;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        host.onLocationTagListItemClick(id);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            host = (LocationTagListFragmentCallbacks) activity;
        }
        catch (ClassCastException e){
            throw new IllegalStateException("Host must implement LocationTagListFragmentCallbacks");
        }
    }

    public interface LocationTagListFragmentCallbacks{

        public void onLocationTagListItemClick(long id);

    }
}
