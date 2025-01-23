package com.commerzi.commerziapi.model;

/**
 * Model class representing a visit in the Commerzi application.
 */
public class Visit {
    private String type; // "client" or "prospect"
    private String id;
    private String status; // "visited" or "not-visited"

    /**
     * Gets the type of the visit.
     *
     * @return the type of the visit
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the visit.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the ID of the visit.
     *
     * @return the ID of the visit
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the visit.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the status of the visit.
     *
     * @return the status of the visit
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the visit.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}