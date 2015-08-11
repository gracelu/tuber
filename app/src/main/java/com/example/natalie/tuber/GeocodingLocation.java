package com.example.natalie.tuber;

/**
 * Created by Natalie on 8/8/15.
 */

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";

    public static double[] getAddressFromLocation(final String locationAddress,
                                                  final Context context) {
        final double[] result = new double[2];
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);

                        result[0] = address.getLatitude();
                        result[1] = address.getLongitude();
                    }

                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);

                }
            }
        };
        thread.start();
        return result;
    }
}