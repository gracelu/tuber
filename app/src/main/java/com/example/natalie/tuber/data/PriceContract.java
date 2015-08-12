package com.example.natalie.tuber.data;

/**
 * Created by jinglingli on 8/11/15.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the price database.
 */
public class PriceContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.natalie.tuber";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_PRICE = "price";


    /* Inner class that defines the table contents of the location table */
    public static final class PriceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRICE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRICE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRICE;

        // Table name
        public static final String TABLE_NAME = "price";

        // The service that provides at such price
        public static final String COLUMN_SERVICE_NAME = "service_name";

        // The price of the service to display
        public static final String COLUMN_PRICE = "price";

        // The lower bound price of the service for sorting
        public static final String COLUMN_LOW_PRICE = "low_price";

        // The identity of the service, 0 means Uber and 1 means Taxi
        public static final String COLUMN_IDENTITY = "identity";

        // Extra information, separated by comma
        // For services that are Uber, extra contains duration, distance, currency_code
        // For services that are Tax, extra contains initial_fare, metered_fare, tip_amount
        public static final String COLUMN_EXTRA = "extra_information";

        public static Uri buildPriceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
