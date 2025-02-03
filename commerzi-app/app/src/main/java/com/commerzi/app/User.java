package com.commerzi.app;

public class User {

    private int userId;
    private static String apiKey;

    public User(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String newApiKey) {
        apiKey = newApiKey;
    }
}
