package com.example.weather;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView tempTextView;
    TextView dateTextView;
    TextView weatherDescTextView;
    TextView cityTextView;
    ImageView weatherImageView;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        tempTextView = findViewById(R.id.tempTextView);
        dateTextView = findViewById(R.id.dateTextView);
        weatherDescTextView = findViewById(R.id.weatherDesctextView);
        cityTextView = findViewById(R.id.cityTextView);

        weatherImageView = findViewById(R.id.weatherImageView);
        weatherImageView.setImageResource(R.drawable.icon_clearsky);
        dateTextView.setText(getCurrentDate());


        getForecast();

//        mSwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");
//// This method performs the actual data-refresh operation.
//// The method calls setRefreshing(false) when it's finished.
//
//                    }
//                }
//        );


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//handling swipe refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        getForecast();
                    }
                }, 2000);
            }
        });


    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }


    private void getForecast() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Dhaka&appid=0ce6d0ef84ed1f6d12b14268aff0e2c3&units=metric";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseObject) {
                        //tempTextView.setText("Response: " + response.toString());
                        Log.v("WEATHER", "Response: " + responseObject.toString());

                        try {
                            JSONObject mainJSONObject = responseObject.getJSONObject("main");
                            JSONArray weatherArray = responseObject.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);


                            String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String city = responseObject.getString("name");
                            String icon = firstWeatherObject.getString("icon");


                            tempTextView.setText(temp);
                            weatherDescTextView.setText(weatherDescription);
                            cityTextView.setText(city);

                            int iconResourceId = getResources().getIdentifier("icon_" + icon, "drawable", getPackageName());

                            weatherImageView.setImageResource(iconResourceId);
                            Log.v("Icon", " " + iconResourceId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);

    }

}
