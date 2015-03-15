package com.tokko.cameandwent.cameandwent.locationtags;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tokko.cameandwent.cameandwent.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.R;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

public class LocationTagEditorFragment extends RoboDialogFragment {

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TAG_TITLE = "EXTRA_TAG_TITLE";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";

    @InjectView(R.id.locationtageditor_cancelButton) private Button cancelButton;
    @InjectView(R.id.locationtageditor_okButton) private Button okButton;
    @InjectView(R.id.locationtageditor_SetLocation) private Button setLocationButton;
    @InjectView(R.id.locationtageditor_TagTitle) private EditText tagTitleEditText;
    @InjectView(R.id.locationtageditor_Latitude) private TextView latituteTextView;
    @InjectView(R.id.locationtageditor_Longitude) private TextView longitudeTextView;
    @InjectView(R.id.locationtageditor_coordinates) private ViewGroup coordinates;

    private long id;
    private double longitude, latitude;
    private String tag;

    public static LocationTagEditorFragment newInstance(long id) {
        LocationTagEditorFragment f = new LocationTagEditorFragment();
        Bundle b = new Bundle();
        b.putLong(EXTRA_ID, id);
        f.setArguments(b);
        return f;
    }

    public static LocationTagEditorFragment newInstance() {
        return newInstance(-1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            loadData(savedInstanceState);
        }
        else if(getArguments() != null){
            id = getArguments().getLong(EXTRA_ID, -1);
            loadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.locationtageditorfragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_ID, id);
        outState.putDouble(EXTRA_LONGITUDE, longitude);
        outState.putDouble(EXTRA_LATITUDE, latitude);
        outState.putString(EXTRA_TAG_TITLE, tagTitleEditText.getText().toString());
    }

    private void loadData(Bundle b){
        latitude = b.getDouble(EXTRA_LATITUDE, -1);
        longitude = b.getDouble(EXTRA_LONGITUDE, -1);
        tag = b.getString(EXTRA_TAG_TITLE);
        id = b.getLong(EXTRA_ID, -1);
    }

    private void loadData(){
        if(id == -1) return;
        Cursor c = getActivity().getContentResolver().query(CameAndWentProvider.URI_GET_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        if(!c.moveToFirst())
           throw new IllegalStateException("Invalid id: " + id);
        if(c.getCount() != 1)
            throw new IllegalStateException("Duplicate primary key?");
        id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
        longitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE));
        latitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE));
        tag = c.getString(c.getColumnIndex(CameAndWentProvider.TAG));
        c.close();
    }

    private void populateUI() {
        tagTitleEditText.setText(tag);
        if(longitude != -1 && latitude != -1){
            longitudeTextView.setText(String.valueOf(longitude));
            latituteTextView.setText(String.valueOf(latitude));
            coordinates.setVisibility(View.VISIBLE);
            setLocationButton.setText("Reset Location");
        }
    }
}
