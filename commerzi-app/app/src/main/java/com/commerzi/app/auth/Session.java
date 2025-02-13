package com.commerzi.app.auth;

public class Session {

    private static User user;

    private static Session instance = null;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static User getUser() {
        return user;
    }

    public static String getApiKey() {
        return user.getApiKey();
    }

    public void setApiKey(String key) {
        user.setApiKey(key);
    }
}
