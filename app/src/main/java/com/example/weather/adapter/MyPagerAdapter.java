package com.example.weather.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.weather.model.CurrentWeatherResponse;
import com.example.weather.model.DayForecastResponse;

import java.util.Collections;
import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private List<List<DayForecastResponse>> weatherList;
    private List<String> cityNameList;
    private List<CurrentWeatherResponse> condition;
    private OnItemDeletedListener listener;

    public MyPagerAdapter(Context context, List<List<DayForecastResponse>> weatherList, List<String> cityNameList, List<CurrentWeatherResponse> condition) {
        this.context = context;
        this.weatherList = weatherList;
        this.cityNameList = cityNameList;
        this.condition = condition;
    }

    public void setListener(OnItemDeletedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (listener != null) {
            listener.onItemDelect(cityNameList.get(position));
        }
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        WeatherDetailAdapter adapter = new WeatherDetailAdapter(context, Collections.singletonList(weatherList.get(position)), cityNameList.get(position), condition.get(position));
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RecyclerView) object);
    }
}


