package com.commerzi.app;

public class Session {

    private User user;

    private static Session instance = null;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getApiKey() {
        return user.getApiKey();
    }

    public void setApiKey(String key) {
        user.setApiKey(key);
    }
}
