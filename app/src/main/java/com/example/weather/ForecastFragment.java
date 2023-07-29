package com.example.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weather.adapter.HourlyForecastAdapter;
import com.example.weather.adapter.MyPagerAdapter;
import com.example.weather.adapter.OnItemDayListener;
import com.example.weather.adapter.OnItemDeletedListener;
import com.example.weather.adapter.WeatherDayAdapter;
import com.example.weather.adapter.WeatherDetailAdapter;
import com.example.weather.data.InfoLocationDatabaseHelper;
import com.example.weather.model.CurrentWeatherResponse;
import com.example.weather.model.DayForecastResponse;
import com.example.weather.model.HourlyForecastResponse;
import com.example.weather.model.InfoLocationResponse;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastFragment extends Fragment {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    OkHttpClient client = new OkHttpClient();
    List<HourlyForecastResponse> hourlyForecastResponseList;
    List<DayForecastResponse> dayForecastResponsesList;
    RecyclerView dayRcv, detailRcv, detailInfoRcv;
    WeatherDayAdapter weatherDayAdapter;
    HourlyForecastAdapter hourlyForecastAdapter;
    WeatherDetailAdapter weatherDetailAdapter;
    List<List<DayForecastResponse>> allCityWeatherData;
    List<CurrentWeatherResponse> condition;
    View view_detail;
    int j;
    PageIndicatorView pageIndicatorView;
    LinearLayoutManager layoutManager_info;
    int lastVisibleItemPosition=0;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        dayRcv = view.findViewById(R.id.dayRcv);
        detailRcv = view.findViewById(R.id.detailRcv);
//        detailInfoRcv = view.findViewById(R.id.detailInfoRcv);
        view_detail = view.findViewById(R.id.view);
        viewPager=view.findViewById(R.id.viewPager);
        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_detail
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        layoutManager_info = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        dayRcv.setLayoutManager(layoutManager);
        detailRcv.setLayoutManager(layoutManager_detail);
//        detailInfoRcv.setLayoutManager(layoutManager_info);


        getWeatherData();
        return view;
    }

    private void getWeatherDataClick(String cityName) {
        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
        List<String> getAllName = db.getAllNames();
        allCityWeatherData = new ArrayList<>();
        condition = new ArrayList<>();
        Log.d("Test_5", "getWeatherData: " + getAllName.size());
        String apiKey = "55aa92d554f64328a5820946231707";
        int days = 5;
        String url = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + cityName + "&days=" + days;
        Request request = new Request.Builder()
                .url(url)
                .build();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {
                    // Handle the response here
                    if (response.isSuccessful()) {
                        final String responseData = response.body().string();

                        // Update UI with the response data on the main thread using handler.post()
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI here
                                JSONObject forecastData = null;
                                dayForecastResponsesList = new ArrayList<>();

                                try {
                                    forecastData = new JSONObject(responseData);
                                    JSONObject currentWeatherData_location = forecastData.getJSONObject("location");
                                    String nameLocation = currentWeatherData_location.getString("name");
                                    String hourseCurrent = currentWeatherData_location.getString("localtime");
                                    JSONObject currentWeatherData = forecastData.getJSONObject("current");
                                    double currentTemp = currentWeatherData.getDouble("temp_c");
                                    String currentCondition = currentWeatherData.getJSONObject("condition").getString("text");
                                    Log.d("Test_6", "run: " + currentCondition);
                                    CurrentWeatherResponse currentWeatherResponse = new CurrentWeatherResponse(currentTemp, currentCondition);
                                    System.out.println(String.format("Current Temp: %f, Current Condition: %s", currentTemp, currentCondition));
                                    condition.add(currentWeatherResponse);
                                    JSONArray forecastDays = forecastData.getJSONObject("forecast").getJSONArray("forecastday");
                                    for (int i = 0; i < forecastDays.length(); i++) {
                                        JSONObject day = forecastDays.getJSONObject(i);
                                        String date = day.getString("date");
                                        JSONObject dayData = day.getJSONObject("day");
                                        double maxTemp = dayData.getDouble("maxtemp_c");
                                        String condition_day = dayData.getJSONObject("condition").getString("text");
                                        double minTemp = dayData.getDouble("mintemp_c");
                                        hourlyForecastResponseList = new ArrayList<>();
                                        System.out.println(String.format("Date: %s, Max Temp: %f, Min Temp: %f", date, maxTemp, minTemp));

                                        JSONArray hourlyForecasts = day.getJSONArray("hour");
                                        for (int j = 0; j < hourlyForecasts.length(); j++) {
                                            JSONObject hourlyForecast = hourlyForecasts.getJSONObject(j);
                                            String time = hourlyForecast.getString("time");
                                            double temp = hourlyForecast.getDouble("temp_c");
                                            String condition = hourlyForecast.getJSONObject("condition").getString("text");
                                            double humidity = hourlyForecast.getDouble("humidity");
                                            double windSpeed = hourlyForecast.getDouble("wind_kph");
                                            double precipMm = hourlyForecast.getDouble("precip_mm");

                                            HourlyForecastResponse hourlyForecastResponse = new HourlyForecastResponse(time, temp, condition, humidity, windSpeed, precipMm);

                                            hourlyForecastResponseList.add(hourlyForecastResponse);


                                            System.out.println(String.format("Time: %s, Temp: %f, Condition: %s, Humidity: %f, Wind Speed: %f km/h, Precipitation: %f mm", time, temp, condition, humidity, windSpeed, precipMm));
                                        }
                                        Log.d("Test_7", "run: " + currentWeatherResponse.getCondition());
                                        DayForecastResponse dayForecastResponse = new DayForecastResponse(date, maxTemp, minTemp, hourlyForecastResponseList, currentWeatherResponse, hourseCurrent, condition_day);
                                        dayForecastResponsesList.add(dayForecastResponse);
                                    }
                                    weatherDayAdapter = new WeatherDayAdapter(getContext(), dayForecastResponsesList);
                                    dayRcv.setAdapter(weatherDayAdapter);
                                    weatherDayAdapter.setListener(new OnItemDayListener() {
                                        @Override
                                        public void onSelectItemDay(int position) {
                                            List<HourlyForecastResponse> hourlyForecastResponsesList = new ArrayList<>();
                                            hourlyForecastResponsesList = (dayForecastResponsesList.get(position).getHourlyForecasts());
                                            Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                            hourlyForecastAdapter = new HourlyForecastAdapter(getContext(), hourlyForecastResponsesList);
                                            detailRcv.setAdapter(hourlyForecastAdapter);
                                            view_detail.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    List<HourlyForecastResponse> hourlyForecastResponsesList = new ArrayList<>();
                                    hourlyForecastResponsesList = (dayForecastResponsesList.get(0).getHourlyForecasts());
                                    Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                    hourlyForecastAdapter = new HourlyForecastAdapter(getContext(), hourlyForecastResponsesList);
                                    detailRcv.setAdapter(hourlyForecastAdapter);
                                    view_detail.setVisibility(View.VISIBLE);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getWeatherData() {
        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
        List<String> getAllName = db.getAllNames();
        allCityWeatherData = new ArrayList<>();
        condition = new ArrayList<>();
        for (j = 0; j < getAllName.size(); j++) {
            Log.d("Test_5", "getWeatherData: " + getAllName.size());
            String apiKey = "55aa92d554f64328a5820946231707";
            String cityName = getAllName.get(j);
            ;
            int days = 5;

            String url = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + cityName + "&days=" + days;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try (Response response = client.newCall(request).execute()) {
                        // Handle the response here
                        if (response.isSuccessful()) {
                            final String responseData = response.body().string();

                            // Update UI with the response data on the main thread using handler.post()
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Update UI here
                                    JSONObject forecastData = null;
                                    dayForecastResponsesList = new ArrayList<>();

                                    try {
                                        forecastData = new JSONObject(responseData);
                                        JSONObject currentWeatherData_location = forecastData.getJSONObject("location");
                                        String nameLocation = currentWeatherData_location.getString("name");
                                        String hourseCurrent = currentWeatherData_location.getString("localtime");
                                        JSONObject currentWeatherData = forecastData.getJSONObject("current");
                                        double currentTemp = currentWeatherData.getDouble("temp_c");
                                        String currentCondition = currentWeatherData.getJSONObject("condition").getString("text");
                                        Log.d("Test_6", "run: " + currentCondition);
                                        CurrentWeatherResponse currentWeatherResponse = new CurrentWeatherResponse(currentTemp, currentCondition);
                                        System.out.println(String.format("Current Temp: %f, Current Condition: %s", currentTemp, currentCondition));
                                        condition.add(currentWeatherResponse);
                                        JSONArray forecastDays = forecastData.getJSONObject("forecast").getJSONArray("forecastday");
                                        for (int i = 0; i < forecastDays.length(); i++) {
                                            JSONObject day = forecastDays.getJSONObject(i);
                                            String date = day.getString("date");
                                            JSONObject dayData = day.getJSONObject("day");
                                            double maxTemp = dayData.getDouble("maxtemp_c");
                                            String condition_day = dayData.getJSONObject("condition").getString("text");
                                            double minTemp = dayData.getDouble("mintemp_c");
                                            hourlyForecastResponseList = new ArrayList<>();
                                            System.out.println(String.format("Date: %s, Max Temp: %f, Min Temp: %f", date, maxTemp, minTemp));

                                            JSONArray hourlyForecasts = day.getJSONArray("hour");
                                            for (int j = 0; j < hourlyForecasts.length(); j++) {
                                                JSONObject hourlyForecast = hourlyForecasts.getJSONObject(j);
                                                String time = hourlyForecast.getString("time");
                                                double temp = hourlyForecast.getDouble("temp_c");
                                                String condition = hourlyForecast.getJSONObject("condition").getString("text");
                                                double humidity = hourlyForecast.getDouble("humidity");
                                                double windSpeed = hourlyForecast.getDouble("wind_kph");
                                                double precipMm = hourlyForecast.getDouble("precip_mm");

                                                HourlyForecastResponse hourlyForecastResponse = new HourlyForecastResponse(time, temp, condition, humidity, windSpeed, precipMm);

                                                hourlyForecastResponseList.add(hourlyForecastResponse);


                                                System.out.println(String.format("Time: %s, Temp: %f, Condition: %s, Humidity: %f, Wind Speed: %f km/h, Precipitation: %f mm", time, temp, condition, humidity, windSpeed, precipMm));
                                            }
                                            Log.d("Test_7", "run: " + currentWeatherResponse.getCondition());
                                            DayForecastResponse dayForecastResponse = new DayForecastResponse(date, maxTemp, minTemp, hourlyForecastResponseList, currentWeatherResponse, hourseCurrent, condition_day);
                                            dayForecastResponsesList.add(dayForecastResponse);
                                        }
                                        weatherDayAdapter = new WeatherDayAdapter(getContext(), dayForecastResponsesList);
                                        dayRcv.setAdapter(weatherDayAdapter);
                                        weatherDayAdapter.setListener(new OnItemDayListener() {
                                            @Override
                                            public void onSelectItemDay(int position) {
                                                List<HourlyForecastResponse> hourlyForecastResponsesList = new ArrayList<>();
                                                hourlyForecastResponsesList = (dayForecastResponsesList.get(position).getHourlyForecasts());
                                                Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                                hourlyForecastAdapter = new HourlyForecastAdapter(getContext(), hourlyForecastResponsesList);
                                                detailRcv.setAdapter(hourlyForecastAdapter);
                                                view_detail.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        List<HourlyForecastResponse> hourlyForecastResponsesList = new ArrayList<>();
                                        hourlyForecastResponsesList = (dayForecastResponsesList.get(0).getHourlyForecasts());
                                        Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                        hourlyForecastAdapter = new HourlyForecastAdapter(getContext(), hourlyForecastResponsesList);
                                        detailRcv.setAdapter(hourlyForecastAdapter);
                                        view_detail.setVisibility(View.VISIBLE);
                                        allCityWeatherData.add(dayForecastResponsesList);
                                        Collections.reverse(allCityWeatherData);
                                        Log.d("Test_8", "run: " + condition.size());
                                        Collections.reverse(getAllName);
//                                        weatherDetailAdapter = new WeatherDetailAdapter(getContext(), allCityWeatherData, getAllName.get(0), condition.get(0));
//                                        Toast.makeText(getContext(), ""+weatherDetailAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
//                                        weatherDetailAdapter.setListener(new OnItemDeletedListener() {
//                                            @Override
//                                            public void onItemDelect(String check) {
//                                                Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
//                                                getWeatherDataClick(check);
//                                            }
//                                        });
//                                        detailInfoRcv.setAdapter(weatherDetailAdapter);
                                        MyPagerAdapter myPagerAdapter=new MyPagerAdapter(getContext(), allCityWeatherData, getAllName, condition);
                                        viewPager.setAdapter(myPagerAdapter);
                                        pageIndicatorView.setViewPager(viewPager);
                                        myPagerAdapter.setListener(new OnItemDeletedListener() {
                                            @Override
                                            public void onItemDelect(String check) {
                                                getWeatherDataClick(check);
                                            }
                                        });
//                                        pageIndicatorView.setCount(detailInfoRcv.getAdapter().getItemCount());
//                                        detailInfoRcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                                            @Override
//                                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                                if(dx>0){
//                                                    lastVisibleItemPosition=lastVisibleItemPosition+1;
//                                                    Toast.makeText(getContext(), ""+lastVisibleItemPosition, Toast.LENGTH_SHORT).show();
//                                                }else{
//                                                    lastVisibleItemPosition=lastVisibleItemPosition-1;
//                                                    Toast.makeText(getContext(), ""+lastVisibleItemPosition, Toast.LENGTH_SHORT).show();
//                                                }
////                                                if (dx > 0 && lastVisibleItemPosition < detailInfoRcv.getAdapter().getItemCount()) {
////                                                    pageIndicatorView.setSelection(2);
////                                                }
////                                                super.onScrolled(recyclerView, dx, dy);
//                                            }
//                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}