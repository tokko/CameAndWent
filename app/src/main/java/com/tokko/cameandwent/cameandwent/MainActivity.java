package com.tokko.cameandwent.cameandwent;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.tokko.cameandwent.cameandwent.automaticbreaks.AutomaticBreakManager;
import com.tokko.cameandwent.cameandwent.backup.BackupAgent;
import com.tokko.cameandwent.cameandwent.locationtags.SetTagFragment;
import com.tokko.cameandwent.cameandwent.log.LogEntryEditorFragment;
import com.tokko.cameandwent.cameandwent.log.LogFragment;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.settings.SettingsActivity;
import com.tokko.cameandwent.cameandwent.summaries.SummaryFragment;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

import roboguice.activity.RoboFragmentActivity;


public class MainActivity extends RoboFragmentActivity implements LogFragment.LogFragmentHost{
    public static final String ACTION_WEEKLY_SUMMARY = "ACTION_WEEKLY_SUMMARY";
    public static final String ACTION_MONTHLY_SUMMARY = "ACTION_MONTHLY_SUMMARY";
    private static final String HAS_SHOWN_SETTINGS = "hasshownsettings_2";
    private LogFragment logFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logFragment = new LogFragment();
        getContentResolver().call(CameAndWentProvider.URI_LOG_ENTRIES, CameAndWentProvider.CLEAN_METHOD, null, null);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, logFragment).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("origin").commit();
        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(HAS_SHOWN_SETTINGS, false)) {
            showSettings();
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(HAS_SHOWN_SETTINGS, true).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(ACTION_WEEKLY_SUMMARY)){
            SummaryFragment.newInstance().show(getSupportFragmentManager(), "summary");
        }
        if(getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(ACTION_MONTHLY_SUMMARY)){
            SummaryFragment.newMonthlyInstance().show(getSupportFragmentManager(), "summary");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void showSettings(){
        //noinspection ConstantConditions
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettings();
            //getActionBar().setDisplayShowHomeEnabled(true);
            return true;
        }
        if(id == android.R.id.home){
            getFragmentManager().popBackStack();
            //noinspection ConstantConditions
            getActionBar().setDisplayHomeAsUpEnabled(false);
            return true;
        }
        if(id == R.id.show_summary){
            SummaryFragment.newInstance().show(getSupportFragmentManager(), "summary");
            return true;
        }
        if(id == R.id.show_monthly_summary){
            SummaryFragment.newMonthlyInstance().show(getSupportFragmentManager(), "monthly_summary");
            return true;
        }
        if(id == R.id.restore_data){
            long lastBackup = getSharedPreferences(BackupAgent.BACKUP_PREFS, Context.MODE_PRIVATE).getLong(BackupAgent.LAST_BACKUP, -1);
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Restore data");
            if(lastBackup > -1){
                b.setMessage("Current settings and log will be replaced with the latest backup from " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(lastBackup)));
                b.setPositiveButton("Do it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new BackupManager(getApplicationContext()).requestRestore(new RestoreObserver() {
                            @Override
                            public void restoreStarting(int numPackages) {
                                super.restoreStarting(numPackages);
                            }
                        });
                    }
                });
                b.setNegativeButton("Don't do it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            else {
                b.setMessage("No backup");
                b.setNeutralButton("Ok :(", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
            b.show();
            return true;
        }
        if(id == R.id.tag_everything){
            new ClockManager(this).clockIn(TimeConverter.getCurrentTime().getMillis(), 1);
            sendBroadcast(new Intent(this, AutomaticBreakManager.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0)
            super.onBackPressed();
        else {
            //noinspection ConstantConditions
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onEditLogentry(long id) {
        LogEntryEditorFragment f = LogEntryEditorFragment.newInstance(id);
        f.show(getSupportFragmentManager(), "editorlog");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }


    }
}
