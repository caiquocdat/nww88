package com.example.weather.model;

public class InfoLocationResponse {
    private int id;
    private String country;
    private String name;
    private String text;
    private double temperatureCelsius;

    public InfoLocationResponse() {
    }

    public InfoLocationResponse(int id, String country, String name, String text, double temperatureCelsius) {
        this.id = id;
        this.country = country;
        this.name = name;
        this.text = text;
        this.temperatureCelsius = temperatureCelsius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }
}
