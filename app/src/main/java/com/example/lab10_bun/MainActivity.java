package com.example.lab10_bun;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Spinner spinnerForecastDays;
    private Button buttonSearch;
    private TextView textViewResult;
    private String cityKey;
    private static final String API_KEY = "9eElvvCSvUzRCniS0DsUAGHjGVnI4K4e"; // Your API key
    private static final String TAG = "WeatherApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        spinnerForecastDays = findViewById(R.id.spinnerForecastDays);
        buttonSearch = findViewById(R.id.buttonSearch);
        textViewResult = findViewById(R.id.textViewResult);

        // Set up Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.forecast_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForecastDays.setAdapter(adapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    Log.d(TAG, "Searching for city: " + city);
                    new CitySearchTask().execute(city);
                } else {
                    textViewResult.setText("Please enter a city name");
                }
            }
        });
    }

    private class CitySearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String url = "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + city;
            Log.d(TAG, "City Search URL: " + url);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    Log.d(TAG, "City Search Response: " + jsonData);
                    JSONArray jsonArray = new JSONArray(jsonData);
                    if (jsonArray.length() > 0) {
                        JSONObject cityObject = jsonArray.getJSONObject(0);
                        return cityObject.getString("Key");
                    } else {
                        Log.e(TAG, "No cities found in response");
                    }
                } else {
                    Log.e(TAG, "City Search failed with code: " + response.code());
                }
            } catch (Exception e) {
                Log.e(TAG, "City Search error: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                cityKey = result;
                textViewResult.setText("City Key: " + cityKey);
                Log.d(TAG, "City Key: " + cityKey);
                String selectedDays = spinnerForecastDays.getSelectedItem().toString();
                int days = selectedDays.equals("1 day") ? 1 : selectedDays.equals("5 days") ? 5 : 10;
                Log.d(TAG, "Fetching forecast for " + days + " days");
                new WeatherForecastTask().execute(cityKey, String.valueOf(days));
            } else {
                textViewResult.setText("City not found or error occurred");
            }
        }
    }

    private class WeatherForecastTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cityKey = params[0];
            int days = Integer.parseInt(params[1]);
            String url = "https://dataservice.accuweather.com/forecasts/v1/daily/" + days + "day/" + cityKey + "?apikey=" + API_KEY + "&metric=true";
            Log.d(TAG, "Weather Forecast URL: " + url);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    Log.d(TAG, "Weather Forecast Response: " + jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray dailyForecasts = jsonObject.getJSONArray("DailyForecasts");

                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < dailyForecasts.length(); i++) {
                        JSONObject forecast = dailyForecasts.getJSONObject(i);
                        String date = forecast.getString("Date").substring(0, 10);
                        JSONObject temperature = forecast.getJSONObject("Temperature");
                        JSONObject minTemp = temperature.getJSONObject("Minimum");
                        JSONObject maxTemp = temperature.getJSONObject("Maximum");

                        double min = minTemp.getDouble("Value") ;
                        double max = maxTemp.getDouble("Value");

                        result.append("Day ").append(i + 1).append(" (").append(date).append("): ")
                                .append("Min Temp: ").append(min).append("°C, Max Temp: ").append(max).append("°C\n");
                    }
                    return result.toString();
                } else {
                    Log.e(TAG, "Weather Forecast failed with code: " + response.code());
                }
            } catch (Exception e) {
                Log.e(TAG, "Weather Forecast error: " + e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                textViewResult.setText(result);
            } else {
                textViewResult.setText("Error fetching weather data");
            }
        }
    }
}