package dat.mid.weather.fragment;

import android.os.Bundle;

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


import dat.mid.weather.R;
import dat.mid.weather.adapter.HourlyAdapter;
import dat.mid.weather.adapter.PagerAdapter;
import dat.mid.weather.adapter.OnDayListener;
import dat.mid.weather.adapter.OnDeletedListener;
import dat.mid.weather.adapter.WeatherDayAdapter;
import dat.mid.weather.adapter.WeatherDetailAdapter;
import dat.mid.weather.data.DatabaseHelper;
import dat.mid.weather.model.CurrentResponse;
import dat.mid.weather.model.DayResponse;
import dat.mid.weather.model.HourlyResponse;

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
    List<HourlyResponse> hourlyForecastResponseList;
    List<DayResponse> dayForecastResponsesList;
    RecyclerView dayRcv, detailRcv, detailInfoRcv;
    WeatherDayAdapter weatherDayAdapter;
    HourlyAdapter hourlyForecastAdapter;
    WeatherDetailAdapter weatherDetailAdapter;
    List<List<DayResponse>> allCityWeatherData;
    List<CurrentResponse> condition;
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



        getWeatherData();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Test_33", "onPageSelected: "+position);
                DatabaseHelper db = new DatabaseHelper(getActivity());
                List<String> getAllName = db.getAllNames();
                getWeatherDataClick(getAllName.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private void getWeatherDataClick(String cityName) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
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
                                    CurrentResponse currentWeatherResponse = new CurrentResponse(currentTemp, currentCondition);
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

                                            HourlyResponse hourlyForecastResponse = new HourlyResponse(time, temp, condition, humidity, windSpeed, precipMm);

                                            hourlyForecastResponseList.add(hourlyForecastResponse);


                                            System.out.println(String.format("Time: %s, Temp: %f, Condition: %s, Humidity: %f, Wind Speed: %f km/h, Precipitation: %f mm", time, temp, condition, humidity, windSpeed, precipMm));
                                        }
                                        Log.d("Test_7", "run: " + currentWeatherResponse.getCondition());
                                        DayResponse dayForecastResponse = new DayResponse(date, maxTemp, minTemp, hourlyForecastResponseList, currentWeatherResponse, hourseCurrent, condition_day);
                                        dayForecastResponsesList.add(dayForecastResponse);
                                    }
                                    weatherDayAdapter = new WeatherDayAdapter(getContext(), dayForecastResponsesList);
                                    dayRcv.setAdapter(weatherDayAdapter);
                                    weatherDayAdapter.setListener(new OnDayListener() {
                                        @Override
                                        public void onSelectItemDay(int position) {
                                            List<HourlyResponse> hourlyForecastResponsesList = new ArrayList<>();
                                            hourlyForecastResponsesList = (dayForecastResponsesList.get(position).getHourlyForecasts());
                                            Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                            hourlyForecastAdapter = new HourlyAdapter(getContext(), hourlyForecastResponsesList);
                                            detailRcv.setAdapter(hourlyForecastAdapter);
                                            view_detail.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    List<HourlyResponse> hourlyForecastResponsesList = new ArrayList<>();
                                    hourlyForecastResponsesList = (dayForecastResponsesList.get(0).getHourlyForecasts());
                                    Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                    hourlyForecastAdapter = new HourlyAdapter(getContext(), hourlyForecastResponsesList);
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
        DatabaseHelper db = new DatabaseHelper(getActivity());
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
                                        CurrentResponse currentWeatherResponse = new CurrentResponse(currentTemp, currentCondition);
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

                                                HourlyResponse hourlyForecastResponse = new HourlyResponse(time, temp, condition, humidity, windSpeed, precipMm);

                                                hourlyForecastResponseList.add(hourlyForecastResponse);


                                                System.out.println(String.format("Time: %s, Temp: %f, Condition: %s, Humidity: %f, Wind Speed: %f km/h, Precipitation: %f mm", time, temp, condition, humidity, windSpeed, precipMm));
                                            }
                                            Log.d("Test_7", "run: " + currentWeatherResponse.getCondition());
                                            DayResponse dayForecastResponse = new DayResponse(date, maxTemp, minTemp, hourlyForecastResponseList, currentWeatherResponse, hourseCurrent, condition_day);
                                            dayForecastResponsesList.add(dayForecastResponse);
                                        }
                                        weatherDayAdapter = new WeatherDayAdapter(getContext(), dayForecastResponsesList);
                                        dayRcv.setAdapter(weatherDayAdapter);
                                        weatherDayAdapter.setListener(new OnDayListener() {
                                            @Override
                                            public void onSelectItemDay(int position) {
                                                List<HourlyResponse> hourlyForecastResponsesList = new ArrayList<>();
                                                hourlyForecastResponsesList = (dayForecastResponsesList.get(position).getHourlyForecasts());
                                                Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                                hourlyForecastAdapter = new HourlyAdapter(getContext(), hourlyForecastResponsesList);
                                                detailRcv.setAdapter(hourlyForecastAdapter);
                                                view_detail.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        List<HourlyResponse> hourlyForecastResponsesList = new ArrayList<>();
                                        hourlyForecastResponsesList = (dayForecastResponsesList.get(0).getHourlyForecasts());
                                        Log.d("Test_3", "run: " + dayForecastResponsesList.get(1).getHourlyForecasts().size());
                                        hourlyForecastAdapter = new HourlyAdapter(getContext(), hourlyForecastResponsesList);
                                        detailRcv.setAdapter(hourlyForecastAdapter);
                                        view_detail.setVisibility(View.VISIBLE);
                                        allCityWeatherData.add(dayForecastResponsesList);
                                        Collections.reverse(allCityWeatherData);
                                        Log.d("Test_8", "run: " + condition.size());
                                        Collections.reverse(getAllName);
                                        PagerAdapter myPagerAdapter=new PagerAdapter(getContext(), allCityWeatherData, getAllName, condition);
                                        viewPager.setAdapter(myPagerAdapter);
                                        pageIndicatorView.setViewPager(viewPager);
                                        myPagerAdapter.setListener(new OnDeletedListener() {
                                            @Override
                                            public void onItemDelect(String check) {
                                                Log.d("Test_21", "onItemDelect: "+check);
                                                getWeatherDataClick(check);
                                            }
                                        });



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
    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();

    }
}