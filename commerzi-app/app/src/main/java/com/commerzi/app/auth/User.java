package com.commerzi.app.auth;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String email;
    private String password;
    @Deprecated
    private static String apiKey;

    public User(int userId) {
        this.setUserId(userId);
    }

    public User(String firstName, String lastName, String address, String city, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.email = email;
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Deprecated
    public static String getApiKey() {
        return apiKey;
    }

    @Deprecated
    public static void setApiKey(String newApiKey) {
        apiKey = newApiKey;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static User fromJSONObject(JSONObject in) throws JSONException {
        User user = new User(in.getInt("userId"));

        user.setFirstName(in.getString("firstName"));
        user.setLastName(in.getString("lastName"));
        user.setEmail(in.getString("email"));
        user.setAddress(in.getString("address"));
        user.setCity(in.getString("city"));
        user.setPassword(in.getString("password"));

        return user;
    }
}
