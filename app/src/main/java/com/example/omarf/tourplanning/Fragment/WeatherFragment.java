package com.example.omarf.tourplanning.Fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.omarf.tourplanning.Adapter.ForeCastDayAdapterRecycler;
import com.example.omarf.tourplanning.Adapter.ForecastHourRecyclerAdapter;
import com.example.omarf.tourplanning.ApiEndPoint;
import com.example.omarf.tourplanning.MainActivity;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.CurrentObservation;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.HourlyForecast.Hourly;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.HourlyForecast.HourlyForecast;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.ModelForecast.Forecast;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.ModelForecast.Forecastday;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.ModelForecast.Forecastday_;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.WeatherUnder;
import com.example.omarf.tourplanning.R;
import com.example.omarf.tourplanning.databinding.FragmentWeatherBinding;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private static final int PLACE_AUTO_COMPLETE_REQUEST = 10;
    private FragmentWeatherBinding dataBinding ;
    /*1bdb6580e4c12e06*/


    private String mCountyName;
    private String mCityName;
    private MyDataListener myDataListener;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);

        dataBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTO_COMPLETE_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiEndPoint endPoint = retrofit.create(ApiEndPoint.class);


        /************************************Current Condition and some parts of details*************************************************/
        Call<WeatherUnder> res = endPoint.getCurrentCondition(mCountyName, mCityName);
        res.enqueue(new Callback<WeatherUnder>() {
            @Override
            public void onResponse(Call<WeatherUnder> call, Response<WeatherUnder> response) {

                CurrentObservation observation = response.body().getCurrentObservation();
                if (observation == null) {
                    /*getActivity().onBackPressed();
                    Toast.makeText(getActivity(), "Server error, please enter new query ", Toast.LENGTH_SHORT).show();*/
                } else {
                    String currentTemp = String.valueOf(observation.getTempC()) + "°";
                    String feelsLike = observation.getFeelslikeC() + "°";
                    String humidity = observation.getRelativeHumidity();
                    String visibility = observation.getVisibilityKm() + "Km";
                    String uv = observation.getUV();
                    String wind=observation.getWindMph()+" mph";
                    String windString=observation.getWindString();



                    dataBinding.currentTextView.setText(currentTemp);
                    dataBinding.locationName.setText(mCityName + " " + mCountyName);

                    dataBinding.feelsLikeTextView.setText(feelsLike);
                    dataBinding.humidityTextView.setText(humidity);
                    dataBinding.visibilityTextView.setText(visibility);
                    dataBinding.uvTextView.setText(uv);
                    dataBinding.windTextView.setText(wind);
                    dataBinding.windStringTextView.setText(windString);
                }

            }

            @Override
            public void onFailure(Call<WeatherUnder> call, Throwable t) {

            }
        });

        /************************************Forecast*************************************************/

        Call<Forecast> forecastCall = endPoint.getForeCast(mCountyName, mCityName);
        forecastCall.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if(response.body().getForecast()==null){
                    return;
                }
                final ArrayList<Forecastday_> forecastday_s = (ArrayList<Forecastday_>) response.body().getForecast().getSimpleforecast().getForecastday();
                ArrayList<Forecastday> forecastday_text = (ArrayList<Forecastday>) response.body().getForecast().getTxtForecast().getForecastday();
              /*  if (forecastday_s == null || forecastday_text == null) {
                    return;
                }*/

                String todaySummary="Today- "+forecastday_text.get(0).getFcttextMetric();
                String tonightSummary="Tonight- "+forecastday_text.get(1).getFcttextMetric();
                dataBinding.todayForecastTextTextView.setText(todaySummary);
                dataBinding.tonightForecastTextTextView.setText(tonightSummary);

                String highCelcius = forecastday_s.get(0).getHigh().getCelsius()+"°";
                String lowestCelcius = forecastday_s.get(0).getLow().getCelsius()+"°";





               /* ForecastDayAdapter adapter = new ForecastDayAdapter(getActivity(), forecastday_s);*/
                dataBinding.highestTextView.setText(highCelcius);
                dataBinding.lowestTextView.setText(lowestCelcius);
               /* dataBinding.dayForecastListView.setAdapter(adapter);*/


                final ArrayList<Forecastday_> forecastFivedays = new ArrayList<Forecastday_>();
                final ArrayList<Forecastday_> forecastTendays = new ArrayList<Forecastday_>();

                for (int i = 0; i < 5; i++) {
                    forecastFivedays.add(forecastday_s.get(i));
                }
                forecastTendays.addAll(forecastday_s);



                /*forecastday_s.clear();
                forecastday_s.addAll(forecastFivedays);*/
                listSwaping(forecastFivedays, forecastday_s);
                final ForeCastDayAdapterRecycler adapterRecycler = new ForeCastDayAdapterRecycler(getActivity(), forecastday_s);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                dataBinding.dayForecastRecyclerView.setAdapter(adapterRecycler);
                dataBinding.dayForecastRecyclerView.setLayoutManager(layoutManager);


                dataBinding.fiveDayTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listSwaping(forecastFivedays, forecastday_s);
                        /*forecastday_s.clear();
                        forecastday_s.addAll(forecastFivedays);*/

                        adapterRecycler.notifyDataSetChanged();
                    }
                });

                dataBinding.tenDayTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*forecastday_s.clear();
                        forecastday_s.addAll(forecastTendays);*/
                        listSwaping(forecastTendays, forecastday_s);

                        adapterRecycler.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

            }
        });


        /***********************************Hourly Forecast*************************************************/

        Call<Hourly> hourlyCall = endPoint.getHourlyForeCast(mCountyName, mCityName);
        hourlyCall.enqueue(new Callback<Hourly>() {
            @Override
            public void onResponse(Call<Hourly> call, Response<Hourly> response) {
                ArrayList<HourlyForecast> hourlyForecasts = (ArrayList<HourlyForecast>) response.body().getHourlyForecast();
                if (hourlyForecasts==null){
                    return;
                }
                ForecastHourRecyclerAdapter adapter = new ForecastHourRecyclerAdapter(getActivity(), hourlyForecasts);
                dataBinding.hourForecastRecyclerView.setAdapter(adapter);

                String condition = hourlyForecasts.get(0).getCondition();
                String iconUrl = hourlyForecasts.get(0).getIconUrl();
                dataBinding.weatherTextView.setText(condition);
                Picasso.with(getActivity()).load(iconUrl).into(dataBinding.weatherImageView);
                Picasso.with(getActivity()).load(iconUrl).into(dataBinding.weatherTypeDetailImageView);

            }

            @Override
            public void onFailure(Call<Hourly> call, Throwable t) {

            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        dataBinding.hourForecastRecyclerView.setLayoutManager(layoutManager);


        return dataBinding.getRoot();
    }


    private void listSwaping(ArrayList<Forecastday_> sourceList, ArrayList<Forecastday_> destinationList) {
        destinationList.clear();
        destinationList.addAll(sourceList);
    }

    public void setQuery(String countryName, String cityName) {
        mCountyName = countryName;
        mCityName = cityName;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTO_COMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                dataBinding.locationName.setText(place.getName());

                myDataListener.restartWeather(String.valueOf(place.getLatLng().latitude),String.valueOf(place.getLatLng().longitude));

                //////////////////////////////////////////////////////////
                dataBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                            startActivityForResult(intent, PLACE_AUTO_COMPLETE_REQUEST);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                ApiEndPoint endPoint = retrofit.create(ApiEndPoint.class);


                /************************************Current Condition and some parts of details*************************************************/
                Call<WeatherUnder> res = endPoint.getCurrentCondition(mCountyName, mCityName);
                res.enqueue(new Callback<WeatherUnder>() {
                    @Override
                    public void onResponse(Call<WeatherUnder> call, Response<WeatherUnder> response) {

                        CurrentObservation observation = response.body().getCurrentObservation();
                        if (observation == null) {
                    /*getActivity().onBackPressed();
                    Toast.makeText(getActivity(), "Server error, please enter new query ", Toast.LENGTH_SHORT).show();*/
                        } else {
                            String currentTemp = String.valueOf(observation.getTempC()) + "°";
                            String feelsLike = observation.getFeelslikeC() + "°";
                            String humidity = observation.getRelativeHumidity();
                            String visibility = observation.getVisibilityKm() + "Km";
                            String uv = observation.getUV();
                            String wind=observation.getWindMph()+" mph";
                            String windString=observation.getWindString();



                            dataBinding.currentTextView.setText(currentTemp);
                            dataBinding.locationName.setText(mCityName + " " + mCountyName);

                            dataBinding.feelsLikeTextView.setText(feelsLike);
                            dataBinding.humidityTextView.setText(humidity);
                            dataBinding.visibilityTextView.setText(visibility);
                            dataBinding.uvTextView.setText(uv);
                            dataBinding.windTextView.setText(wind);
                            dataBinding.windStringTextView.setText(windString);
                        }

                    }

                    @Override
                    public void onFailure(Call<WeatherUnder> call, Throwable t) {

                    }
                });

                /************************************Forecast*************************************************/

                Call<Forecast> forecastCall = endPoint.getForeCast(mCountyName, mCityName);
                forecastCall.enqueue(new Callback<Forecast>() {
                    @Override
                    public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                        if(response.body().getForecast()==null){
                            return;
                        }
                        final ArrayList<Forecastday_> forecastday_s = (ArrayList<Forecastday_>) response.body().getForecast().getSimpleforecast().getForecastday();
                        ArrayList<Forecastday> forecastday_text = (ArrayList<Forecastday>) response.body().getForecast().getTxtForecast().getForecastday();
              /*  if (forecastday_s == null || forecastday_text == null) {
                    return;
                }*/

                        String todaySummary="Today- "+forecastday_text.get(0).getFcttextMetric();
                        String tonightSummary="Tonight- "+forecastday_text.get(1).getFcttextMetric();
                        dataBinding.todayForecastTextTextView.setText(todaySummary);
                        dataBinding.tonightForecastTextTextView.setText(tonightSummary);

                        String highCelcius = forecastday_s.get(0).getHigh().getCelsius()+"°";
                        String lowestCelcius = forecastday_s.get(0).getLow().getCelsius()+"°";





               /* ForecastDayAdapter adapter = new ForecastDayAdapter(getActivity(), forecastday_s);*/
                        dataBinding.highestTextView.setText(highCelcius);
                        dataBinding.lowestTextView.setText(lowestCelcius);
               /* dataBinding.dayForecastListView.setAdapter(adapter);*/


                        final ArrayList<Forecastday_> forecastFivedays = new ArrayList<Forecastday_>();
                        final ArrayList<Forecastday_> forecastTendays = new ArrayList<Forecastday_>();

                        for (int i = 0; i < 5; i++) {
                            forecastFivedays.add(forecastday_s.get(i));
                        }
                        forecastTendays.addAll(forecastday_s);



                /*forecastday_s.clear();
                forecastday_s.addAll(forecastFivedays);*/
                        listSwaping(forecastFivedays, forecastday_s);
                        final ForeCastDayAdapterRecycler adapterRecycler = new ForeCastDayAdapterRecycler(getActivity(), forecastday_s);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

                        dataBinding.dayForecastRecyclerView.setAdapter(adapterRecycler);
                        dataBinding.dayForecastRecyclerView.setLayoutManager(layoutManager);


                        dataBinding.fiveDayTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listSwaping(forecastFivedays, forecastday_s);
                        /*forecastday_s.clear();
                        forecastday_s.addAll(forecastFivedays);*/

                                adapterRecycler.notifyDataSetChanged();
                            }
                        });

                        dataBinding.tenDayTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                        /*forecastday_s.clear();
                        forecastday_s.addAll(forecastTendays);*/
                                listSwaping(forecastTendays, forecastday_s);

                                adapterRecycler.notifyDataSetChanged();
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<Forecast> call, Throwable t) {

                    }
                });


                /***********************************Hourly Forecast*************************************************/

                Call<Hourly> hourlyCall = endPoint.getHourlyForeCast(mCountyName, mCityName);
                hourlyCall.enqueue(new Callback<Hourly>() {
                    @Override
                    public void onResponse(Call<Hourly> call, Response<Hourly> response) {
                        ArrayList<HourlyForecast> hourlyForecasts = (ArrayList<HourlyForecast>) response.body().getHourlyForecast();
                        if (hourlyForecasts==null){
                            return;
                        }
                        ForecastHourRecyclerAdapter adapter = new ForecastHourRecyclerAdapter(getActivity(), hourlyForecasts);
                        dataBinding.hourForecastRecyclerView.setAdapter(adapter);

                        String condition = hourlyForecasts.get(0).getCondition();
                        String iconUrl = hourlyForecasts.get(0).getIconUrl();
                        dataBinding.weatherTextView.setText(condition);
                        Picasso.with(getActivity()).load(iconUrl).into(dataBinding.weatherImageView);
                        Picasso.with(getActivity()).load(iconUrl).into(dataBinding.weatherTypeDetailImageView);

                    }

                    @Override
                    public void onFailure(Call<Hourly> call, Throwable t) {

                    }
                });


                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

                dataBinding.hourForecastRecyclerView.setLayoutManager(layoutManager);

            }
        }
    }

    public interface MyDataListener {
        public void restartWeather(String latitude,String longitude);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myDataListener = (MyDataListener) context;


    }
}
