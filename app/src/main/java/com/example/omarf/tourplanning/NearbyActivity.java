package com.example.omarf.tourplanning;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.omarf.tourplanning.databinding.ActivityNearbyBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class NearbyActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = "PlacesAPIActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int PLACE_PICKER_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ActivityNearbyBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(NearbyActivity.this,R.layout.activity_nearby);
        Button currentButton = (Button) findViewById(R.id.current_button);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();

      /*  currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleApiClient.isConnected()) {
                    if (ContextCompat.checkSelfPermission(NearbyActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NearbyActivity.this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_CODE);
                    } else {
                        callPlaceDetectionApi();
                    }

                }

            }
        });*/

        showingIntoMap();

        mBinding.currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingIntoMap();
            }
        });



    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPlaceDetectionApi();
                }
                break;
        }
    }

    private void callPlaceDetectionApi() throws SecurityException {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                float highestLikelyHood = 0;
                PlaceLikelihood highestPlaceLikelyHood = null;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if (highestLikelyHood < placeLikelihood.getLikelihood()) {
                        highestLikelyHood = placeLikelihood.getLikelihood();
                        highestPlaceLikelyHood = placeLikelihood;
                    }

                    Log.i(LOG_TAG, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));

                }

                Log.i(LOG_TAG, String.format("Place '%s' with " +
                                "likelihood: %g",
                        highestPlaceLikelyHood.getPlace().getName(),
                        highestPlaceLikelyHood.getLikelihood()));

                Toast.makeText(NearbyActivity.this, String.format("Place '%s' with " +
                                "likelihood: %g",
                        highestPlaceLikelyHood.getPlace().getName(),
                        highestPlaceLikelyHood.getLikelihood()), Toast.LENGTH_SHORT).show();


                showingIntoMap();

                likelyPlaces.release();


            }
        });
    }

    private void showingIntoMap() {
        //LatLngBounds latLngBounds= crateBound(highestPlaceLikelyHood.getPlace().getLatLng());
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // intentBuilder.setLatLngBounds(latLngBounds);
        try {
            Intent intent = intentBuilder.build(NearbyActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private LatLngBounds crateBound(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude + 1;
        LatLng toLatLng = new LatLng(latitude, longitude);

        return new LatLngBounds(latLng, toLatLng);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Place place=PlacePicker.getPlace(this,data);

            String name=place.getName().toString();
             String address=place.getAddress().toString();
            String phoneNumber=place.getPhoneNumber().toString();
            if (!name.isEmpty())
            mBinding.placeNameTv.setText(name);
            if (!address.isEmpty())
            mBinding.addressTv.setText(address);
            if (!phoneNumber.isEmpty())
            mBinding.phoneTv.setText(phoneNumber);



        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
