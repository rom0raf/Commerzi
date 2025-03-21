package com.commerzi.app.customers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a customer with various details such as name, address, city, description, contact information, and GPS coordinates.
 */
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

    /**
     * Constructs a new Customer instance.
     *
     * @param id The unique identifier of the customer.
     * @param name The name of the customer.
     * @param address The address of the customer.
     * @param city The city of the customer.
     * @param description A description of the customer.
     * @param type The type of the customer (client or prospect).
     * @param contact The contact information of the customer.
     * @param gpsCoordinates The GPS coordinates of the customer's location.
     */
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

    /**
     * Gets the unique identifier of the customer.
     *
     * @return The unique identifier of the customer.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the type of the customer.
     *
     * @return The type of the customer.
     */
    public ECustomerType getType() {
        return type;
    }

    /**
     * Gets the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the address of the customer.
     *
     * @return The address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city of the customer.
     *
     * @return The city of the customer.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the description of the customer.
     *
     * @return The description of the customer.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the contact information of the customer.
     *
     * @return The contact information of the customer.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Gets the GPS coordinates of the customer's location.
     *
     * @return The GPS coordinates of the customer's location.
     */
    public Coordinates getGpsCoordinates() {
        return gpsCoordinates;
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

    /**
     * A Creator class to create instances of the Customer class from a Parcel.
     */
    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            String id = in.readString();
            String name = in.readString();
            String address = in.readString();
            String city = in.readString();
            String description = in.readString();
            ECustomerType type = (ECustomerType) in.readSerializable();
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