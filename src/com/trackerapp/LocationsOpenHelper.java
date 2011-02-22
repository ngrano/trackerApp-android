package com.trackerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trackerapp_db";
    private static final String LOCATIONS_TABLE_NAME = "trackerapp_locations";
    private static final String LOCATIONS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + LOCATIONS_TABLE_NAME + " (" +
            "apikey TEXT PRIMARY KEY, " +
            "longtitude TEXT, " +
            "latitude TEXT);";

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

    public boolean hasPendingLocations(String apiKey) {
        Cursor locations = getLocations(apiKey);
        return locations.getCount() == 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        
    }
}
