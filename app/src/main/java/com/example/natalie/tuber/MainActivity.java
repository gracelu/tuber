package com.example.natalie.tuber;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getResultsButton2();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getResultsButton2() {
        Button button = (Button) findViewById(R.id.button);
        final EditText origin = (EditText) findViewById(R.id.editText);
        final EditText destination = (EditText) findViewById(R.id.editText2);
        final String api_key = getString(R.string.taxifarefinder_api_key);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("origin", origin.getText().toString());
                Log.v("destination", destination.getText().toString());
                FetchTaxiPriceTask taxiPriceTask = new FetchTaxiPriceTask(api_key);
                //"42.368025,-71.022155","42.362571,-71.055543"
                taxiPriceTask.execute(origin.getText().toString(), destination.getText().toString());
            }
        });
    }

    public void getResultsButton() {
        Button button = (Button) findViewById(R.id.button);

        final EditText start_address = (EditText) findViewById(R.id.editText);

        button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.v("EditText", start_address.getText().toString());
                        getUberCost(37.775818, -122.418028, 37.775838, -122.118028);
                    }
                });
    }

    public static double getUberCost(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String uberData = null;

        String pass_url = "https://api.uber.com/v1/estimates/price?server_token=28t86KafW8mM1WjOVyQJ2_rLmVWEU71LVeC5ucjW&start_latitude=" + Double.toString(startLatitude) + "&start_longitude=" + Double.toString(startLongitude) + "&end_latitude=" + Double.toString(endLatitude) + "&end_longitude=" + Double.toString(endLongitude);

        try {
            //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
            //URL url = new URL("https://api.uber.com/v1/estimates/price?server_token=28t86KafW8mM1WjOVyQJ2_rLmVWEU71LVeC5ucjW&start_latitude=37.775818&start_longitude=-122.418028&end_latitude=37.775838&end_longitude=-122.118028");
            URL url = new URL(pass_url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                uberData = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                uberData = null;
            }
            uberData = buffer.toString();
        } catch (IOException e) {
            //Log.e("PlaceholderFragment", "Error ", e);
            String msg = (e.getMessage() == null) ? "Login failed!" : e.getMessage();
            Log.e("Login Error1", msg);

            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            uberData = null;
        }

        Log.v("Uber Data", uberData);

        JSONObject forecastJson = null;
        try {
            forecastJson = new JSONObject(uberData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray weatherArray = null;
        try {
            weatherArray = forecastJson.getJSONArray("prices");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.v("low estimate: ", String.valueOf(weatherArray.getJSONObject(0).getInt("low_estimate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return weatherArray.getJSONObject(0).getInt("low_estimate");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
