package com.trackerapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.trackerapp.rest.JsonClient;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class TrackerAppActivity extends TabActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLocationDatabase();
        setContentView(R.layout.main);
        initializeWidgets();
    }

    private void initializeWidgets() {
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        // Share location tab
        intent = new Intent().setClass(this, LocationShareActivity.class);
        spec = tabHost.newTabSpec("locationshare").setIndicator("Share location");
        spec.setContent(R.id.shareLocation);
        tabHost.addTab(spec);

        // Settings tab
        intent = new Intent().setClass(this, SettingsActivity.class);
        spec = tabHost.newTabSpec("settings").setIndicator("Settings");
        spec.setContent(R.id.settings);
        tabHost.addTab(spec);
    }

    private void createLocationDatabase() {
        // TODO Auto-generated method stub
        
    }
}