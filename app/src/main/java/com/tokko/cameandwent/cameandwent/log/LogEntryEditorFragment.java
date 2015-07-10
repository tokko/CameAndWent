package com.tokko.cameandwent.cameandwent.log;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.util.TimeConverter;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

public class LogEntryEditorFragment extends RoboDialogFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, CompoundButton.OnCheckedChangeListener {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_CAME = "extra_came";
    private static final String EXTRA_WENT = "extra_went";
    private static final String EXTRA_ISBREAK = "extra_isbreak";
    private static final String EXTRA_TAG = "extra_tag";

    @InjectView(R.id.editor_came) private TimePicker cameTimePicker;
    @InjectView(R.id.editor_went) private TimePicker wentTimePicker;
    @InjectView(R.id.isBreakCheckBox) private CheckBox isBreakCheckBox;
    @InjectView(R.id.cancelButton) private Button cancelButton;
    @InjectView(R.id.okButton) private Button okButton;
    @InjectView(R.id.log_edit_went_container) private LinearLayout wentContainer;
    @InjectView(R.id.date) private Button dateButton;
    @InjectView(R.id.logentryeditor_TagSpinner) private Spinner tagSpinner;
    @InjectView(R.id.dailyduration_label) private TextView dailyDurationLabel;
    @InjectView(R.id.start_date_label) private TextView startDateLabel;
    @InjectView(R.id.end_date_label) private TextView endDateLabel;
    @InjectView(R.id.enddate) private Button selectEndDateButton;
    @InjectView(R.id.isBulkCheckBox) private CheckBox isBulkCheckBox;

    private CursorAdapter tagSpinnerAdapter;
    private long id;
    private long tagId = -1;
    private boolean isBulk;

    public static LogEntryEditorFragment newInstance(long id){
        LogEntryEditorFragment f = new LogEntryEditorFragment();
        Bundle b = new Bundle();
        b.putLong(EXTRA_ID, id);
        f.setArguments(b);
        return f;
    }

    public static LogEntryEditorFragment newInstance(){
        LogEntryEditorFragment f = new LogEntryEditorFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagSpinnerAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, null, new String[]{CameAndWentProvider.TAG}, new int[]{android.R.id.text1}, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logentryeditorfragment, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Edit entry");
        tagSpinner.setAdapter(tagSpinnerAdapter);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        dateButton.setOnClickListener(this);
        wentTimePicker.setIs24HourView(true);
        cameTimePicker.setIs24HourView(true);
        isBulkCheckBox.setOnCheckedChangeListener(this);
        DateTime dt = TimeConverter.getCurrentTime();
        dateButton.setText(dt.getYear() + "-" + dt.getMonthOfYear() + "-" + dt.getDayOfMonth());

        if(savedInstanceState != null){
            long cameTime = savedInstanceState.getLong(EXTRA_CAME);
            cameTimePicker.setCurrentHour(TimeConverter.currentTimeInMillisToCurrentHours(cameTime));
            cameTimePicker.setCurrentMinute(TimeConverter.currentTimeInMillisToCurrentMinutes(cameTime));

            long wentTime = savedInstanceState.getLong(EXTRA_WENT);
            wentTimePicker.setCurrentHour(TimeConverter.currentTimeInMillisToCurrentHours(wentTime));
            wentTimePicker.setCurrentMinute(TimeConverter.currentTimeInMillisToCurrentMinutes(wentTime));

            tagId = savedInstanceState.getLong(EXTRA_TAG, -1);
            scrollTagSpinnerToId();
            isBreakCheckBox.setChecked(savedInstanceState.getBoolean(EXTRA_ISBREAK, false));
            id = savedInstanceState.getLong(EXTRA_ID, -1);
        }
        else
            id = getArguments().getLong(EXTRA_ID, -1);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_ID, id);
        outState.putLong(EXTRA_CAME, TimeConverter.hourAndMinuteToMillis(cameTimePicker.getCurrentHour(), cameTimePicker.getCurrentMinute()));
        outState.putLong(EXTRA_WENT, TimeConverter.hourAndMinuteToMillis(wentTimePicker.getCurrentHour(), wentTimePicker.getCurrentMinute()));
        outState.putBoolean(EXTRA_ISBREAK, isBreakCheckBox.isChecked());
        outState.putLong(EXTRA_TAG, tagId);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(getActivity());
        switch(id){
            case 1:
                cl.setUri(CameAndWentProvider.URI_GET_LOG_ENTRY_FOR_EDIT);
                cl.setSelection(CameAndWentProvider.ID + "=?");
                cl.setSelectionArgs(new String[]{String.valueOf(this.id)});
                return cl;
            case 2:
                cl.setUri(CameAndWentProvider.URI_TAGS);
                return cl;
            default:
                return cl;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case 1:
                if(!data.moveToFirst())
                    throw new IllegalStateException("WHAT THE FUCK??");
                if(data.getCount() != 1)
                    throw new IllegalStateException("WHAT THE FUCK?");
                long cameTime = data.getLong(data.getColumnIndex(CameAndWentProvider.CAME));
                long wentTime = data.getLong(data.getColumnIndex(CameAndWentProvider.WENT));
                tagId = data.getLong(data.getColumnIndex(CameAndWentProvider.TAG));
                scrollTagSpinnerToId();

                wentTimePicker.setVisibility(wentTime == 0 ? View.GONE : View.VISIBLE);

                dateButton.setText(sdf.format(new Date(cameTime)));

                cameTimePicker.setCurrentHour(TimeConverter.currentTimeInMillisToCurrentHours(cameTime));
                cameTimePicker.setCurrentMinute(TimeConverter.currentTimeInMillisToCurrentMinutes(cameTime));

                if(wentTime > 0) {
                    wentContainer.setVisibility(View.VISIBLE);
                    wentTimePicker.setCurrentHour(TimeConverter.currentTimeInMillisToCurrentHours(wentTime));
                    wentTimePicker.setCurrentMinute(TimeConverter.currentTimeInMillisToCurrentMinutes(wentTime));
                }
                else
                    wentContainer.setVisibility(View.GONE);
                data.close();
                break;
            case 2:
                tagSpinnerAdapter.swapCursor(data);
                scrollTagSpinnerToId();
        }

    }

    private void scrollTagSpinnerToId() {
        if(tagId > -1)
            for(int i = 0; i<tagSpinner.getCount(); i++)
                if(tagSpinner.getItemIdAtPosition(i) == tagId) {
                    tagSpinner.setSelection(i);
                }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        switch (loader.getId()){
            case 1:
                dismiss();
                break;
            case 2:
                tagSpinnerAdapter.swapCursor(null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(id > -1)
            getLoaderManager().initLoader(1, null, this);
        getLoaderManager().initLoader(2, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(1);
        getLoaderManager().destroyLoader(2);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okButton:
                final ContentValues cv = new ContentValues();
                cv.put(CameAndWentProvider.ISBREAK, isBreakCheckBox.isChecked()?1:0);
                cv.put(CameAndWentProvider.CAME, TimeConverter.parseDate(dateButton.getText().toString()).withTime(cameTimePicker.getCurrentHour(), cameTimePicker.getCurrentMinute(), 0, 0).getMillis());
                cv.put(CameAndWentProvider.TAG, tagSpinner.getSelectedItemId());
                if(wentContainer.getVisibility() == View.VISIBLE)
                    cv.put(CameAndWentProvider.WENT, TimeConverter.parseDate(dateButton.getText().toString()).withTime(wentTimePicker.getCurrentHour(), wentTimePicker.getCurrentMinute(), 0, 0).getMillis());
                if(id > -1)
                    getActivity().getContentResolver().update(CameAndWentProvider.URI_LOG_ENTRIES, cv, CameAndWentProvider.ID + "=?", new String[]{String.valueOf(id)});
                else
                    getActivity().getContentResolver().insert(CameAndWentProvider.URI_CAME, cv);
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.date:
                DateTime dt = TimeConverter.getCurrentTime();
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateButton.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth()).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isBulk = isChecked;
        int visibility = isChecked ? View.VISIBLE : View.GONE;
        dailyDurationLabel.setVisibility(visibility);
        startDateLabel.setVisibility(visibility);
        endDateLabel.setVisibility(visibility);
        selectEndDateButton.setVisibility(visibility);
        dailyDurationLabel.setText(isChecked ? "Daily duration:" : "Came:");
        wentContainer.setVisibility(isChecked ? View.GONE : View.VISIBLE);
    }
}
