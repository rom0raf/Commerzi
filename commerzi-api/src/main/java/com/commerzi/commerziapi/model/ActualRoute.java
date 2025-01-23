package com.commerzi.commerziapi.model;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Model class representing an itinerary in the Commerzi application.
 */
@Document(collection = "journeys")
public class ActualRoute {

    @Id
    private String id;
    private String date;
    private String userId;
    private String plannedRouteId;
    private List<JOpenCageLatLng> path;
    private List<Visit> visits;

    /**
     * Gets the ID of the journey.
     *
     * @return the ID of the journey
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the journey.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the date of the journey.
     *
     * @return the date of the journey
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the journey.
     *
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the user ID associated with the journey.
     *
     * @return the user ID associated with the journey
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the journey.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the route ID associated with the journey.
     *
     * @return the route ID associated with the journey
     */
    public String getRouteId() {
        return plannedRouteId;
    }

    /**
     * Sets the route ID associated with the journey.
     *
     * @param routeId the route ID to set
     */
    public void setRouteId(String routeId) {
        this.plannedRouteId = routeId;
    }

    /**
     * Gets the path of the journey as a list of GPS coordinates.
     *
     * @return the path of the journey
     */
    public List<JOpenCageLatLng> getPath() {
        return path;
    }

    /**
     * Sets the path of the journey as a list of GPS coordinates.
     *
     * @param path the path to set
     */
    public void setPath(List<JOpenCageLatLng> path) {
        this.path = path;
    }

    /**
     * Gets the visits associated with the journey.
     *
     * @return the visits associated with the journey
     */
    public List<Visit> getVisits() {
        return visits;
    }

    /**
     * Sets the visits associated with the journey.
     *
     * @param visits the visits to set
     */
    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}