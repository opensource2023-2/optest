package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class weather_page extends AppCompatActivity implements LocationListener {
    private ProgressBar progressBar;
    private RelativeLayout rLHome;
    private TextView textCityName, textTemp, textConditions, textWindSpeed, textLastTime;
    private RecyclerView rvWeather, rvFavs;
    private ImageView imgBG, imgSearch, imgWeather, imgRefresh;
    private TextInputEditText editCityName;
    private ArrayList<WeatherModel> arr;
    private WeatherModelAdapter weatherModelAdapter;
    private int PERMISSION_CODE = 1;
    double lat, lon;
    private LocationManager locationManager;
    private Location location;
    String apiKey = "fc853b4d52b5b246574b3f6ff1f63387";
    String[] saveKey = {
            "CurrentWeatherData",
            "ForecastWeatherData",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);

        progressBar = findViewById(R.id.pBarLoading);
        rLHome = findViewById(R.id.RLHome);
        textCityName = findViewById(R.id.textCityName);
        textTemp = findViewById(R.id.textTemp);
        textConditions = findViewById(R.id.textConditions);
        rvWeather = findViewById(R.id.rvWeather); //rvWeather!!!!!!!
        imgBG = findViewById(R.id.imgBG);
        imgWeather = findViewById(R.id.imgWeather);
        imgSearch = findViewById(R.id.imgSearch);
        imgRefresh = findViewById(R.id.imgRefresh);
        editCityName = findViewById(R.id.editCityName);
        textWindSpeed = findViewById(R.id.textWindSpeed);
        textLastTime = findViewById(R.id.textLastTime);

        //수정
        String mylocal = "Cheongju-si";
        updateWeather(mylocal);

        arr = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Toast.makeText(this,"Fetching Last known location",Toast.LENGTH_SHORT).show();
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
        getCurrentWeather(lat, lon);  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!최신날씨 받기
        getForecastWeather(lat, lon);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        weatherModelAdapter = new WeatherModelAdapter(this, arr);
        rvWeather.setAdapter(weatherModelAdapter); //rvWeather!!!



        boolean isInternetAvailable = NetworkCheck.isNetworkAvailable(getApplicationContext());
        if(isInternetAvailable){
            for(int i=2;i<saveKey.length;i++){
                getFavCoord(saveKey[i],i);
            }
        }
        else{
            Toast.makeText(weather_page.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            for (int i=0;i<saveKey.length;i++) {
                retrieveLastResponse(i);
            }
        }

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInternetAvailable = NetworkCheck.isNetworkAvailable(getApplicationContext());
                if(isInternetAvailable){
                    String city = editCityName.getText().toString();
                    if (city.equals("")) {
                        Toast.makeText(weather_page.this, "Please enter city Name", Toast.LENGTH_SHORT).show();
                        editCityName.requestFocus();
                        editCityName.setError("Please Enter City Name");
                    }else{
                        updateWeather(city);
                    }
                }else {
                    Toast.makeText(weather_page.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //수정
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInternetAvailable = NetworkCheck.isNetworkAvailable(getApplicationContext());
                if (isInternetAvailable) {
                    String city = editCityName.getText().toString();
                    if (city.equals("")) {
                        Toast.makeText(weather_page.this, "Please enter city Name", Toast.LENGTH_SHORT).show();
                        editCityName.requestFocus();
                        editCityName.setError("Please Enter City Name");
                    } else {
                        updateWeather(city);
                    }
                } else {
                    Toast.makeText(weather_page.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInternetAvailable = NetworkCheck.isNetworkAvailable(getApplicationContext());
                if(isInternetAvailable){
                    Toast.makeText(weather_page.this,"Refreshing...",Toast.LENGTH_SHORT).show();
                    getCurrentWeather(lat,lon); //받아!!!!!!!!!!!!!!!!!
                    getForecastWeather(lat,lon);
                    for(int i=2;i<saveKey.length;i++){
                        getFavCoord(saveKey[i],i);
                    }
                }else {
                    Toast.makeText(weather_page.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                +city
                +"&appid="
                +apiKey
                +"&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(weather_page.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("cod").equals("404")){
                        Toast.makeText(weather_page.this, "Please enter correct city name", Toast.LENGTH_LONG).show();
                    }else {
                        textLastTime.setText(getCurrentTime());
                        lon = response.getJSONObject("coord").getDouble("lon");
                        lat = response.getJSONObject("coord").getDouble("lat");
                        getCurrentWeather(lat, lon); //받아!
                        getForecastWeather(lat, lon);
                    }
                } catch (Exception ex) {
                    Toast.makeText(weather_page.this, "Please enter correct city name", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fetch Error","city not found");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private String getCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return dateTime.format(formatter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(weather_page.this, "Permission Granted...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(weather_page.this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void getCurrentWeather(double lat, double lon) {
        RequestQueue requestQueue = Volley.newRequestQueue(weather_page.this);

        String urlCurrent = "https://api.openweathermap.org/data/2.5/weather?lat="
                + lat
                + "&lon="
                + lon
                + "&appid="
                + apiKey
                + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCurrent, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textLastTime.setText(getCurrentTime());
                saveLastResponse(response,0);
                progressBar.setVisibility(View.GONE);
                rLHome.setVisibility(View.VISIBLE);
                updateCurrentWeather(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fetch Error",error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateCurrentWeather(JSONObject response) {
        try{
            String city = response.getString("name");
            textCityName.setText(city);

            String temperature = response.getJSONObject("main").getString("temp");
            textTemp.setText(temperature + "°C");

            String img = response.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("icon");
            Picasso.get().load("https://openweathermap.org/img/w/" + img + ".png").into(imgWeather);

            String condition = response.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("main");
            textConditions.setText(condition);

            double wSpeed = response.getJSONObject("wind")
                    .getDouble("speed");
            textWindSpeed.setText(""+wSpeed+"Km/h");

            if(img.charAt(img.length()-1) == 'd')
                imgBG.setImageResource(R.drawable.day);
            if(img.charAt(img.length()-1) == 'n')
                imgBG.setImageResource(R.drawable.night);
        }catch (Exception e){
            Log.d("Update Res",e.getMessage());
        }

    }

    private void getForecastWeather(double lat, double lon) {
        RequestQueue requestQueue = Volley.newRequestQueue(weather_page.this);

        String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?lat="
                + lat
                + "&lon="
                + lon
                + "&appid="
                + apiKey
                + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlForecast, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textLastTime.setText(getCurrentTime());
                saveLastResponse(response,1);
                arr.clear();
                updateForecastWeather(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fetch Error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateForecastWeather(JSONObject response) {
        try {
            int loop = response.getInt("cnt");
            JSONArray forecast = response.getJSONArray("list");

            // Create a list to store forecast data before sorting
            ArrayList<WeatherModel> forecastList = new ArrayList<>();

            for (int i = 0; i < loop; i += 1) {
                String time = forecast.getJSONObject(i).getString("dt_txt");
                double temp = forecast.getJSONObject(i).getJSONObject("main").getDouble("temp");
                String condition = forecast.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                double wSpeed = forecast.getJSONObject(i).getJSONObject("wind").getDouble("speed");
                String pod = forecast.getJSONObject(i).getJSONObject("sys").getString("pod");

                // Add forecast data to the temporary list
                forecastList.add(new WeatherModel(temp, condition, wSpeed, time, pod));

                if (i == 0 && pod.equals("n")) {
                    imgBG.setImageResource(R.drawable.night);
                } else if (i == 0) {
                    imgBG.setImageResource(R.drawable.day);
                }
            }

            // Sort the list based on weather conditions (you may need to define a comparator)
            Collections.sort(forecastList, new WeatherComparator());

            // Clear the existing data in 'arr'
            arr.clear();

            // Add sorted data back to 'arr'
            arr.addAll(forecastList);

            // Notify the adapter that the data has changed
            weatherModelAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("Update Res", e.getMessage());
        }
    }
    public class WeatherComparator implements Comparator<WeatherModel> {
        @Override

        public int compare(WeatherModel weather1, WeatherModel weather2) {
            // 세탁지수 계산: (기온*0.729) - (0.056*풍속)
            double laundryIndex1 = (weather1.getTemp() * 0.729) - (0.056 * weather1.getwSpeed());
            double laundryIndex2 = (weather2.getTemp() * 0.729) - (0.056 * weather2.getwSpeed());

            // 세탁지수가 높은 순으로 정렬
            if (laundryIndex1 > laundryIndex2) {
                return -1; // weather1이 weather2보다 우선일 때
            } else if (laundryIndex1 < laundryIndex2) {
                return 1; // weather1이 weather2보다 후순위일 때
            } else {
                return 0; // 두 날씨가 동일한 우선순위일 때
            }
        }
    }


    @Override
    public void onLocationChanged(Location loca) {
        location = loca;

        lat = location.getLatitude();
        lon = location.getLongitude();

        Log.d("Latitude", String.valueOf(lat));
        Log.d("Longitude", String.valueOf(lon));

        // Stop receiving location updates if needed
        locationManager.removeUpdates(this);
    }

    private void saveLastResponse(JSONObject res, int time) {

        SharedPreferences sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try{
            String save = res.toString();
            editor.putString(saveKey[time], save);
            editor.putString("Time", getCurrentTime());
            editor.apply();
        }catch (Exception e){
            Log.d("Save Res",e.getMessage());
        }
    }


    private void retrieveLastResponse(int time) {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        String weatherData = sharedPreferences.getString(saveKey[time], "");
        String dateTime = sharedPreferences.getString("Time", "");
        textLastTime.setText(dateTime);
        try {
            JSONObject response = new JSONObject(weatherData);
            switch (time){
                case 0:
                    updateCurrentWeather(response);
                    break;
                case 1:
                    updateForecastWeather(response);
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    break;
                default:
                    Log.d("retrieveLastResponse","Wrong time");
                    break;
            }
        }catch (Exception ex){
            Log.d("Load Res",ex.getMessage());
        }
    }

    public void getFavCoord(String name,int i){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                +name
                +"&appid="
                +apiKey
                +"&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(weather_page.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textLastTime.setText(getCurrentTime());
                    double lon = response.getJSONObject("coord").getDouble("lon");
                    double lat = response.getJSONObject("coord").getDouble("lat");
                    getFavWeather(lat, lon, i);
                } catch (Exception ex) {
                    Log.d("getFavCoord","Favorite City Fetch Failed");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fetch Error",error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getFavWeather(double lat, double lon, int i) {
        RequestQueue requestQueue = Volley.newRequestQueue(weather_page.this);

        String urlCurrent = "https://api.openweathermap.org/data/2.5/weather?lat="
                + lat
                + "&lon="
                + lon
                + "&appid="
                + apiKey
                + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCurrent, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                saveLastResponse(response,i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fetch Error",error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


}