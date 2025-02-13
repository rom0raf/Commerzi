package com.commerzi.commerziapi.model;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a route in the Commerzi application.
 */
@Document(collection = "routes")
public class PlannedRoute {

    @Id
    private String id;
    private String name;
    private String userId;
    private List<Customer> customersAndProspects;
    private JOpenCageLatLng startingPoint;
    private JOpenCageLatLng endingPoint;
    private double totalDistance;

    public PlannedRoute() {
        this.customersAndProspects = new ArrayList<>();
    }

    /**
     * Gets the ID of the route.
     *
     * @return the ID of the route
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the route.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the name of the route.
     * @return the name of the route
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the route.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user ID associated with the route.
     *
     * @return the user ID associated with the route
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the route.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the list of customers and prospects associated with the route.
     *
     * @return the list of customers and prospects
     */
    public List<Customer> getCustomersAndProspects() {
        return customersAndProspects;
    }

    /**
     * Sets the list of customers and prospects associated with the route.
     *
     * @param customersAndProspects the list of customers and prospects to set
     */
    public void setCustomersAndProspects(List<Customer> customersAndProspects) {
        if (customersAndProspects == null || customersAndProspects.isEmpty()) {
            throw new IllegalArgumentException("The list of customers and prospects cannot be null or empty.");
        }

        if (customersAndProspects.size() < 2) {
            throw new IllegalArgumentException("The list of customers and prospects must contain at least two elements.");
        }

        if (customersAndProspects.size() > 8) {
            throw new IllegalArgumentException("The list of customers and prospects must contain at most eight elements.");
        }

        this.customersAndProspects = customersAndProspects;
    }

    /**
     * Gets the starting point of the route.
     *
     * @return the starting point of the route
     */
    public JOpenCageLatLng getStartingPoint() {
        return startingPoint;
    }

    /**
     * Sets the starting point of the route.
     *
     * @param startingPoint the starting point to set
     */
    public void setStartingPoint(JOpenCageLatLng startingPoint) {
        this.startingPoint = startingPoint;
    }

    /**
     * Gets the ending point of the route.
     *
     * @return the ending point of the route
     */
    public JOpenCageLatLng getEndingPoint() {
        return endingPoint;
    }

    /**
     * Sets the ending point of the route.
     *
     * @param endingPoint the ending point to set
     */
    public void setEndingPoint(JOpenCageLatLng endingPoint) {
        this.endingPoint = endingPoint;
    }

    /**
     * Gets the total distance of the route.
     *
     * @return the total distance of the route
     */
    public double getTotalDistance() {
        return totalDistance;
    }

    /**
     * Sets the total distance of the route.
     *
     * @param totalDistance the total distance to set
     */
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    @Override
    public String toString() {
        return "PlannedRoute{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", customersAndProspects=" + customersAndProspects +
                ", startingPoint=" + startingPoint +
                ", endingPoint=" + endingPoint +
                ", totalDistance=" + totalDistance +
                '}';
    }
}