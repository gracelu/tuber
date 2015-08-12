package com.example.natalie.tuber;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding);

        final EditText origin = (EditText) findViewById(R.id.EditText01);
        final EditText destination = (EditText) findViewById(R.id.EditText02);
        final double[] originLocation = new double[2];
        final double[] destinationLocation = new double[2];

        final Button[] addressButton = {(Button) findViewById(R.id.calculateButton)};
        addressButton[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GeocodingLocation locationAddress = new GeocodingLocation();
                originLocation[0] = locationAddress.getAddressFromLocation(origin.getText().toString(), getApplicationContext())[0];
                originLocation[1] = locationAddress.getAddressFromLocation(origin.getText().toString(), getApplicationContext())[1];

                destinationLocation[0] = locationAddress.getAddressFromLocation(destination.getText().toString(), getApplicationContext())[0];
                destinationLocation[1] = locationAddress.getAddressFromLocation(destination.getText().toString(), getApplicationContext())[1];

                String uberOrigLat = Double.toString(originLocation[0]);
                String uberOrigLong = Double.toString(originLocation[1]);
                String uberDestLat = Double.toString(destinationLocation[0]);
                String uberDestLong = Double.toString(destinationLocation[1]);

                String taxiOrig = uberOrigLat + "," + uberOrigLong;
                String taxiDest = uberDestLat + "," + uberDestLong;

                Log.v("Lat", uberOrigLat);
                Log.v("Long", uberOrigLong);
                Log.v("DestLat", uberDestLat);
                Log.v("DestLong", uberDestLong);

                Log.v("taxiOrig", taxiOrig);
                Log.v("taxiDest", taxiDest);

                ArrayAdapter<String> mQueryAdapterTaxi =
                        new ArrayAdapter<String>(
                                MainActivity.this, // The current context (this activity)
                                R.layout.taxi_list_item, // The name of the layout ID.
                                R.id.list_item_taxi_textview, // The ID of the textview to populate.
                                new ArrayList<String>());


                String api_key = getString(R.string.taxifarefinder_api_key);
                FetchTaxiPriceTask taxiTask = new FetchTaxiPriceTask(MainActivity.this, api_key, mQueryAdapterTaxi);
                AsyncTask<String, Void, String[]> taxresults = taxiTask.execute(taxiOrig, taxiDest);
                Log.v("TaxiFare", "0" );


                setContentView(R.layout.fragment_query);



            }
        });
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
}

//    public void getResultsButton2() {
//        Button button = (Button) findViewById(R.id.button);
//
//        final String api_key = getString(R.string.taxifarefinder_api_key);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Log.v("origin", origin.getText().toString());
//                Log.v("destination", destination.getText().toString());
//                FetchTaxiPriceTask taxiPriceTask = new FetchTaxiPriceTask(api_key);
//                //"42.368025,-71.022155","42.362571,-71.055543"
//                taxiPriceTask.execute(origin.getText().toString(), destination.getText().toString());
//            }
//        });
//    }
    /*
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
*/