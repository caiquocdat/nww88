package dat.mid.weather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import dat.mid.weather.R;
import dat.mid.weather.model.DayResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class WeatherDayAdapter extends RecyclerView.Adapter<WeatherDayAdapter.ForecastViewHolder> {
    private List<DayResponse> forecastDataList;
    private Context context;
    private int selectedPosition = 0;
    private OnDayListener listener;

    public WeatherDayAdapter(Context context, List<DayResponse> forecastDataList) {
        this.context = context;
        this.forecastDataList = forecastDataList;
    }

    public void setListener(OnDayListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_day, parent, false);
        return new ForecastViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        DayResponse forecastData = forecastDataList.get(position);
        // TODO: Update rainImg based on weather condition

        if (position == 0) {
            holder.dayTv.setText("Today");
        } else {
            String date = forecastData.getDate();

// Chuyển đổi chuỗi date thành đối tượng LocalDate
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

// Lấy ngày trong tuần từ đối tượng LocalDate
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("en"));

// Lấy ngày và tháng từ đối tượng LocalDate
            int dayOfMonth = localDate.getDayOfMonth();
            int month = localDate.getMonthValue();

// Tạo chuỗi kết quả dạng "Thứ ngày/tháng"
            String result = String.format("%s %d/%d", dayOfWeekString, dayOfMonth, month);

            holder.dayTv.setText(result);
        }
        holder.itemRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                // Notify the adapter that item views need to be refreshed
                notifyDataSetChanged();
                if (listener!=null){
                    listener.onSelectItemDay(selectedPosition);
                }
            }
        });
        holder.view.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);
        holder.temMaxTv.setText(context.getString(R.string.temperature_format, forecastData.getMaxTemp()));
        holder.temMinTv.setText(context.getString(R.string.temperature_format, forecastData.getMinTemp()));
        if (forecastData.getHourlyForecasts().get(position).getCondition().toLowerCase().contains("rain".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_rain);
            holder.rainImg.setImageDrawable(drawable);
        } else if (forecastData.getHourlyForecasts().get(position).getCondition().toLowerCase().contains("clear".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_cle);
            holder.rainImg.setImageDrawable(drawable);
        } else if (forecastData.getHourlyForecasts().get(position).getCondition().toLowerCase().contains("Sunny".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_sun);
            holder.rainImg.setImageDrawable(drawable);
        } else if (forecastData.getHourlyForecasts().get(position).getCondition().toLowerCase().contains("cloudy".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_more_clo);
            holder.rainImg.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return forecastDataList.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        ImageView rainImg;
        TextView dayTv, temMaxTv, temMinTv;
        View view;
        RelativeLayout itemRel;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            rainImg = itemView.findViewById(R.id.rainImg);
            dayTv = itemView.findViewById(R.id.dayTv);
            temMaxTv = itemView.findViewById(R.id.temMaxTv);
            temMinTv = itemView.findViewById(R.id.temMinTv);
            view = itemView.findViewById(R.id.view);
            itemRel = itemView.findViewById(R.id.itemRel);
        }
    }
}
