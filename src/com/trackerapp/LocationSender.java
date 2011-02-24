package com.trackerapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.util.Log;

import com.trackerapp.rest.JsonClient;

public class LocationSender implements LocationListener {
    public static final String TAG = LocationSender.class.getSimpleName();

    private LocationManager mLocationManager = null;
    private boolean mPeriodicalUpdate = false;
    private Properties mApiProperties = null;
    private LocationsOpenHelper mLocationsOpenHelper = null;
    private ConnectivityManager mConnectivityManager = null;
    private String mApiKey = null;

    public LocationSender(Properties apiProperties) {
        mApiProperties = apiProperties;
    }

    public void setLocationManager(LocationManager manager) {
        mLocationManager = manager;
    }

    public void setConnectivityManager(ConnectivityManager cm) {
        mConnectivityManager = cm;
    }

    public void setLocationsOpenHelper(LocationsOpenHelper loh) {
        mLocationsOpenHelper = loh;
    }

    public void setPeriodicalUpdate(boolean isPeriodical) {
        mPeriodicalUpdate = isPeriodical;
    }

    public boolean isPeriodicalUpdate() {
        return mPeriodicalUpdate;
    }

    public void setApiKey(String key) {
        mApiKey = key;
    }

    public void sendToWebService(Location loc) {
        if (mApiKey == null) {
            Log.e(TAG, "sendToWebService: api key null, could not send");
            return;
        }

        JSONObject request = new JSONObject();
        JSONObject location = new JSONObject();
        JsonClient client = new JsonClient();

        try {
            request.put("apikey", mApiKey);
            location.put("latitude", loc.getLatitude());
            location.put("longtitude", loc.getLongitude());
            request.put("location", location);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject response = client.doPost(
                getLocationApiUrl() + ".json", request);

        if (response != null) {
            Log.e("locationsender.send", response.toString());
        } else {
            Log.e("locationsender.send", "null");
        }
    }

    public void sendToDatabase(Location loc) {
        if (mApiKey == null) {
            Log.e("LocationSender", "api key null, could not send");
            return;
        }

        if (mLocationsOpenHelper == null) {
            Log.d(TAG, "sendToDatabase: LocationsOpenHelper is null");
            return;
        }

        mLocationsOpenHelper.insertLocation(mApiKey, loc);
    }

    public void send(Location loc) {
        NetworkInfo networkInfo =
                mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String env = (String) mApiProperties.get("environment");

        if (env == null) {
            env = "development";
        }

        if (env.equalsIgnoreCase("production")) {
            if (networkInfo.getState() == State.CONNECTED) {
                Log.d(TAG, "send: Connected to Wifi. Sending to web service...");
                sendToWebService(loc);
            } else {
                Log.d(TAG, "send: Not connected to Wifi. Saving the location to the database");
                sendToDatabase(loc);
            }
        } else if (env.equalsIgnoreCase("development")) {
            Log.d(TAG, "send (Development): Sending to web service...");
            sendToWebService(loc);
        }
    }

    private String getLocationApiUrl() {
        if (mApiProperties == null) {
            return "";
        }

        String domain = (String) mApiProperties.get("domain");
        String locations = (String) mApiProperties.get("locations");
        return domain + locations;
    }

    public void onLocationChanged(Location location) {
        send(location);

        if (!mPeriodicalUpdate) {
            mLocationManager.removeUpdates(this);
        }
    }

    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
        
    }

    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
        
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
        
    }
}
