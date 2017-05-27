package com.example.omarf.tourplanning.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.omarf.tourplanning.ModelWu.ModelCurrentObservation.HourlyForecast.HourlyForecast;
import com.example.omarf.tourplanning.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by omarf on 1/24/2017.
 */

public class ForecastHourRecyclerAdapter extends  RecyclerView.Adapter<ForecastHourRecyclerAdapter.HourHolder>  {

    private final ArrayList<HourlyForecast> mHourlyList;
    private final Context mContext;

    public ForecastHourRecyclerAdapter(Context context, ArrayList<HourlyForecast> hourlyList) {
        mContext=context;
        mHourlyList=hourlyList;
    }

    @Override
    public HourHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_hour_row,parent,false);
        return new HourHolder(view);
    }

    @Override
    public void onBindViewHolder(HourHolder holder, int position) {
        String time=mHourlyList.get(position).getFCTTIME().getHour()+":"+
                mHourlyList.get(position).getFCTTIME().getMin();
         holder.mTimeTextView.setText(time);
        String temp=mHourlyList.get(position).getTemp().getMetric()+"°";
        holder.mTempTextView.setText(temp);

        Picasso.with(mContext).load(mHourlyList.get(position).getIconUrl()).into(holder.mWeatherImageView);
    }

    @Override
    public int getItemCount() {
        return mHourlyList.size();
    }

    public class HourHolder extends RecyclerView.ViewHolder{
        private TextView mTimeTextView;
        private TextView mTempTextView;
        private ImageView mWeatherImageView;
        public HourHolder(View itemView) {
            super(itemView);
            mTimeTextView= (TextView) itemView.findViewById(R.id.time_text_view);
            mTempTextView= (TextView) itemView.findViewById(R.id.temp_text_view);
            mWeatherImageView = (ImageView) itemView.findViewById(R.id.weather_type_image_view);
        }
    }
}
