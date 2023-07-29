package com.example.weather.api;

import com.example.weather.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(@Query("key") String key, @Query("q") String location);
}
