package com.tokko.cameandwent.cameandwent.locationtags;

import android.os.Bundle;

import roboguice.activity.RoboFragmentActivity;

public class LocationTagActivity extends RoboFragmentActivity implements LocationTagListFragment.LocationTagListFragmentCallbacks{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new LocationTagListFragment()).commit();
    }

    @Override
    public void onLocationTagListItemClick(long id) {
        LocationTagEditorFragment.newInstance(id).show(getSupportFragmentManager(), "tag");
    }

}

