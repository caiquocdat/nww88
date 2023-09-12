package dat.mid.weather.model;

public class CurrentResponse {
    private double temp;
    private String condition;

    public CurrentResponse(double temp, String condition) {
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

