package com.trackerapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.trackerapp.rest.JsonClient;

public class LocationSender implements LocationListener {
    private String mApiKey = null;

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
                TrackerAppActivity.getLocationApiUri() + ".json", request);

        if (response != null) {
            Log.e("locationsender.send", response.toString());
        } else {
            Log.e("locationsender.send", "null");
        }
    }

    public void onLocationChanged(Location location) {
        send(location);
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
