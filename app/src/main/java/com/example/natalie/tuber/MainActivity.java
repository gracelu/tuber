package com.example.natalie.tuber;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
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

        final Button[] addressButton = {(Button) findViewById(R.id.calculateButton)};
        addressButton[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GeocodingLocation locationAddress = new GeocodingLocation();

                double[] originLocation = locationAddress.getAddressFromLocation(origin.getText().toString(), getApplicationContext());
                double[] destinationLocation = locationAddress.getAddressFromLocation(destination.getText().toString(), getApplicationContext());

                if (originLocation == null) {
                    origin.setText("Invalid location!");
                }
                if (destinationLocation == null) {
                    destination.setText("Invalid location!");
                }
                if (originLocation == null || destinationLocation == null)  {
                    // We can't calculate fares for invalid locations.
                    return;
                }
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



/*                String api_key = getString(R.string.taxifarefinder_api_key);
                FetchTaxiPriceTask taxiTask = new FetchTaxiPriceTask(MainActivity.this, api_key);
                AsyncTask<String, Void, String[]> taxresults = taxiTask.execute(taxiOrig, taxiDest);
                Log.v("TaxiFare", "0" );

                FetchUberPriceTask uberTask = new FetchUberPriceTask(MainActivity.this);
                uberTask.execute(uberOrigLat,uberOrigLong,uberDestLat,uberDestLong);*/


                Intent intent = new Intent(getApplicationContext(), resultsActivity.class);
                intent.putExtra("LATLONG_DATA", new String[]{uberOrigLat,uberOrigLong,uberDestLat,uberDestLong});
                startActivity(intent);


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