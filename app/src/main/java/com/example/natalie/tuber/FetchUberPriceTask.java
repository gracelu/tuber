package com.example.natalie.tuber;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.natalie.tuber.data.PriceContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gracelu on 8/11/15
 */

public class FetchUberPriceTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchUberPriceTask.class.getSimpleName();
    //private String api_key;
    private final Context mContext;
    private final int serviceId = 1;

    public FetchUberPriceTask(Context context) {
        mContext = context;
    }

    private String[] getWeatherDataFromJson(String taxiFareJsonStr, int numEntities)
            throws JSONException {
        Log.v(LOG_TAG, "Uber total fare is: " + "getWeatherDataFromJson");
        if (taxiFareJsonStr == null) {
            Log.v("UberPrice", "Uber not available");
            return null;
        }
        // These are the names of the JSON objects that need to be extracted.

        //String serviceName, String price, double lowPrice, int identity, String extras
        final String UBER_TYPE = "localized_display_name"; //serviceName
        final String UBER_LOW_ESTIMATE = "low_estimate"; //lowPrice
        final String UBER_HIGH_ESTIMATE = "high_estimate";
        final String UBER_ESTIMATE = "estimate"; //price

        final String[] Entities = {UBER_TYPE, UBER_LOW_ESTIMATE, UBER_HIGH_ESTIMATE, UBER_ESTIMATE};

        String[] resultStrs = new String[numEntities];

        JSONObject taxiFareJson = new JSONObject(taxiFareJsonStr);
        //String status = taxiFareJson.getString(TAXIFARE_STATUS);

        //Todo: remember to add conditions for status that are not OK


        for (int i = 0; i < numEntities; i++) {
            resultStrs[i] = taxiFareJson.getString(Entities[i]);
            Log.v(LOG_TAG, resultStrs[i]);
        }

        addPrice(resultStrs[0], resultStrs[3], Double.parseDouble(resultStrs[1]), serviceId, resultStrs[2]);
        return resultStrs;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null)
            Log.v(LOG_TAG, "Uber total fare is: " + result[0]);
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

        String uber1Str;

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

            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
            } catch (FileNotFoundException e) {
                return null;
            }

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


            JSONObject uberJson = new JSONObject(taxiFareJsonStr);
            Log.v(LOG_TAG, "Fare finder string: " + uberJson.getJSONArray("prices").getJSONObject(0));
            try {
                uber1Str = uberJson.getJSONArray("prices").getJSONObject(0).toString();
            } finally {
                return null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
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
            JSONObject uberJson = new JSONObject(taxiFareJsonStr);
             Log.v(LOG_TAG, "Going "+uberJson);
            if (uberJson != null)
                return getWeatherDataFromJson(String.valueOf(uberJson.getJSONArray("prices").getJSONObject(0)), 4);
            else
                return null;

            //return weatherArray.getJSONObject(0).getInt("low_estimate");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    long addPrice(String serviceName, String price, double lowPrice, int identity, String extras) {
        long priceId;
        ContentValues priceValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        priceValues.put(PriceContract.PriceEntry.COLUMN_SERVICE_NAME, serviceName);
        priceValues.put(PriceContract.PriceEntry.COLUMN_PRICE, price);
        priceValues.put(PriceContract.PriceEntry.COLUMN_LOW_PRICE, lowPrice);
        priceValues.put(PriceContract.PriceEntry.COLUMN_IDENTITY, identity);
        priceValues.put(PriceContract.PriceEntry.COLUMN_EXTRA, extras);

        // Finally, insert location data into the database.
        Uri insertedUri = mContext.getContentResolver().insert(
                PriceContract.PriceEntry.CONTENT_URI,
                priceValues
        );

        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
        priceId = ContentUris.parseId(insertedUri);
        Log.v(LOG_TAG, priceId + "");
        return priceId;
    }

}

