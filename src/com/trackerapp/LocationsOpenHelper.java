package com.trackerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class LocationsOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = LocationsOpenHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "trackerapp_db";
    private static final String LOCATIONS_TABLE_NAME = "trackerapp_locations";
    private static final String LOCATIONS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + LOCATIONS_TABLE_NAME + " (" +
            "apikey TEXT, " +
            "longtitude TEXT, " +
            "latitude TEXT, " +
            "created_at TEXT);";

    public LocationsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOCATIONS_TABLE_CREATE);
    }

    public Cursor getLocations(String apiKey) {
        String[] args = {apiKey};
        SQLiteDatabase dbRead = getReadableDatabase();

        return dbRead.rawQuery("SELECT * FROM " + LOCATIONS_TABLE_NAME +
                " WHERE apikey = ?", args);
    }

    public void insertLocation(String apiKey, Location location) {
        String[] args = {
                apiKey,
                Double.toString(location.getLongitude()),
                Double.toString(location.getLatitude())
        };

        SQLiteDatabase dbWrite = getWritableDatabase();
        String query = "INSERT INTO " + LOCATIONS_TABLE_NAME
                + " (?, ?, ?, datetime('now'))";

        Cursor record = dbWrite.rawQuery(query, args);

        if (record == null || record.getCount() == 0) {
            Log.d(TAG, "saveLocation: Could not save location");
        }
    }

    public boolean hasPendingLocations(String apiKey) {
        Cursor locations = getLocations(apiKey);
        return locations.getCount() == 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        String tableName =
                oldVersion == 1 ? "trackerapp_locations" : LOCATIONS_TABLE_NAME;

        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.execSQL(LOCATIONS_TABLE_CREATE);
    }
}
