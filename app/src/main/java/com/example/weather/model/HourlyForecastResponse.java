package com.example.weather.model;

public class HourlyForecastResponse {
    private String time;
    private double temp;
    private String condition;
    private double humidity;
    private double windSpeed;
    private double precipMm;

    public HourlyForecastResponse(String time, double temp, String condition, double humidity, double windSpeed, double precipMm) {
        this.time = time;
        this.temp = temp;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.precipMm = precipMm;
    }

    public String getTime() {
        return time;
    }

    public double getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPrecipMm() {
        return precipMm;
    }
}
