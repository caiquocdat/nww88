package com.example.weather.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.CurrentWeatherResponse;
import com.example.weather.model.DayForecastResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class WeatherDetailAdapter extends RecyclerView.Adapter<WeatherDetailAdapter.WeatherViewHolder> {

    private Context context;
    private List<List<DayForecastResponse>> weatherList;
    private String cityNameList;
    private CurrentWeatherResponse condition;
    private OnItemDeletedListener listener;

    public WeatherDetailAdapter(Context context, List<List<DayForecastResponse>> weatherList,String cityNameList, CurrentWeatherResponse condition) {
        this.context = context;
        this.weatherList = weatherList;
        this.cityNameList = cityNameList;
        this.condition = condition;
    }

    public void setListener(OnItemDeletedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wearther, parent, false);
        return new WeatherViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayForecastResponse weather = weatherList.get(0).get(position);
        holder.cityTv.setText(cityNameList);
        SharedPreferences sharedPreferences_temp = context.getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences sharedPreferences_wind = context.getSharedPreferences("wind", MODE_PRIVATE);
        int value = sharedPreferences_temp.getInt("KEY", 1); // Thay "KEY" và "DEFAULT_VALUE" bằng khóa và giá trị mặc định của bạn
        int value_wind = sharedPreferences_wind.getInt("KEY", 1); // Thay "KEY" và "DEFAULT_VALUE" bằng khóa và giá trị mặc định của bạn
        if (value == 0) {
            holder.tempTv.setText(context.getString(R.string.temperature_format, weather.getHourlyForecasts().get(0).getTemp() * 9 / 5 + 32));
            holder.tempMaxTv.setText("Cao " + context.getString(R.string.temperature_format, weather.getMaxTemp() * 9 / 5 + 32) + "F");
            holder.tempMinTv.setText("Thấp " + context.getString(R.string.temperature_format, weather.getMinTemp() * 9 / 5 + 32) + "F");
        } else {
            holder.tempTv.setText(context.getString(R.string.temperature_format, weather.getHourlyForecasts().get(0).getTemp()));
            holder.tempMaxTv.setText("Cao " + context.getString(R.string.temperature_format, weather.getMaxTemp()) + "C");
            holder.tempMinTv.setText("Thấp " + context.getString(R.string.temperature_format, weather.getMinTemp()) + "C");
        }
        holder.itemRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemDelect(cityNameList);
                }
            }
        });
        holder.humidityTv.setText(weather.getHourlyForecasts().get(0).getHumidity() + "%");
        holder.rainTv.setText(weather.getHourlyForecasts().get(0).getPrecipMm() + "");
        if (value_wind == 0) {
            holder.windTv.setText("TB Gió " + context.getString(R.string.wind_format,  weather.getHourlyForecasts().get(0).getWindSpeed() * 0.621371) + "mi/h");
        }else if (value_wind == 2) {
            holder.windTv.setText("TB Gió " + context.getString(R.string.wind_format, weather.getHourlyForecasts().get(0).getWindSpeed() * 0.277778) + "m/s");
        } else if (value_wind == 3) {
            holder.windTv.setText("TB Gió " + context.getString(R.string.wind_format, weather.getHourlyForecasts().get(0).getWindSpeed()  * 0.539957) + "knots");
        }else {
            holder.windTv.setText("TB Gió " + context.getString(R.string.wind_format, weather.getHourlyForecasts().get(0).getWindSpeed()) + "km/h");
        }
        holder.conditionTv.setText(condition.getCondition());
        if (weather.getCondition_day().toLowerCase().contains("sunny".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_weather_sun);
            holder.weatherIcon.setImageDrawable(drawable);
        }
        if (holder.conditionTv.getText().toString().toLowerCase().contains("sunny".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_weather_sun);
            holder.weatherHourImg.setImageDrawable(drawable);
        } else if (holder.conditionTv.getText().toString().toLowerCase().contains("clear".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_weather_clear);
            holder.weatherHourImg.setImageDrawable(drawable);
        } else if (holder.conditionTv.getText().toString().toLowerCase().contains("Thundery".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_weather_thundery);
            holder.weatherHourImg.setImageDrawable(drawable);
        }
        if (holder.conditionTv.getText().toString().toLowerCase().contains("rain".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_weather_rain);
            holder.weatherHourImg.setImageDrawable(drawable);
        }
        String date = weather.getDate();

// Chuyển đổi chuỗi date thành đối tượng LocalDate
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

// Lấy ngày trong tuần từ đối tượng LocalDate
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("vi"));

// Lấy ngày và tháng từ đối tượng LocalDate
        int dayOfMonth = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        String hourCurrent = weather.getHour();
        String lastFiveCharacters = hourCurrent.substring(hourCurrent.length() - 5);

// Tạo chuỗi kết quả dạng "Thứ ngày/tháng"
        String result = String.format("%s %s", lastFiveCharacters, dayOfWeekString);
        holder.timeTv.setText(result);
        // Thiết lập các giá trị khác nếu cần
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView cityTv, tempTv, tempMaxTv, tempMinTv, humidityTv, rainTv, windTv, timeTv, conditionTv;
        ImageView weatherIcon, weatherHourImg;
        RelativeLayout itemRel;

        WeatherViewHolder(View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.cityTv);
            tempTv = itemView.findViewById(R.id.tempTv);
            tempMaxTv = itemView.findViewById(R.id.tempMaxTv);
            tempMinTv = itemView.findViewById(R.id.tempMinTv);
            humidityTv = itemView.findViewById(R.id.humidityTv);
            rainTv = itemView.findViewById(R.id.rainTv);
            windTv = itemView.findViewById(R.id.windTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
            weatherHourImg = itemView.findViewById(R.id.weatherHourImg);
            conditionTv = itemView.findViewById(R.id.conditionTv);
            itemRel = itemView.findViewById(R.id.itemRel);
        }
    }
}

