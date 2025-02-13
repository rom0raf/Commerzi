package com.commerzi.app.communication;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommunicatorProperties {
    private Properties properties;

    private final String PROPERTIES_FILE = "communicator.properties";
    public static final String BASE_KEY = "base";
    public static final String PORT = "port";
    public static final String AUTH_URL = "auth_url";
    public static final String USER_URL = "user_url";
    public static final String CUSTOMER_URL = "customer_url";
    public static final String PLANNED_ROUTE_URL = "planned_route_url";
    public static final String ACTUAL_ROUTE_URL = "actual_route_url";

    public CommunicatorProperties(Context context) throws IOException {
        this.properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(PROPERTIES_FILE);
        this.properties.load(inputStream);
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }
}
