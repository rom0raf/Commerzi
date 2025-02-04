package com.commerzi.commerziapi.model;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Model class representing a customer in the Commerzi application.
 */
@Document(collection = "customers")
public class Customer {

    @MongoId
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String address;
    private String city;
    private String description;
    private JOpenCageLatLng gpsCoordinates;
    private Contact contact;
    private ECustomerType type;
    private String userId;

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
     * Gets the city of the customer.
     * @return the city of the customer
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the customer.
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
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
    public JOpenCageLatLng getGpsCoordinates() {
        return gpsCoordinates;
    }

    /**
     * Sets the GPS coordinates of the customer.
     *
     * @param gpsCoordinates the GPS coordinates to set
     */
    public void setGpsCoordinates(JOpenCageLatLng gpsCoordinates) {
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
    public ECustomerType getType() {
        return type;
    }

    /**
     * Sets the type of the customer (client or prospect).
     *
     * @param type the type to set
     */
    public void setType(ECustomerType type) {
        this.type = type;
    }

    /**
     * Gets the user ID associated with the customer.
     *
     * @return the user ID associated with the customer
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the customer.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void merge(Customer second) {
        setName(second.getName());
        setAddress(second.getAddress());
        setCity(second.getCity());
        setDescription(second.getDescription());
        setGpsCoordinates(second.getGpsCoordinates());
        setContact(second.getContact());
        setType(second.getType());
        setUserId(second.getUserId());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", gpsCoordinates=" + gpsCoordinates +
                ", contact=" + contact +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                '}';
    }
}