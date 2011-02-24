package com.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationShareActivity extends Activity {
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
        // Load key from the database
        mLocationSender.setApiKey("f3ed4cf89fee80bd2918615cade79f4f232cf596");
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
