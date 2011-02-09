package com.trackerapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ApiProperty {
    public final static String API_PROPERTY_FILE = "api.properties";
    private Properties mProperties;
    private static ApiProperty sSelf = null;

    private ApiProperty(FileInputStream file) {
        mProperties = new Properties();

        try {
            mProperties.load(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static ApiProperty getInstance() {
        if (sSelf == null) {
            try {
                sSelf = new ApiProperty(new FileInputStream(API_PROPERTY_FILE));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return sSelf;
    }

    public Object getValue(String key) {
        return mProperties.get(key);
    }
}
