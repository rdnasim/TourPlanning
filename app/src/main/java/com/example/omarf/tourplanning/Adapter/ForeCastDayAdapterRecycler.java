package com.example.omarf.tourplanning.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.ModelForecast.Forecastday_;
import com.example.omarf.tourplanning.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by omarf on 1/23/2017.
 */

public class ForeCastDayAdapterRecycler extends RecyclerView.Adapter<ForeCastDayAdapterRecycler.ForecastHolder>  {

    private ArrayList<Forecastday_> mForecastday_s;
    private Context mContext;

    public ForeCastDayAdapterRecycler(Context context,ArrayList<Forecastday_> forecastdays) {
       mForecastday_s=forecastdays;
        mContext=context;
    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.forecast_day_row,parent,false);
       /*LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        ForecastDayRowBinding dayRowBinding= DataBindingUtil.inflate(layoutInflater,R.layout.forecast_day_row,parent,false);
        */
        return new ForecastHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        Forecastday_ forecastday=mForecastday_s.get(position);
        String dayName = forecastday.getDate().getWeekday();
        String highCelc = forecastday.getHigh().getCelsius()+"°";
        String lowCelc = forecastday.getLow().getCelsius()+"°";
        String url=forecastday.getIconUrl();

        holder.mDayNameTextView.setText(dayName);
        holder.mHighTempTextView.setText(highCelc);
        holder.mLowTempTextView.setText(lowCelc);
        Picasso.with(mContext).load(url).into(holder.mWeatherImageView);
    }

    @Override
    public int getItemCount() {
        return mForecastday_s.size();
    }

    public   class ForecastHolder extends RecyclerView.ViewHolder {
       private TextView mDayNameTextView;
        private TextView mHighTempTextView;
        private TextView mLowTempTextView;
        private ImageView mWeatherImageView;
        public ForecastHolder(View itemView) {
            super(itemView);
            /*ForecastDayRowBinding dayRowBinding=DataBindingUtil.findBinding(itemView);
            mDayNameTextView=dayRowBinding.dayNameTextView;
            mHighTempTextView=dayRowBinding.highestTempTextView;
            mLowTempTextView=dayRowBinding.lowestTempTextView;
            mWeatherImageView=dayRowBinding.weatherTypeImageView;*/
            mDayNameTextView= (TextView) itemView.findViewById(R.id.day_name_text_view);
            mHighTempTextView= (TextView) itemView.findViewById(R.id.highest_temp_text_view);
            mLowTempTextView= (TextView) itemView.findViewById(R.id.lowest_temp_text_view);
            mWeatherImageView= (ImageView) itemView.findViewById(R.id.weather_type_image_view);
        }


    }
}
