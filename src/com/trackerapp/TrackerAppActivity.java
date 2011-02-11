package com.trackerapp;

import com.trackerapp.rest.JsonClient;

import android.app.Activity;
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

    public static final String getLocationApiUri() {
        ApiProperty apiProp = ApiProperty.getInstance();

        if (apiProp != null) {
            String d = (String) apiProp.getValue("domain");
            String l = (String) apiProp.getValue("locations");
            return d + l;
        }

        return null;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationManager =
            (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mLocationSender = new LocationSender();
        mLocationProvider = LocationManager.GPS_PROVIDER;

        createLocationDatabase();
        setContentView(R.layout.main);
        initializeWidgets();
        authenticate();
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
                mLocationManager.removeUpdates(mLocationSender);
            }
        });
    }

    private void createLocationDatabase() {
        // TODO Auto-generated method stub
        
    }
}