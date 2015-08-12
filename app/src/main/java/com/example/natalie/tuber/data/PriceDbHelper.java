package com.example.natalie.tuber.data;

/**
 * Created by jinglingli on 8/11/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.natalie.tuber.data.PriceContract.PriceEntry;
/**
 * Manages a local database for weather data.
 */
public class PriceDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "price.db";

    public PriceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PriceEntry.TABLE_NAME);
        final String SQL_CREATE_PRICE_TABLE = "CREATE TABLE " + PriceEntry.TABLE_NAME + " (" +
                PriceEntry._ID + " INTEGER PRIMARY KEY," +
                PriceEntry.COLUMN_SERVICE_NAME + " TEXT NOT NULL, " +
                PriceEntry.COLUMN_PRICE + " TEXT NOT NULL, " +
                PriceEntry.COLUMN_LOW_PRICE + " DOUBLE NOT NULL, " +
                PriceEntry.COLUMN_IDENTITY + " INTEGER NOT NULL, " +
                PriceEntry.COLUMN_EXTRA + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PRICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PriceEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
