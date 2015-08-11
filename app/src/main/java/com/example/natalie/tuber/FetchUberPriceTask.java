package com.example.natalie.tuber;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gracelu on 8/11/15
 */

public class FetchUberPriceTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchTaxiPriceTask.class.getSimpleName();
    //private String api_key;

    public FetchUberPriceTask() {

    }

    private String[] getWeatherDataFromJson(String taxiFareJsonStr, int numEntities)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String UBER_LOW_ESTIMATE = "low_estimate";
        final String UBER_HIGH_ESTIMATE = "high_estimate";

        final String[] Entities = {UBER_LOW_ESTIMATE, UBER_HIGH_ESTIMATE};

        String[] resultStrs = new String[numEntities];

        JSONObject taxiFareJson = new JSONObject(taxiFareJsonStr);
        //String status = taxiFareJson.getString(TAXIFARE_STATUS);

        //Todo: remember to add conditions for status that are not OK


            for (int i = 0; i < numEntities; i++) {
                resultStrs[i] = taxiFareJson.getString(Entities[i]);
            }


        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Uber: " + s);
        }
        return resultStrs;
    }

    @Override
    protected void onPostExecute (String[] result){
        Log.v(LOG_TAG, "Uber total fare is: "+result[0]);
    }

    @Override
    protected String[] doInBackground(String... params) {

        // If there's no origin and destination location, no fare to look up
        if (params.length < 2) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String taxiFareJsonStr = null;

        try {
            // Construct the URL for the TaxiFareFinder query
            // Possible parameters are available at its API page, at
            // http://www.taxifarefinder.com/api.php#entities
            /*final String TAXI_BASE_URL =
                    "http://api.taxifarefinder.com/fare?";
            final String API_KEY = "key";
            final String ORIGIN = "origin";
            final String DESTINATION = "destination";
            //Todo: try to use ENTITY_HANDLE as well
            final String ENTITY_HANDLE = "entity_handle";

            Uri builtUri = Uri.parse(TAXI_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .appendQueryParameter(ORIGIN, params[0])
                    .appendQueryParameter(DESTINATION, params[1])
                    .build();*/

            String pass_url = "https://api.uber.com/v1/estimates/price?server_token=28t86KafW8mM1WjOVyQJ2_rLmVWEU71LVeC5ucjW&start_latitude=" + params[0] + "&start_longitude=" + params[1] + "&end_latitude=" + params[2] + "&end_longitude=" + params[3];

            Log.v("passurl: ", pass_url);

            URL url = new URL(pass_url);

            //Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            taxiFareJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Fare finder string: " + taxiFareJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        //Todo: find a way to better specify how many entities we want to get from the Json array returned
        // for now, we just get the total amount

        try {
            return getWeatherDataFromJson(taxiFareJsonStr, 1);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
}

