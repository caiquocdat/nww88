package dat.mid.weather.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Arrays;
import java.util.List;

import dat.mid.weather.R;

public class SettingFragment extends Fragment {
    private List<TextView> textViewWindList;
    private List<TextView> textViewPumpList;
    private List<TextView> textViewRainList;
    private List<TextView> textViewDistanceList;
    TextView fTv, cTv, miTv, kmTv, mTv, nutTv, inchTv, mmTv, mbarTv, inchRainTv, mmRainTv, damTv, kmDistanceTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences sharedPreferences_temp = getContext().getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences sharedPreferences_wind = getContext().getSharedPreferences("wind", MODE_PRIVATE);
        fTv = view.findViewById(R.id.fTv);
        cTv = view.findViewById(R.id.cTv);
        miTv = view.findViewById(R.id.miTv);
        kmTv = view.findViewById(R.id.kmTv);
        mTv = view.findViewById(R.id.mTv);
        nutTv = view.findViewById(R.id.nutTv);
        inchTv = view.findViewById(R.id.inchTv);
        mmTv = view.findViewById(R.id.mmTv);
        mbarTv = view.findViewById(R.id.mbarTv);
        textViewWindList = Arrays.asList(miTv, kmTv, mTv, nutTv);
        textViewPumpList = Arrays.asList(inchTv, mmTv, mbarTv);
        textViewRainList = Arrays.asList(inchRainTv, mmRainTv);
        textViewDistanceList = Arrays.asList(damTv, kmDistanceTv);
        int value_temp = sharedPreferences_temp.getInt("KEY", 1);
        int value_wind = sharedPreferences_wind.getInt("KEY", 1);
        if (value_temp == 0) {
            fTv.setBackgroundResource(R.drawable.parameter_item_click_brg);
            cTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
        }
        if (value_wind == 0) {
            miTv.setBackgroundResource(R.drawable.parameter_item_click_brg);
            kmTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            mTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            nutTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
        }else if (value_wind == 2) {
            miTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            kmTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            mTv.setBackgroundResource(R.drawable.parameter_item_click_brg);
            nutTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
        }if (value_wind == 3) {
            miTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            kmTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            mTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
            nutTv.setBackgroundResource(R.drawable.parameter_item_click_brg);
        }


        cTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cTv.setBackgroundResource(R.drawable.parameter_item_click_brg);
                fTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
                SharedPreferences.Editor editor = sharedPreferences_temp.edit();
                editor.putInt("KEY", 1); // Thay "KEY" và "VALUE" bằng khóa và giá trị của bạn
                editor.apply(); // Đừng quên gọi hàm apply() để lưu thay đổi
            }
        });
        fTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fTv.setBackgroundResource(R.drawable.parameter_item_click_brg);
                cTv.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
                SharedPreferences.Editor editor = sharedPreferences_temp.edit();
                editor.putInt("KEY", 0); // Thay "KEY" và "VALUE" bằng khóa và giá trị của bạn
                editor.apply(); // Đừng quên gọi hàm apply() để lưu thay đổi
            }
        });


        for (TextView tv : textViewWindList) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (TextView item : textViewWindList) {
                        if (item.getId() == v.getId()) {
                            item.setBackgroundResource(R.drawable.parameter_item_click_brg);
                            if (item.getText().toString().contains("mi/h")) {
                                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences_wind.edit();
                                editor.putInt("KEY", 0); // Thay "KEY" và "VALUE" bằng khóa và giá trị của bạn
                                editor.apply(); // Đừng quên gọi hàm apply() để lưu thay
                            } else if(item.getText().equals("km/h")) {
                                SharedPreferences.Editor editor = sharedPreferences_wind.edit();
                                editor.putInt("KEY", 1); // Thay "KEY" và "VALUE" bằng khóa và giá trị của bạn
                                editor.apply(); // Đừng quên gọi hàm apply() để lưu thay
                            }else if(item.getText().equals("m/s")) {
                                SharedPreferences.Editor editor = sharedPreferences_wind.edit();
                                editor.putInt("KEY", 2); // Thay "KEY" và "VALUE" bằng khóa và giá trị của bạn
                                editor.apply(); // Đừng quên gọi hàm apply() để lưu thay
                            }else if(item.getText().equals("nút")) {
                                SharedPreferences.Editor editor = sharedPreferences_wind.edit();
                                editor.putInt("KEY", 3); // Thay "KEY" và "VALUE" bằng khóa và giá trị của bạn
                                editor.apply(); // Đừng quên gọi hàm apply() để lưu thay
                            }
                        } else {
                            item.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
                        }
                    }
                }
            });
        }
        for (TextView tv : textViewPumpList) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (TextView item : textViewPumpList) {
                        if (item.getId() == v.getId()) {
                            item.setBackgroundResource(R.drawable.parameter_item_click_brg);
                        } else {
                            item.setBackgroundResource(R.drawable.parameter_item_un_click_brg);
                        }
                    }
                }
            });
        }


        return view;
    }
    private void hideSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();

    }
}