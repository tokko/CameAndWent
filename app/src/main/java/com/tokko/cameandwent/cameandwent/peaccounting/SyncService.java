package com.tokko.cameandwent.cameandwent.peaccounting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service{
    private static final Object lock = new Object();
    private static PeAccountingSyncAdapter adapter;

    public SyncService(){
    }

    @Override
    public void onCreate(){
        super.onCreate();
        synchronized(lock){
            if(adapter == null){
                adapter = new PeAccountingSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return adapter.getSyncAdapterBinder();
    }
}
