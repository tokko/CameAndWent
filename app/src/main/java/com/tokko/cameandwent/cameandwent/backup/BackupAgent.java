package com.tokko.cameandwent.cameandwent.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import java.io.File;
import java.io.IOException;

public class BackupAgent extends BackupAgentHelper {
    public static final String BACKUP_PREFS = "backupprefs";
    public static final String LAST_BACKUP = "lastbackup";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, getPackageName()+"_preferences");
        addHelper("default preferences", helper);
        FileBackupHelper dbs = new FileBackupHelper(this, CameAndWentProvider.DATABASE_NAME);
        addHelper("dbs", dbs);
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        Log.d("BackupAgent", "Restoring");
        super.onRestore(data, appVersionCode, newState);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        Log.d("BackupAgent", "Backing up");
        getSharedPreferences(BACKUP_PREFS, Context.MODE_PRIVATE).edit().putLong(LAST_BACKUP, TimeConverter.getCurrentTime().getMillis()).apply();
        super.onBackup(oldState, data, newState);
    }

    @Override
    public File getFilesDir(){
        File path = getDatabasePath(CameAndWentProvider.DATABASE_NAME);
        return path.getParentFile();
    }
}
