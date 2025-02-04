package com.commerzi.app;

public class User {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String email;
    private String password;
    private static String apiKey;

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

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String newApiKey) {
        apiKey = newApiKey;
    }
}
