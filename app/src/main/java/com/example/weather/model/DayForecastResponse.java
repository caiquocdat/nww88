package com.example.weather.model;

import java.util.List;

public class DayForecastResponse {
    private String date;
    private double maxTemp;
    private double minTemp;
    private List<HourlyForecastResponse> hourlyForecasts;
    private CurrentWeatherResponse currentWeather;
    private String hour;
    private String condition_day ;

    public DayForecastResponse(String date, double maxTemp, double minTemp, List<HourlyForecastResponse> hourlyForecasts, CurrentWeatherResponse currentWeather, String hour, String condition_day) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.hourlyForecasts = hourlyForecasts;
        this.currentWeather = currentWeather;
        this.hour = hour;
        this.condition_day = condition_day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public List<HourlyForecastResponse> getHourlyForecasts() {
        return hourlyForecasts;
    }

    public void setHourlyForecasts(List<HourlyForecastResponse> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    public CurrentWeatherResponse getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeatherResponse currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCondition_day() {
        return condition_day;
    }

    public void setCondition_day(String condition_day) {
        this.condition_day = condition_day;
    }
}
