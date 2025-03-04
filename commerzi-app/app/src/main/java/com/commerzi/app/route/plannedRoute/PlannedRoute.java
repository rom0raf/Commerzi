package com.commerzi.app.route.plannedRoute;

import com.commerzi.app.customers.Coordinates;
import com.commerzi.app.customers.Customer;

import java.util.List;

/**
 * Model class representing a route in the Commerzi application.
 */
public class PlannedRoute {

    private String id;
    private String userId;
    private String name;
    private List<Customer> customersAndProspects;
    private Coordinates startingPoint;
    private Coordinates endingPoint;
    private double totalDistance;
    
    public PlannedRoute(String name, List<Customer> customersAndProspects){
        this.name = name;
        this.customersAndProspects = customersAndProspects;
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
        this.customersAndProspects = customersAndProspects;
    }

    /**
     * Gets the starting point of the route.
     *
     * @return the starting point of the route
     */
    public Coordinates getStartingPoint() {
        return startingPoint;
    }

    /**
     * Sets the starting point of the route.
     *
     * @param startingPoint the starting point to set
     */
    public void setStartingPoint(Coordinates startingPoint) {
        this.startingPoint = startingPoint;
    }

    /**
     * Gets the ending point of the route.
     *
     * @return the ending point of the route
     */
    public Coordinates getEndingPoint() {
        return endingPoint;
    }

    /**
     * Sets the ending point of the route.
     *
     * @param endingPoint the ending point to set
     */
    public void setEndingPoint(Coordinates endingPoint) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PlannedRoute{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", customersAndProspects=" + customersAndProspects +
                ", startingPoint=" + startingPoint +
                ", endingPoint=" + endingPoint +
                ", totalDistance=" + totalDistance +
                '}';
    }
}