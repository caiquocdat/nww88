package dat.mid.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import dat.mid.weather.fragment.ForecastFragment;
import dat.mid.weather.fragment.LocationFragment;
import dat.mid.weather.fragment.RadarFragment;
import dat.mid.weather.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {
    LinearLayout weartherLinear,radarLinear,locationLinear,settingLinear;
    private static int REQUEST_LOCATION_PERMISSION = 1;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isGPSEnabled()) {

        } else {
            turnOnGPS();
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // READ_EXTERNAL_STORAGE permission has not been granted.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {


        }
        mapping();
        hideSystemUI();
        RadarFragment myFragment = new RadarFragment();

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
    private void turnOnGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(MainActivity.this)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnable = false;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

    private void mapping() {
        weartherLinear=findViewById(R.id.weartherBrg);
        radarLinear=findViewById(R.id.radarBrg);
        locationLinear=findViewById(R.id.locationBrg);
        settingLinear=findViewById(R.id.settingBrg);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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