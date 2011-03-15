package com.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LocationShareActivity extends Activity {
    public static final String TAG = LocationShareActivity.class.getSimpleName();
    public static final String API_PROPERTIES_FILE = "api.properties";

    private LocationManager mLocationManager = null;
    private LocationSender mLocationSender = null;
    private String mLocationProvider = null;
    private Properties mApiProperties = null;

    private Button mShareLocationOnceButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationshare);
        readApiProperties();

        mLocationManager =
            (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mLocationProvider = LocationManager.GPS_PROVIDER;

        mLocationSender = new LocationSender(mApiProperties);
        mLocationSender.setLocationManager(mLocationManager);
        mLocationSender.setConnectivityManager(
                (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE));
        mLocationSender.setLocationsOpenHelper(new LocationsOpenHelper(this));

        initializeWidgets();
        authenticate();
    }

    private void readApiProperties() {
        Resources res = this.getResources();
        AssetManager assetManager = res.getAssets();

        try {
            InputStream in = assetManager.open(API_PROPERTIES_FILE);
            mApiProperties = new Properties();
            mApiProperties.load(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void authenticate() {
        SharedPreferences settings =
                getSharedPreferences(SettingsActivity.PREFS_NAME, 0);

        String apiKey = settings.getString("apikey", null);
        mLocationSender.setApiKey(apiKey);

        if (apiKey != null) {
            Log.i(TAG, "authenticate: Successfully authenticated");
        } else {
            Log.i(TAG, "authenticate: Could not authenticate");

            if (mApiProperties == null) {
                return;
            }

            String env = (String) mApiProperties.getProperty("environment", "");
            if (env.equalsIgnoreCase("development")) {
                Log.i(TAG, "authenticate: Trying to authenticate with dev api key");
                apiKey = (String) mApiProperties.getProperty("apikey", "");

                if (apiKey.length() == 0) {
                    Log.i(TAG, "authenticate: Could not authenticate with a dev key");
                    return;
                }

                mLocationSender.setApiKey(apiKey);
                Log.i(TAG, "authenticate: Successfully authenticated with dev key");
            }
        }
    }

    private void initializeWidgets() {
        mShareLocationOnceButton = (Button) findViewById(R.id.send_location_once);
        mShareLocationOnceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLocationManager.requestLocationUpdates(
                        mLocationProvider, 0, 0, mLocationSender);
            }
        });
    }
}
