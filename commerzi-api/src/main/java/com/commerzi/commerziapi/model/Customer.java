package com.commerzi.commerziapi.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class representing a customer in the Commerzi application.
 */
@Document(collection = "customers")
public class Customer {

    public static final String CLIENT = "client";
    public static final String PROSPECT = "prospect";

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private String id;
    private String name;
    private String address;
    private String description;
    private String gpsCoordinates;
    private Contact contact;
    private String type;

    /**
     * Gets the ID of the customer.
     *
     * @return the ID of the customer
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the customer.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the customer.
     *
     * @return the address of the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the description of the customer.
     *
     * @return the description of the customer
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the customer.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the GPS coordinates of the customer.
     *
     * @return the GPS coordinates of the customer
     */
    public String getGpsCoordinates() {
        return gpsCoordinates;
    }

    /**
     * Sets the GPS coordinates of the customer.
     *
     * @param gpsCoordinates the GPS coordinates to set
     */
    public void setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    /**
     * Gets the contact information of the customer.
     *
     * @return the contact information of the customer
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the contact information of the customer.
     *
     * @param contact the contact information to set
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Gets the type of the customer (client or prospect).
     *
     * @return the type of the customer
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the customer (client or prospect).
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}