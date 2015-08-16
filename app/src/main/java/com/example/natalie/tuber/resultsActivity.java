package com.example.natalie.tuber;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class resultsActivity extends ActionBarActivity {
    private final String LOG_TAG = resultsActivity.class.getSimpleName();
    QueryFragment qFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        if (savedInstanceState == null) {
            String[] Lat_long = getIntent().getStringArrayExtra("LATLONG_DATA");
            Bundle arguments = new Bundle();
            arguments.putStringArray("LATLONG_DATA", Lat_long);
            qFragment = new QueryFragment();
            qFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, qFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
