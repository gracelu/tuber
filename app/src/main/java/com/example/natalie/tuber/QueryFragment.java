package com.example.natalie.tuber;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.natalie.tuber.data.PriceContract;


public class QueryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = QueryFragment.class.getSimpleName();
    private static final int PRICE_LOADER = 0;
    PriceAdapter mQueryAdapter;
    String api_key;
    String[] Long_lat;

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_PRICE_ID = 0;
    static final int COL_PRICE_SERVICENAME = 1;
    static final int COL_PRICE_PRICE = 2;
    static final int COL_PRICE_LOWPRICE = 3;
    static final int COL_PRICE_IDENTITY = 4;
    static final int COL_PRICE_EXTRA = 5;

    public QueryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Long_lat = arguments.getStringArray("LATLONG_DATA");
        }
        // Inflate the layout for this fragment
        mQueryAdapter =  new PriceAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_query, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_uber);
        listView.setAdapter(mQueryAdapter);
        api_key = getActivity().getString(R.string.taxifarefinder_api_key);
        Log.v(LOG_TAG, api_key);
        getActivity().getContentResolver().delete(PriceContract.PriceEntry.CONTENT_URI,
                null, null);
        updatePrice();
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
        FetchTaxiPriceTask taxiTask = new FetchTaxiPriceTask(getActivity(), api_key);
        FetchUberPriceTask uberTask = new FetchUberPriceTask(getActivity());
        uberTask.execute(Long_lat[0],Long_lat[1],Long_lat[2],Long_lat[3]);
        taxiTask.execute(Long_lat[0]+","+Long_lat[1], Long_lat[2]+","+Long_lat[3]);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {


        // Sort order:  Ascending, by date.
        String sortOrder = PriceContract.PriceEntry.COLUMN_LOW_PRICE + " ASC";

        CursorLoader cursorl = new CursorLoader(getActivity(),
                PriceContract.PriceEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);
        return cursorl ;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mQueryAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQueryAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PRICE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
}
