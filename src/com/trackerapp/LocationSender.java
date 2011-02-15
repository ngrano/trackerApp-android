package com.trackerapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.trackerapp.rest.JsonClient;

public class LocationSender implements LocationListener {
    private LocationManager mLocationManager = null;
    private boolean mPeriodicalUpdate = false;
    private Properties mApiProperties = null;
    private String mApiKey = null;

    public LocationSender(Properties apiProperties) {
        mApiProperties = apiProperties;
    }

    public void setLocationManager(LocationManager manager) {
        mLocationManager = manager;
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

    public void send(Location loc) {
        if (mApiKey == null) {
            Log.e("LocationSender", "api key null, could not send");
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
