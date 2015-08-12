package com.example.natalie.tuber;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class QueryFragment extends Fragment {
    private final String LOG_TAG = QueryFragment.class.getSimpleName();
    private ArrayAdapter<String> mQueryAdapterUber;
    private ArrayAdapter<String> mQueryAdapterTaxi;
    String api_key;

    public QueryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mQueryAdapterTaxi =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.taxi_list_item, // The name of the layout ID.
                        R.id.list_item_taxi_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_query, container, false);
        //ListView listView = (ListView) rootView.findViewById(R.id.fragment_taxi);
        //listView.setAdapter(mQueryAdapterTaxi);

        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void updatePrice() {
        api_key = getActivity().getString(R.string.taxifarefinder_api_key);
        FetchTaxiPriceTask taxiTask = new FetchTaxiPriceTask(getActivity(), api_key, mQueryAdapterTaxi);
        taxiTask.execute("42.368025,-71.022155","42.362571,-71.055543");
        Log.v(LOG_TAG, "task running");
    }

    @Override
    public void onStart() {
        super.onStart();
        updatePrice();
    }
}
