package dat.mid.weather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import dat.mid.weather.R;
import dat.mid.weather.data.InfoLocationDatabaseHelper;
import dat.mid.weather.model.InfoResponse;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<InfoResponse> weatherList;
    private Context context;
    private String checkUpdate;
    private OnDeletedListener listener;

    public void setListener(OnDeletedListener listener) {
        this.listener = listener;
    }

    public WeatherAdapter(List<InfoResponse> weatherList, Context context, String checkUpdate) {
        this.weatherList = weatherList;
        this.context = context;
        this.checkUpdate = checkUpdate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoResponse weather = weatherList.get(position);
        holder.txtCountry.setText(weather.getCountry());
        holder.txtCity.setText(weather.getName());
        holder.txtCondition.setText(weather.getText());
        holder.txtTemperature.setText(String.format("%s°", weather.getTemperatureCelsius()));
        holder.delRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(context);
                List<InfoResponse> infoLocationResponseList = db.getAllWeather();
                if (infoLocationResponseList.size() == 1) {
                    Toast.makeText(context, "Bạn không thể xóa toàn bộ vị trí!", Toast.LENGTH_SHORT).show();
                } else {
                    db.deleteWeather(weather.getName());
                    if (listener != null) {
                        listener.onItemDelect("true");
                    }
                }
            }
        });

        // To load image from a url, you can use libraries like Glide or Picasso
        // For example, using Glide:
//        Glide.with(context)
//                .load("http:" + weather.getCurrent().getCondition().getIcon())
//                .into(holder.imgWeatherCondition);

        if (checkUpdate.equals("true")) {
            holder.delRel.setVisibility(View.VISIBLE);
            holder.txtTemperature.setVisibility(View.GONE);
        } else {
            holder.delRel.setVisibility(View.GONE);
            holder.txtTemperature.setVisibility(View.VISIBLE);
        }


        if (weather.getText().toLowerCase().contains("rain".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_rain);
            holder.imgWeatherCondition.setImageDrawable(drawable);
        } else if (weather.getText().toLowerCase().contains("clear".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_clear);
            holder.imgWeatherCondition.setImageDrawable(drawable);
        } else if (weather.getText().toLowerCase().contains("Sunny".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_sun);
            holder.imgWeatherCondition.setImageDrawable(drawable);
        } else if (weather.getText().toLowerCase().contains("Cloudy".toLowerCase())) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.img_more_cloud);
            holder.imgWeatherCondition.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountry, txtCity, txtCondition, txtTemperature;
        ImageView imgWeatherCondition;
        RelativeLayout delRel;

        public ViewHolder(View itemView) {
            super(itemView);
            imgWeatherCondition = itemView.findViewById(R.id.cloudyImg);
            delRel = itemView.findViewById(R.id.delectRel);
            txtCountry = itemView.findViewById(R.id.countyTv);
            txtCity = itemView.findViewById(R.id.cityTv);
            txtCondition = itemView.findViewById(R.id.conditionTv);
            txtTemperature = itemView.findViewById(R.id.tempTv);
        }
    }
}

