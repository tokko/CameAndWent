package com.tokko.cameandwent.cameandwent.locationtag;

import android.test.ActivityInstrumentationTestCase2;

import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.locationtags.LocationTagActivity;


public class LocationTagActivityTest extends ActivityInstrumentationTestCase2<LocationTagActivity> {

    public LocationTagActivityTest() {
        super(LocationTagActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().getContext().getContentResolver().call(CameAndWentProvider.URI_GET_LOG_ENTRIES, CameAndWentProvider.SEED_METHOD, null, null);
        getActivity();
    }

    public void testTagListIsPopulated(){
        String tag = "TAG";
        int suffix = 0;
        for (int i = 0; i < 5; i++)
            ;
    }


}
