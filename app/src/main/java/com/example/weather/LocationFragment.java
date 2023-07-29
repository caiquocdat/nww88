package com.example.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

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

import com.example.weather.adapter.OnItemDeletedListener;
import com.example.weather.adapter.WeatherAdapter;
import com.example.weather.api.RetrofitClient;
import com.example.weather.api.WeatherService;
import com.example.weather.data.InfoLocationDatabaseHelper;
import com.example.weather.model.InfoLocationResponse;
import com.example.weather.model.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // READ_EXTERNAL_STORAGE permission has not been granted.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {


        }

        locationRcv = view.findViewById(R.id.locationRcv);
        locationEdt = view.findViewById(R.id.locationEdt);
        editTv = view.findViewById(R.id.editTv);
        doneTv = view.findViewById(R.id.doneTv);
        searchImg = view.findViewById(R.id.searchImg);
        locationRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
        List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
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
                        Log.d("Test_8", "lal: "+currentLatLng.latitude+", long: "+currentLatLng.longitude);

                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            String cityName = address.getSubAdminArea();
                            setUpInfoLocation(cityName);
                            // Use the city name as needed
                        }
                    }
                }
            });

        } else {
            requestLocationPermission();
        }


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
                List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
                weatherAdapter = new WeatherAdapter(infoLocationResponseList, getActivity(), "true");
                weatherAdapter.notifyDataSetChanged();
                locationRcv.setAdapter(weatherAdapter);
                editTv.setVisibility(View.GONE);
                doneTv.setVisibility(View.VISIBLE);
                weatherAdapter.setListener(new OnItemDeletedListener() {
                    @Override
                    public void onItemDelect(String check) {
                        if (check.equals("true")) {
                            InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                            List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
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
                List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
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
                    InfoLocationResponse infoLocationResponse = new InfoLocationResponse();
                    infoLocationResponse.setCountry(weather.getLocation().getCountry());
                    infoLocationResponse.setName(weather.getLocation().getName());
                    infoLocationResponse.setText(weather.getCurrent().getCondition().getText());
                    infoLocationResponse.setTemperatureCelsius(weather.getCurrent().getTemperatureCelsius());
                    InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                    db.insertWeather(infoLocationResponse);
                    List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
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
        List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
        if (infoLocationResponseList.size() == 0) {
            Call<WeatherResponse> call_new = weatherService_new.getCurrentWeather("55aa92d554f64328a5820946231707", location);

            call_new.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call_new, Response<WeatherResponse> response) {
                    if (response.isSuccessful()) {
                        // TODO: handle the response
                        WeatherResponse weather = response.body();
                        Log.d("Test_1", "onResponse: " + weather.getLocation().getName());
                        InfoLocationResponse infoLocationResponse = new InfoLocationResponse();
                        infoLocationResponse.setCountry(weather.getLocation().getCountry());
                        infoLocationResponse.setName(weather.getLocation().getName());
                        infoLocationResponse.setText(weather.getCurrent().getCondition().getText());
                        infoLocationResponse.setTemperatureCelsius(weather.getCurrent().getTemperatureCelsius());
                        InfoLocationDatabaseHelper db = new InfoLocationDatabaseHelper(getActivity());
                        db.insertWeather(infoLocationResponse);
                        List<InfoLocationResponse> infoLocationResponseList = db.getAllWeather();
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
                                    Log.d("Test_8", "lal: "+currentLatLng.latitude+", long: "+currentLatLng.longitude);

                                    if (addresses != null && !addresses.isEmpty()) {
                                        Address address = addresses.get(0);
                                        String cityName = address.getSubAdminArea();
                                        setUpInfoLocation(cityName);
                                        // Use the city name as needed
                                    }
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
    }
}