package dat.mid.weather.api;

import dat.mid.weather.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("current.json")
    Call<WeatherResponse> getCurrentWeather(@Query("key") String key, @Query("q") String location);
}
