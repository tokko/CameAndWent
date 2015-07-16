package com.tokko.cameandwent.cameandwent.peaccounting;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service{
    private final Authenticator authenticator;

    public AuthenticatorService(){
        authenticator = new Authenticator(getApplicationContext());
    }
    
    @Override
    public IBinder onBind(Intent intent){
        return authenticator.getIBinder();
    }
}
