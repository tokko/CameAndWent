package com.tokko.cameandwent.cameandwent.peaccounting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;

import org.joda.time.DateTimeConstants;

import roboguice.inject.InjectView;

public class PeAccountingLoginActivity extends Activity implements View.OnClickListener{
    public static final String PEACCOUNTING_API_KEY = "peaccounting_api_key";
    public static final String PEACOUNTING_DATA = "peacounting_data";
    @InjectView(R.id.peaccounting_apiKey)
    private EditText apiKey;
    @InjectView(R.id.peaccounting_ok)
    private Button okButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState){
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.peaccountinglogin);
        okButton.setOnClickListener(this);
        if(savedInstanceState != null)
            apiKey.setText(savedInstanceState.getString("key", ""));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("key", apiKey.getText().toString());
    }

    @Override
    public void onClick(View v){
        getSharedPreferences(PEACOUNTING_DATA, MODE_PRIVATE).edit().putString
                (PEACCOUNTING_API_KEY, apiKey.getText().toString()).apply();
        Account account = new Account("CameAndWent - PeAccounting", "PeAccounting");
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) getApplicationContext().getSystemService(
                        ACCOUNT_SERVICE);
        if(accountManager.addAccountExplicitly(account, null, null)){
            ContentResolver.addPeriodicSync(
                    account,
                    CameAndWentProvider.AUTHORITY,
                    Bundle.EMPTY,
                    DateTimeConstants.MILLIS_PER_DAY);
        }
        else{
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
    }
}
