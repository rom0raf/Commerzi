package com.commerzi.commerziapi.properties;

import java.util.Properties;

public class SecretsPropertiesHandler {

    private static final String SECRETS_PROPERTIES_FILE = "secrets.properties";

    private static SecretsPropertiesHandler instance;

    private final Properties properties;

    private SecretsPropertiesHandler() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(SECRETS_PROPERTIES_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretsPropertiesHandler getInstance() {
        if (instance == null) {
            instance = new SecretsPropertiesHandler();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
