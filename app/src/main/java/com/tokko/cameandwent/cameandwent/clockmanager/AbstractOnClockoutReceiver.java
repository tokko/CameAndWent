package com.tokko.cameandwent.cameandwent.clockmanager;

import android.content.BroadcastReceiver;

public abstract class AbstractOnClockoutReceiver extends BroadcastReceiver{
    public static final String ACTION = "com.tokko.cameandwent.ACTION_ON_CLOCKOUT";
    public static final String EXTRA_TAG = "com.tokko.cameandwent.EXTRA_TAG";

    public AbstractOnClockoutReceiver(){}
}
