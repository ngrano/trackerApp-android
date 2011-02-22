package com.trackerapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingsActivity extends Activity {
    public static final String PREFS_NAME = "trackerapp_settings";

	private SeekBar seekFreq;
	private TextView txtTest;
	private Button mSaveSettingsButton;
	private EditText mApiKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initializeWidgets();
        initalizeWidgetData();
    }

    private void initalizeWidgetData() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (txtTest != null) {
            txtTest.setText(settings.getString("frequency", "0"));
        }

        if (seekFreq != null) {
            int progress =
                    Integer.parseInt(settings.getString("frequency", "0"));
            seekFreq.setProgress(progress);
        }

        if (mApiKey != null) {
            mApiKey.setText(settings.getString("apikey", ""));
        }
    }

    private void initializeWidgets() {
        seekFreq = (SeekBar) findViewById(R.id.seekFreq);
        txtTest = (TextView) findViewById(R.id.txtTest);
        mApiKey = (EditText) findViewById(R.id.txtApiKey);
        mSaveSettingsButton = (Button) findViewById(R.id.btnSaveSettings);

        // Add listener to a frequency slider
        seekFreq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                txtTest.setText(Integer.toString(progress));
            }
        });

        // Add listener to a save button
        mSaveSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (seekFreq != null) {
            editor.putString(Integer.toString(seekFreq.getProgress()), "0");
        }

        if (mApiKey != null) {
            Editable text = mApiKey.getText();
            editor.putString(text.toString(), "");
        }

        editor.commit();
    }
}
