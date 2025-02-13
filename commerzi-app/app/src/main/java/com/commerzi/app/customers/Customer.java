package com.commerzi.app.customers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Customer implements Parcelable {

    private String id;
    private String name;
    private String address;
    private String city;
    private String description;
    private Coordinates gpsCoordinates;
    private Contact contact;
    private ECustomerType type;
    private String userId;

    public Customer(String id, String name, String address, String city, String description, ECustomerType type, Contact contact, Coordinates gpsCoordinates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.description = description;
        this.type = type;
        this.contact = contact;
        this.gpsCoordinates = gpsCoordinates;
    }

    public String getId() {
        return id;
    }

    public ECustomerType getType() {
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

    public Contact getContact() {
        return contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(description);
        dest.writeSerializable(type);
        dest.writeParcelable(contact, flags);
        dest.writeParcelable(gpsCoordinates, flags);
    }

     public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            String id = in.readString();
            String name = in.readString();
            String address = in.readString();
            String city = in.readString();
            String description = in.readString();
            ECustomerType type = (ECustomerType) in.readSerializable();  // L'énumération est lue avec readSerializable()
            Contact contact = in.readParcelable(Contact.class.getClassLoader());
            Coordinates gpsCoordinates = in.readParcelable(Coordinates.class.getClassLoader());
            return new Customer(id, name, address, city, description, type, contact, gpsCoordinates);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}

