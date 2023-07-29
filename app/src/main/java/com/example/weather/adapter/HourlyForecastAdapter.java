package com.example.weather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.HourlyForecastResponse;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder> {

    private List<HourlyForecastResponse> hourlyForecasts;
    private Context context;

    public HourlyForecastAdapter(Context context, List<HourlyForecastResponse> hourlyForecasts) {
        this.context = context;
        this.hourlyForecasts = hourlyForecasts;
    }

    @NonNull
    @Override
    public HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_day_detail, parent, false);
        return new HourlyForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {
        HourlyForecastResponse hourlyForecastData = hourlyForecasts.get(position);
        String time = hourlyForecastData.getTime();
        String lastFiveCharacters = time.substring(time.length() - 5);
        holder.timeTv.setText(lastFiveCharacters);
        holder.tempTv.setText(context.getString(R.string.temperature_format, hourlyForecastData.getTemp()));
        // Update rainImg based on condition
        // This is a simple example, you should implement a proper method to set image based on weather condition
        if (hourlyForecastData.getCondition().toLowerCase().contains("rain".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_rain);
            holder.rainImg.setImageDrawable(drawable);
        } else if (hourlyForecastData.getCondition().toLowerCase().contains("clear".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_clear);
            holder.rainImg.setImageDrawable(drawable);
        } else if (hourlyForecastData.getCondition().toLowerCase().contains("Sunny".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_sun);
            holder.rainImg.setImageDrawable(drawable);
        } else if (hourlyForecastData.getCondition().toLowerCase().contains("cloudy".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_more_cloud);
            holder.rainImg.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return hourlyForecasts.size();
    }

    public static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {
        ImageView rainImg;
        TextView tempTv;
        TextView timeTv;

        public HourlyForecastViewHolder(View view) {
            super(view);
            rainImg = view.findViewById(R.id.rainImg);
            tempTv = view.findViewById(R.id.tempTv);
            timeTv = view.findViewById(R.id.timeTv);
        }
    }
}