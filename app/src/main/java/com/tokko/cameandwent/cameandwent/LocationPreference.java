package com.tokko.cameandwent.cameandwent;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;

import com.google.android.gms.common.api.GoogleApiClient;
import com.tokko.cameandwent.cameandwent.locationtags.LocationTagActivity;

public class LocationPreference extends Preference implements Preference.OnPreferenceClickListener {

    private GoogleApiClient mGoogleApiClient;
    private Context context;

    public LocationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        context.startActivity(new Intent(context, LocationTagActivity.class));
        return true;
    }

}
