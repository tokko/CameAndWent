package com.tokko.cameandwent.cameandwent;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.IOException;

public class BackupAgent extends BackupAgentHelper {

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
        super.onRestore(data, appVersionCode, newState);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
    }

    @Override
    public File getFilesDir(){
        File path = getDatabasePath(CameAndWentProvider.DATABASE_NAME);
        return path.getParentFile();
    }
}
