package com.example.omarf.tourplanning;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omarf.tourplanning.Fragment.EventListFragment;
import com.example.omarf.tourplanning.Fragment.WeatherFragment;
import com.example.omarf.tourplanning.databinding.ActivityMainBinding;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, WeatherFragment.MyDataListener {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String TAG = "main_activity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private double mLat;
    private double mLong;
    private ApiEndPoint mEndPoint;

    public static final String BASE_URL = "http://api.wunderground.com/api/f511f9ee1428e329/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();


        getCurrentLatLong();

        //retrofit initial

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mEndPoint = retrofit.create(ApiEndPoint.class);

        setSupportActionBar(mainBinding.toolbar);

        mainBinding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.events_bottom_menu:
                        fragment = new EventListFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.weather_bottom_menu:
                        createWeatherFragment();
                        break;
                    case R.id.nearby_bottom_menu:
                       startActivity(new Intent(MainActivity.this,NearbyActivity.class));
                        break;
                    default:
                        fragment = null;
                }




                return true;
            }
        });


        mainBinding.navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.sign_out_drawer_menu:
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        return true;


                }

                return false;
            }
        });

        View headerView = mainBinding.navigationDrawer.getHeaderView(0);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.header_image_view);
        TextView nameTextView = (TextView) headerView.findViewById(R.id.name_header_text_view);

        Picasso.with(this).load(fbUser.getPhotoUrl()).into(imageView);
        nameTextView.setText(fbUser.getDisplayName());

    }

    private void createWeatherFragment() {
        WeatherFragment weatherFragment = new WeatherFragment();
        Log.i(TAG,mLong+" "+mLat);
        weatherFragment.setQuery(String.valueOf(mLat), String.valueOf(mLong));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, weatherFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void getCurrentLatLong() {
        /*if (mGoogleApiClient.isConnected()) {*/
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                callPlaceDetectionApi();
            }

       // }


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

                    Log.i(TAG, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));

                }

                Log.i(TAG, String.format("Place '%s' with " +
                                "likelihood: %g",
                        highestPlaceLikelyHood.getPlace().getName(),
                        highestPlaceLikelyHood.getLikelihood()));
                      mLat=  highestPlaceLikelyHood.getPlace().getLatLng().latitude;
                    mLong=highestPlaceLikelyHood.getPlace().getLatLng().longitude;


                likelyPlaces.release();


            }
        });
    }


    private void createNearBy() {
        PlacePicker.IntentBuilder intentBuilder=new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(intentBuilder.build(this),PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PLACE_PICKER_REQUEST){
            Place place= PlacePicker.getPlace(this,data);
            Toast.makeText(this, place.getName().toString(), Toast.LENGTH_SHORT).show();
            Log.i(TAG,place.getName().toString());
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void restartWeather(String latitude, String longitude) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Log.i(TAG,mLong+" "+mLat);
        weatherFragment.setQuery(latitude, longitude);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, weatherFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
