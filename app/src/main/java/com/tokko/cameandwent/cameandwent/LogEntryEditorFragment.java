package com.tokko.cameandwent.cameandwent;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

public class LogEntryEditorFragment extends RoboDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_CAME = "extra_came";
    private static final String EXTRA_WENT = "extra_went";

    @InjectView(R.id.editor_came) private TimePicker cameTimePicker;
    @InjectView(R.id.editor_went) private TimePicker wentTimePicker;
    @InjectView(R.id.stillAtWorkCheckBox) private CheckBox stillAtWorkCheckBox;
    @InjectView(R.id.cancelButton) private Button cancelButton;
    @InjectView(R.id.okButton) private Button okButton;
    @InjectView(R.id.log_edit_went_container) private LinearLayout wentContainer;

    private long id;

    public static LogEntryEditorFragment newInstance(long id){
        LogEntryEditorFragment f = new LogEntryEditorFragment();
        Bundle b = new Bundle();
        b.putLong(EXTRA_ID, id);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logentryeditorfragment, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        stillAtWorkCheckBox.setOnCheckedChangeListener(this);
        wentTimePicker.setIs24HourView(true);
        cameTimePicker.setIs24HourView(true);

        if(savedInstanceState != null){
            long cameTime = savedInstanceState.getLong(EXTRA_CAME);
            cameTimePicker.setCurrentHour(TimeConverter.millisToHours(cameTime));
            cameTimePicker.setCurrentMinute(TimeConverter.millisToMinutes(cameTime));

            long wentTime = savedInstanceState.getLong(EXTRA_WENT);
            wentTimePicker.setCurrentHour(TimeConverter.millisToHours(wentTime));
            wentTimePicker.setCurrentMinute(TimeConverter.millisToMinutes(wentTime));

            id = savedInstanceState.getLong(EXTRA_ID);
        }
        else
            id = getArguments().getLong(EXTRA_ID);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_ID, id);
        outState.putLong(EXTRA_CAME, TimeConverter.hourAndMinuteToMillis(cameTimePicker.getCurrentHour(), cameTimePicker.getCurrentMinute()));
        outState.putLong(EXTRA_WENT, TimeConverter.hourAndMinuteToMillis(wentTimePicker.getCurrentHour(), wentTimePicker.getCurrentMinute()));
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(getActivity());
        cl.setUri(CameAndWentProvider.URI_GET_DETAILS);
        cl.setSelection(CameAndWentProvider.ID + "=?");
        cl.setSelectionArgs(new String[]{String.valueOf(this.id)});
        return cl;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst())
            throw new IllegalStateException("WHAT THE FUCK??");
        if(data.getCount() != 1)
            throw new IllegalStateException("WHAT THE FUCK?");
        long cameTime = data.getLong(data.getColumnIndex(CameAndWentProvider.CAME));
        long wentTime = data.getLong(data.getColumnIndex(CameAndWentProvider.WENT));

        stillAtWorkCheckBox.setChecked(wentTime == 0);

        cameTimePicker.setCurrentHour(TimeConverter.millisToHours(cameTime));
        cameTimePicker.setCurrentMinute(TimeConverter.millisToMinutes(cameTime));

        if(wentTime > 0) {
            wentContainer.setVisibility(View.VISIBLE);
            wentTimePicker.setCurrentHour(TimeConverter.millisToHours(wentTime));
            wentTimePicker.setCurrentMinute(TimeConverter.millisToMinutes(wentTime));
        }
        else
            wentContainer.setVisibility(View.GONE);

        data.close();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        dismiss();

    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okButton:
                ContentValues cv = new ContentValues();
                cv.put(CameAndWentProvider.CAME, TimeConverter.hourAndMinuteToMillis(cameTimePicker.getCurrentHour(), cameTimePicker.getCurrentMinute()));
                if(wentContainer.getVisibility() == View.VISIBLE)
                    cv.put(CameAndWentProvider.WENT, TimeConverter.hourAndMinuteToMillis(wentTimePicker.getCurrentHour(), wentTimePicker.getCurrentMinute()));
                getActivity().getContentResolver().update(CameAndWentProvider.URI_UPDATE_PARTICULAR_LOG_ENTRY, cv, CameAndWentProvider.ID + "=?", new String[]{String.valueOf(id)});
            case R.id.cancelButton:
                dismiss();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        wentTimePicker.setVisibility(isChecked?View.GONE:View.VISIBLE);
    }
}
