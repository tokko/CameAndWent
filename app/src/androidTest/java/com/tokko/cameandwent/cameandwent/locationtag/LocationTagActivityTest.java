package com.tokko.cameandwent.cameandwent.locationtag;

import android.test.ActivityInstrumentationTestCase2;

import com.tokko.cameandwent.cameandwent.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.locationtags.LocationTagListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LocationTagActivityTest extends ActivityInstrumentationTestCase2<LocationTagListActivity> {

    public LocationTagActivityTest() {
        super(LocationTagListActivity.class);
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
        for (int i = 0; i < 5; i++){
            onView(withText(tag+suffix)).check(matches(isDisplayed()));
        }
    }
}
