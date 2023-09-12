package dat.mid.weather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dat.mid.weather.model.CurrentResponse;
import dat.mid.weather.model.DayResponse;

import java.util.Collections;
import java.util.List;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    private Context context;
    private List<List<DayResponse>> weatherList;
    private List<String> cityNameList;
    private List<CurrentResponse> condition;
    private OnDeletedListener listener;

    public PagerAdapter(Context context, List<List<DayResponse>> weatherList, List<String> cityNameList, List<CurrentResponse> condition) {
        this.context = context;
        this.weatherList = weatherList;
        this.cityNameList = cityNameList;
        this.condition = condition;
    }

    public void setListener(OnDeletedListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        if (listener != null) {
//            listener.onItemDelect(cityNameList.get(position));
//        }
        for (int i = 0; i < cityNameList.size(); i++) {
            Log.d("Test_30", "instantiateItem: " + cityNameList.get(i));
        }
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        WeatherDetailAdapter adapter = new WeatherDetailAdapter(context, Collections.singletonList(weatherList.get(position)), cityNameList.get(position), condition.get(position));
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RecyclerView) object);
    }
}


