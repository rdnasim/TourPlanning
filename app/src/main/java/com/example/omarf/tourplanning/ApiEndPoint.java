package com.example.omarf.tourplanning;







import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.HourlyForecast.Hourly;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.ModelForecast.Forecast;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.Search.SearchResult;
import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.WeatherUnder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by omarf on 1/21/2017.
 */

public interface ApiEndPoint {
    @GET("conditions/q/{CountryName},{CityName}.json")
    Call<WeatherUnder> getCurrentCondition(@Path("CountryName") String countryName, @Path("CityName") String cityName);


   /* @GET("23.8103,90.4125")
    Call<WeatherDarkSky> getCurrentWeather();*/

    @GET("forecast10day/q/{CountryName},{CityName}.json")
    Call<Forecast> getForeCast(@Path("CountryName") String countryName, @Path("CityName") String cityName);

    @GET("hourly/q/{CountryName},{CityName}.json")
    Call<Hourly> getHourlyForeCast(@Path("CountryName") String countryName, @Path("CityName") String cityName);

    @GET("conditions/q/xx/{CityName}.json")
    Call<SearchResult> getSearchResult(@Path("CityName") String cityName);



}
