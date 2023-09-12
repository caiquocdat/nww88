package dat.mid.weather.fragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dat.mid.weather.R;
import dat.mid.weather.adapter.OnDeletedListener;
import dat.mid.weather.adapter.WeatherAdapter;
import dat.mid.weather.api.RetrofitClient;
import dat.mid.weather.api.WeatherService;
import dat.mid.weather.data.InfoLocationDatabaseHelper;
import dat.mid.weather.model.InfoResponse;
import dat.mid.weather.model.WeatherResponse;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationFragment extends Fragment {
    RecyclerView locationRcv;
    WeatherAdapter weatherAdapter;
    EditText locationEdt;
    ImageView searchImg;
    TextView editTv, doneTv;
    private static int REQUEST_LOCATION_PERMISSION = 1;
    private LocationRequest locationRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // READ_EXTERNAL_STORAGE permission has not been granted.
//                if (isGPSEnabled()) {
//
//                    LocationServices.getFusedLocationProviderClient(getActivity())
//                            .requestLocationUpdates(locationRequest, new LocationCallback() {
//                                @Override
//                                public void onLocationResult(@NonNull LocationResult locationResult) {
//                                    super.onLocationResult(locationResult);
//
//                                    LocationServices.getFusedLocationProviderClient(getActivity())
//                                            .removeLocationUpdates(this);
//
//                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
//
//                                        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//                                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            // TODO: Consider calling
//                                            //    ActivityCompat#requestPermissions
//                                            // here to request the missing permissions, and then overriding
//                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                            //                                          int[] grantResults)
//                                            // to handle the case where the user grants the permission. See the documentation
//                                            // for ActivityCompat#requestPermissions for more details.
//                                            return;
//                                        }
//                                        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                                            @Override
//                                            public void onSuccess(Location location) {
//                                                if (location != null) {
//                                                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                                                    List<Address> addresses = null;
//                                                    try {
//                                                        addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
//                                                    } catch (IOException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    Log.d("Test_8", "lal: " + currentLatLng.latitude + ", long: " + currentLatLng.longitude);
//
//                                                    if (addresses != null && !addresses.isEmpty()) {
//                                                        Address address = addresses.get(0);
//                                                        String cityName = address.getSubAdminArea();
//                                                        setUpInfoLocation(cityName);
//                                                        // Use the city name as needed
//                                                    }
//                                                }
//                                            }
//                                        });
//                                    }
//                                }
//                            }, Looper.getMainLooper());
//
//                } else {
//                    turnOnGPS();
//                }
//            } else {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//
//            }
//        }

        locationRcv = view.findViewById(R.id.locationRcv);
        locationEdt = view.findViewById(R.id.locationEdt);
        editTv = view.findViewById(R.id.editTv);
        doneTv = view.findViewById(R.id.doneTv);
        searchImg = view.findViewById(R.id.searchImg);
        locationRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
        List<InfoResponse> infoLocationResponseList = db.getAllWeather();

//
//        if (isGPSEnabled()) {
//
//        } else {
//            turnOnGPS();
//        }
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("Test_8", "lal: " + currentLatLng.latitude + ", long: " + currentLatLng.longitude);

                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            String cityName = address.getAddressLine(0);
                            String[] parts = cityName.split(",");
                            String lastPart = parts[parts.length - 2].trim();
                            Log.d("Test_10", "city: " + lastPart);
                            setUpInfoLocation(lastPart);
                            // Use the city name as needed
                        }
                    }
                }
            });


        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            requestLocationPermission();
        }
        weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "false");
        locationRcv.setAdapter(weatherAdapter);
//        }
//        else {
//            turnOnGPS();
//        }


//        weatherAdapter.setListener(new OnItemDeletedListener() {
//            @Override
//            public void onItemDelect(String check) {
//                Toast.makeText(getActivity(), ""+check, Toast.LENGTH_SHORT).show();
//                if (check.equals("true")){
//                    Toast.makeText(getActivity(), "a", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                List<InfoResponse> infoLocationResponseList = db.getAllWeather();
                weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "true");
                weatherAdapter.notifyDataSetChanged();
                locationRcv.setAdapter(weatherAdapter);
                editTv.setVisibility(View.GONE);
                doneTv.setVisibility(View.VISIBLE);
                weatherAdapter.setListener(new OnDeletedListener() {
                    @Override
                    public void onItemDelect(String check) {
                        if (check.equals("true")) {
                            InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                            List<InfoResponse> infoLocationResponseList = db.getAllWeather();
                            // Create adapter and set it to RecyclerView
                            weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "true");
                            locationRcv.setAdapter(weatherAdapter);
                        }
                    }
                });
            }
        });
        doneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                List<InfoResponse> infoLocationResponseList = db.getAllWeather();
                weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "false");
                weatherAdapter.notifyDataSetChanged();
                locationRcv.setAdapter(weatherAdapter);
                editTv.setVisibility(View.VISIBLE);
                doneTv.setVisibility(View.GONE);
            }
        });


        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpInfo();
                locationEdt.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(locationEdt.getWindowToken(), 0);
            }
        });

        return view;
    }

    private void turnOnGPS() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(getContext(), "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(getActivity(), 2);
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
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }
        isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

    void setUpInfo() {
        WeatherService weatherService = RetrofitClient.getWeatherService();
        Call<WeatherResponse> call = weatherService.getCurrentWeather("55aa92d554f64328a5820946231707", locationEdt.getText().toString());

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    // TODO: handle the response
                    WeatherResponse weather = response.body();
                    Log.d("Test_1", "onResponse: " + weather.getLocation().getName());
                    InfoResponse infoLocationResponse = new InfoResponse();
                    infoLocationResponse.setCountry(weather.getLocation().getCountry());
                    infoLocationResponse.setName(weather.getLocation().getName());
                    infoLocationResponse.setText(weather.getCurrent().getCondition().getText());
                    infoLocationResponse.setTemperatureCelsius(weather.getCurrent().getTemperatureCelsius());
                    InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                    db.insertWeather(infoLocationResponse);
                    List<InfoResponse> infoLocationResponseList = db.getAllWeather();
                    // Create adapter and set it to RecyclerView
                    weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "false");
                    locationRcv.setAdapter(weatherAdapter);

//                            List<InfoLocationResponse> weatherList = new ArrayList<>();
//                            weatherList.add(weather);
//                            Log.d("Test_2", "onResponse: "+weatherList.size());


                    // Create adapter and set it to RecyclerView
//                            weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
//                            locationRcv.setAdapter(weatherAdapter);

                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // TODO: handle the error
            }
        });
    }

    void setUpInfoLocation(String location) {
        WeatherService weatherService_new = RetrofitClient.getWeatherService();
        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
        List<InfoResponse> infoLocationResponseList = db.getAllWeather();
        if (infoLocationResponseList.size() == 0) {
            Call<WeatherResponse> call_new = weatherService_new.getCurrentWeather("55aa92d554f64328a5820946231707", location);

            call_new.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call_new, Response<WeatherResponse> response) {
                    if (response.isSuccessful()) {
                        // TODO: handle the response
                        WeatherResponse weather = response.body();
                        Log.d("Test_1", "onResponse: " + weather.getLocation().getName());
                        InfoResponse infoLocationResponse = new InfoResponse();
                        infoLocationResponse.setCountry(weather.getLocation().getCountry());
                        infoLocationResponse.setName(weather.getLocation().getName());
                        infoLocationResponse.setText(weather.getCurrent().getCondition().getText());
                        infoLocationResponse.setTemperatureCelsius(weather.getCurrent().getTemperatureCelsius());
                        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                        db.insertWeather(infoLocationResponse);
                        List<InfoResponse> infoLocationResponseList = db.getAllWeather();
                        // Create adapter and set it to RecyclerView
                        weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "false");
                        locationRcv.setAdapter(weatherAdapter);

//                            List<InfoLocationResponse> weatherList = new ArrayList<>();
//                            weatherList.add(weather);
//                            Log.d("Test_2", "onResponse: "+weatherList.size());


                        // Create adapter and set it to RecyclerView
//                            weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
//                            locationRcv.setAdapter(weatherAdapter);

                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    // TODO: handle the error
                }
            });
        } else {
            // Create adapter and set it to RecyclerView
            weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "false");
            locationRcv.setAdapter(weatherAdapter);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show dialog to explain why we need this permission
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(currentLatLng.latitude, currentLatLng.longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Test_8", "lal: " + currentLatLng.latitude + ", long: " + currentLatLng.longitude);

                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    String cityName = address.getAddressLine(0);
                                    String[] parts = cityName.split(",");
                                    String lastPart = parts[parts.length - 2].trim();
                                    Log.d("Test_10", "city: " + lastPart);
                                    setUpInfoLocation(lastPart);
                                    // Use the city name as needed
                                }
                            }else{
                                setUpInfoLocation("Ha Noi");
                            }
                        }
                    });
                }
            } else {
                // Permission denied
                setUpInfoLocation("Ha Noi");
            }
        }
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