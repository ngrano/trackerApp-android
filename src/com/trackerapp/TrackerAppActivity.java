package com.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.trackerapp.rest.JsonClient;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrackerAppActivity extends Activity {
    private LocationManager mLocationManager = null;
    private LocationSender mLocationSender = null;
    private String mLocationProvider = null;
    private Properties mApiProperties = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readApiProperties();

        mLocationManager =
            (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mLocationSender = new LocationSender(mApiProperties);
        mLocationSender.setLocationManager(mLocationManager);
        mLocationProvider = LocationManager.GPS_PROVIDER;

        createLocationDatabase();
        setContentView(R.layout.main);
        initializeWidgets();
        authenticate();
    }

    private void readApiProperties() {
        Resources res = this.getResources();
        AssetManager assetManager = res.getAssets();

        try {
            InputStream in = assetManager.open("api.properties");
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
        // Send location once button
        final Button mSendLocationOnce =
                (Button) findViewById(R.id.send_location_once);

        mSendLocationOnce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mLocationManager.requestLocationUpdates(
                        mLocationProvider, 0, 0, mLocationSender);
            }
        });
    }

    private void createLocationDatabase() {
        // TODO Auto-generated method stub
        
    }
}