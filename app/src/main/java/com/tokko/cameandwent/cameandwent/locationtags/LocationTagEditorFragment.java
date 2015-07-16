package com.tokko.cameandwent.cameandwent.locationtags;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tokko.cameandwent.cameandwent.R;
import com.tokko.cameandwent.cameandwent.providers.CameAndWentProvider;
import com.tokko.cameandwent.cameandwent.services.GeofenceService;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

public class LocationTagEditorFragment extends RoboDialogFragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TAG_TITLE = "EXTRA_TAG_TITLE";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";

    @InjectView(R.id.locationtageditor_cancelButton) private Button cancelButton;
    @InjectView(R.id.locationtageditor_okButton) private Button okButton;
    @InjectView(R.id.locationtageditor_DeleteButton) private Button deleteButton;
    @InjectView(R.id.locationtageditor_SetLocation) private Button setLocationButton;
    @InjectView(R.id.locationtageditor_TagTitle) private EditText tagTitleEditText;
    @InjectView(R.id.locationtageditor_Latitude) private TextView latitudeTextView;
    @InjectView(R.id.locationtageditor_Longitude) private TextView longitudeTextView;
    @InjectView(R.id.locationtageditor_coordinates) private ViewGroup coordinates;

    private long id;
    private double longitude = -1, latitude = -1;
    private String tag;
    private GoogleApiClient mGoogleApiClient;
    private boolean isActive;
    private String origin;

    public static LocationTagEditorFragment newInstance(){
        return newInstance(-1);
    }

    public static LocationTagEditorFragment newInstance(long id) {
        LocationTagEditorFragment f = new LocationTagEditorFragment();
        Bundle b = new Bundle();
        b.putLong(EXTRA_ID, id);
        f.setArguments(b);
        return f;
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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        setLocationButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        populateUI();
    }

    private void populateUI(){
        tagTitleEditText.setText(tag);
        deleteButton.setEnabled(id != -1);
        setCoordinates();
        if(!origin.equals(CameAndWentProvider.LOCAL_GUID)) setExternalOriginMode();
        else if(!isActive) lockEditor();
    }

    private void setCoordinates(){
        if(longitude != -1 && latitude != -1){
            longitudeTextView.setText(String.valueOf(longitude));
            latitudeTextView.setText(String.valueOf(latitude));
            coordinates.setVisibility(View.VISIBLE);
            setLocationButton.setText("Reset Location");
        }
    }

    private void setExternalOriginMode(){
        tagTitleEditText.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void lockEditor(){
        setLocationButton.setEnabled(false);
        tagTitleEditText.setEnabled(false);
        deleteButton.setEnabled(false);
        okButton.setEnabled(false);
    }

    private void loadData(Bundle b){
        latitude = b.getDouble(EXTRA_LATITUDE, -1);
        longitude = b.getDouble(EXTRA_LONGITUDE, -1);
        tag = b.getString(EXTRA_TAG_TITLE);
        id = b.getLong(EXTRA_ID, -1);
    }

    private void loadData(){
        if(id == -1) return;
        Cursor c = getActivity().getContentResolver().query(CameAndWentProvider.URI_TAGS, null, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)}, null);
        if(!c.moveToFirst())
           throw new IllegalStateException("Invalid id: " + id);
        if(c.getCount() != 1)
            throw new IllegalStateException("Duplicate primary key?");
        id = c.getLong(c.getColumnIndex(CameAndWentProvider.ID));
        longitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LONGITUDE));
        latitude = c.getDouble(c.getColumnIndex(CameAndWentProvider.LATITUDE));
        tag = c.getString(c.getColumnIndex(CameAndWentProvider.TAG));
        isActive = c.getInt(c.getColumnIndex(CameAndWentProvider.ACTIVE)) == 1;
        origin = c.getString(c.getColumnIndex(CameAndWentProvider.ORIGIN));
        c.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState){
        return inflater.inflate(R.layout.locationtageditorfragment, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_ID, id);
        outState.putDouble(EXTRA_LONGITUDE, longitude);
        outState.putDouble(EXTRA_LATITUDE, latitude);
        outState.putString(EXTRA_TAG_TITLE, tagTitleEditText.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.locationtageditor_SetLocation:
                mGoogleApiClient.connect();
                break;
            case R.id.locationtageditor_okButton:
                ContentValues cv = new ContentValues();
                cv.put(CameAndWentProvider.TAG, tagTitleEditText.getText().toString());
                cv.put(CameAndWentProvider.LATITUDE, latitude);
                cv.put(CameAndWentProvider.LONGITUDE, longitude);
                if(id == -1)
                    getActivity().getContentResolver().insert(CameAndWentProvider.URI_TAGS, cv);
                else
                    getActivity().getContentResolver().update(CameAndWentProvider.URI_TAGS, cv, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
            case R.id.locationtageditor_cancelButton:
                dismiss();
                break;
            case R.id.locationtageditor_DeleteButton:
                getActivity().getContentResolver().delete(CameAndWentProvider.URI_TAGS, String.format("%s=?", CameAndWentProvider.ID), new String[]{String.valueOf(id)});
                getActivity().sendBroadcast(new Intent(GeofenceService.DEACTIVATE_GEOFENCE).putExtra(GeofenceService.EXTRA_ID, id));
                dismiss();
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    if(getActivity() != null)
                         Toast.makeText(getActivity(), String.format("Longitude: %f\nLatitude %f", location.getLongitude(), location.getLatitude()), Toast.LENGTH_SHORT).show();
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCoordinates();
                        }
                    });
                }
                else
                    Toast.makeText(getActivity(), "No last known location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
