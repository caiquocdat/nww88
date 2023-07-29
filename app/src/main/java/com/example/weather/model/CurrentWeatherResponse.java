package com.example.weather.model;

public class CurrentWeatherResponse {
    private double temp;
    private String condition;

    public CurrentWeatherResponse(double temp, String condition) {
        this.temp = temp;
        this.condition = condition;
    }

    public double getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }
}

