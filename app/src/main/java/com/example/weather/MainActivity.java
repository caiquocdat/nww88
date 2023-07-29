package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout weartherLinear,radarLinear,locationLinear,settingLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        hideSystemUI();
        ForecastFragment myFragment = new ForecastFragment();

        // Sử dụng FragmentManager để quản lý việc hiển thị Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Bắt đầu giao tranh Fragment bằng cách sử dụng một FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thêm Fragment vào giao tranh Fragment bằng cách sử dụng phương thức add()
        fragmentTransaction.add(R.id.content, myFragment);

        // (Tuỳ chọn) Nếu bạn muốn thêm Fragment vào Back stack
        fragmentTransaction.addToBackStack(null);

        // Kết thúc giao tranh Fragment bằng cách sử dụng phương thức commit()
        fragmentTransaction.commit();
        weartherLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForecastFragment myFragment = new ForecastFragment();

                // Sử dụng FragmentManager để quản lý việc hiển thị Fragment
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Bắt đầu giao tranh Fragment bằng cách sử dụng một FragmentTransaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();

                // Thêm Fragment vào giao tranh Fragment bằng cách sử dụng phương thức add()
                fragmentTransaction.add(R.id.content, myFragment);

                // (Tuỳ chọn) Nếu bạn muốn thêm Fragment vào Back stack
                fragmentTransaction.addToBackStack(null);

                // Kết thúc giao tranh Fragment bằng cách sử dụng phương thức commit()
                fragmentTransaction.commit();
            }
        });
        radarLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadarFragment myFragment = new RadarFragment();

                // Sử dụng FragmentManager để quản lý việc hiển thị Fragment
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Bắt đầu giao tranh Fragment bằng cách sử dụng một FragmentTransaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();

                // Thêm Fragment vào giao tranh Fragment bằng cách sử dụng phương thức add()
                fragmentTransaction.add(R.id.content, myFragment);

                // (Tuỳ chọn) Nếu bạn muốn thêm Fragment vào Back stack
                fragmentTransaction.addToBackStack(null);

                // Kết thúc giao tranh Fragment bằng cách sử dụng phương thức commit()
                fragmentTransaction.commit();

            }
        });
        locationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationFragment myFragment = new LocationFragment();

                // Sử dụng FragmentManager để quản lý việc hiển thị Fragment
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Bắt đầu giao tranh Fragment bằng cách sử dụng một FragmentTransaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();

                // Thêm Fragment vào giao tranh Fragment bằng cách sử dụng phương thức add()
                fragmentTransaction.add(R.id.content, myFragment);

                // (Tuỳ chọn) Nếu bạn muốn thêm Fragment vào Back stack
                fragmentTransaction.addToBackStack(null);

                // Kết thúc giao tranh Fragment bằng cách sử dụng phương thức commit()
                fragmentTransaction.commit();

            }
        });
        settingLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment myFragment = new SettingFragment();

                // Sử dụng FragmentManager để quản lý việc hiển thị Fragment
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Bắt đầu giao tranh Fragment bằng cách sử dụng một FragmentTransaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();

                // Thêm Fragment vào giao tranh Fragment bằng cách sử dụng phương thức add()
                fragmentTransaction.add(R.id.content, myFragment);

                // (Tuỳ chọn) Nếu bạn muốn thêm Fragment vào Back stack
                fragmentTransaction.addToBackStack(null);

                // Kết thúc giao tranh Fragment bằng cách sử dụng phương thức commit()
                fragmentTransaction.commit();

            }
        });
    }

    private void mapping() {
        weartherLinear=findViewById(R.id.weartherBrg);
        radarLinear=findViewById(R.id.radarBrg);
        locationLinear=findViewById(R.id.locationBrg);
        settingLinear=findViewById(R.id.settingBrg);
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                         |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }
}