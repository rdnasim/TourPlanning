package com.example.omarf.tourplanning.Adapter;

import android.content.Context;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.ModelForecast.Forecastday_;
import com.example.omarf.tourplanning.R;
import com.example.omarf.tourplanning.databinding.ForecastDayRowBinding;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by omarf on 1/23/2017.
 */

public class ForecastDayAdapter extends ArrayAdapter {


    private Context mContext;
    private ArrayList<Forecastday_> mForecastday_s;

    public ForecastDayAdapter(Context context, ArrayList<Forecastday_> forecastdays) {
        super(context, R.layout.forecast_day_row, forecastdays);
        mContext = context;
        mForecastday_s = forecastdays;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ForecastDayRowBinding dayRowBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.forecast_day_row,
                parent, false);

        Forecastday_ forecastday=mForecastday_s.get(position);
        String dayName = forecastday.getDate().getWeekday();
        String highCelc = forecastday.getHigh().getCelsius()+"°";
        String lowCelc = forecastday.getLow().getCelsius()+"°";
        String url=forecastday.getIconUrl();

        Picasso.with(mContext).load(url).into(dayRowBinding.weatherTypeImageView);

        dayRowBinding.dayNameTextView.setText(dayName);
        dayRowBinding.highestTempTextView.setText(highCelc);
        dayRowBinding.lowestTempTextView.setText(lowCelc);
        return dayRowBinding.getRoot();
    }
}
