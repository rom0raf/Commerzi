package com.commerzi.app;

import java.io.Serializable;

public class Client implements Serializable {

    private String id;
    private String name;
    private String address;
    private String city;
    private String type;
    private String description;
    private String contactFirstName;
    private String contactLastName;
    private String contactPhoneNumber;

    public Client(String id,String name, String address, String city, String description, String type, String contactFirstName, String contactLastName, String contactPhoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.description = description;
        this.type = type;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public String getContactInfo() {
        return contactFirstName + " " + contactLastName + " (" + contactPhoneNumber + ")";
    }
}

