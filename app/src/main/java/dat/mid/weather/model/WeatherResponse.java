package dat.mid.weather.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private Current current;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    // getters and setters


    public static class Location {
        @SerializedName("name")
        private String name;

        @SerializedName("region")
        private String region;

        @SerializedName("country")
        private String country;

        @SerializedName("lat")
        private double latitude;

        @SerializedName("lon")
        private double longitude;

        @SerializedName("tz_id")
        private String timeZoneId;

        @SerializedName("localtime_epoch")
        private long localtimeEpoch;

        @SerializedName("localtime")
        private String localtime;

        // getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getTimeZoneId() {
            return timeZoneId;
        }

        public void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
        }

        public long getLocaltimeEpoch() {
            return localtimeEpoch;
        }

        public void setLocaltimeEpoch(long localtimeEpoch) {
            this.localtimeEpoch = localtimeEpoch;
        }

        public String getLocaltime() {
            return localtime;
        }

        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }
    }

    public static class Current {
        @SerializedName("last_updated_epoch")
        private long lastUpdatedEpoch;

        @SerializedName("last_updated")
        private String lastUpdated;

        @SerializedName("temp_c")
        private double temperatureCelsius;

        @SerializedName("temp_f")
        private double temperatureFahrenheit;

        @SerializedName("is_day")
        private int isDay;

        @SerializedName("condition")
        private Condition condition;
        @SerializedName("wind_mph")
        private double windMph;

        @SerializedName("wind_kph")
        private double windKph;

        @SerializedName("wind_degree")
        private int windDegree;

        @SerializedName("wind_dir")
        private String windDir;

        @SerializedName("pressure_mb")
        private double pressureMb;

        @SerializedName("pressure_in")
        private double pressureIn;

        @SerializedName("precip_mm")
        private double precipMm;

        @SerializedName("precip_in")
        private double precipIn;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("cloud")
        private int cloud;

        @SerializedName("feelslike_c")
        private double feelslikeC;

        @SerializedName("feelslike_f")
        private double feelslikeF;

        @SerializedName("vis_km")
        private double visKm;

        @SerializedName("vis_miles")
        private double visMiles;

        @SerializedName("uv")
        private double uv;

        @SerializedName("gust_mph")
        private double gustMph;

        @SerializedName("gust_kph")
        private double gustKph;

        public long getLastUpdatedEpoch() {
            return lastUpdatedEpoch;
        }

        public void setLastUpdatedEpoch(long lastUpdatedEpoch) {
            this.lastUpdatedEpoch = lastUpdatedEpoch;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public double getTemperatureCelsius() {
            return temperatureCelsius;
        }

        public void setTemperatureCelsius(double temperatureCelsius) {
            this.temperatureCelsius = temperatureCelsius;
        }

        public double getTemperatureFahrenheit() {
            return temperatureFahrenheit;
        }

        public void setTemperatureFahrenheit(double temperatureFahrenheit) {
            this.temperatureFahrenheit = temperatureFahrenheit;
        }

        public int getIsDay() {
            return isDay;
        }

        public void setIsDay(int isDay) {
            this.isDay = isDay;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public double getWindMph() {
            return windMph;
        }

        public void setWindMph(double windMph) {
            this.windMph = windMph;
        }

        public double getWindKph() {
            return windKph;
        }

        public void setWindKph(double windKph) {
            this.windKph = windKph;
        }

        public int getWindDegree() {
            return windDegree;
        }

        public void setWindDegree(int windDegree) {
            this.windDegree = windDegree;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public double getPressureMb() {
            return pressureMb;
        }

        public void setPressureMb(double pressureMb) {
            this.pressureMb = pressureMb;
        }

        public double getPressureIn() {
            return pressureIn;
        }

        public void setPressureIn(double pressureIn) {
            this.pressureIn = pressureIn;
        }

        public double getPrecipMm() {
            return precipMm;
        }

        public void setPrecipMm(double precipMm) {
            this.precipMm = precipMm;
        }

        public double getPrecipIn() {
            return precipIn;
        }

        public void setPrecipIn(double precipIn) {
            this.precipIn = precipIn;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getCloud() {
            return cloud;
        }

        public void setCloud(int cloud) {
            this.cloud = cloud;
        }

        public double getFeelslikeC() {
            return feelslikeC;
        }

        public void setFeelslikeC(double feelslikeC) {
            this.feelslikeC = feelslikeC;
        }

        public double getFeelslikeF() {
            return feelslikeF;
        }

        public void setFeelslikeF(double feelslikeF) {
            this.feelslikeF = feelslikeF;
        }

        public double getVisKm() {
            return visKm;
        }

        public void setVisKm(double visKm) {
            this.visKm = visKm;
        }

        public double getVisMiles() {
            return visMiles;
        }

        public void setVisMiles(double visMiles) {
            this.visMiles = visMiles;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }

        public double getGustMph() {
            return gustMph;
        }

        public void setGustMph(double gustMph) {
            this.gustMph = gustMph;
        }

        public double getGustKph() {
            return gustKph;
        }

        public void setGustKph(double gustKph) {
            this.gustKph = gustKph;
        }

        // Other fields and getters/setters...


        public static class Condition {
            @SerializedName("text")
            private String text;

            @SerializedName("icon")
            private String icon;

            @SerializedName("code")
            private int code;

            // getters and setters

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }
        }
    }
}
