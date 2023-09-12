package dat.mid.weather.model;

import java.util.List;

public class DayResponse {
    private String date;
    private double maxTemp;
    private double minTemp;
    private List<HourlyResponse> hourlyForecasts;
    private CurrentResponse currentWeather;
    private String hour;
    private String condition_day ;

    public DayResponse(String date, double maxTemp, double minTemp, List<HourlyResponse> hourlyForecasts, CurrentResponse currentWeather, String hour, String condition_day) {
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

    public List<HourlyResponse> getHourlyForecasts() {
        return hourlyForecasts;
    }

    public void setHourlyForecasts(List<HourlyResponse> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    public CurrentResponse getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentResponse currentWeather) {
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
