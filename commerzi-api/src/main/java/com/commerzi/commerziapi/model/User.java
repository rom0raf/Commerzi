package com.commerzi.commerziapi.model;

public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String session;

    public User() {}

    public User(String userId, String firstName, String lastName, String email, String password, String address) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getAddress() {
        return address;
    }

    public String getSession() { return session; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setAddress(String address) { this.address = address; }

    public void setSession(String session) { this.session = session; }

}
