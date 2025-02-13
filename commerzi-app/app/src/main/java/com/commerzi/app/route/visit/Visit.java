package com.commerzi.app.route.visit;

import com.commerzi.app.customers.Customer;
import com.commerzi.app.route.visit.EVisitStatus;

/**
 * Model class representing a visit in the Commerzi application.
 */
public class Visit {
    private String id;
    private Customer type; // "client" or "prospect"
    private EVisitStatus status;

    private static final int idCounter = 0;

    public Visit(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        this.id = "visit_" + idCounter;
        this.type = customer;
        this.status = EVisitStatus.NOT_VISITED;
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
     * Gets the type of the visit.
     *
     * @return the type of the visit
     */
    public Customer getType() {
        return type;
    }

    /**
     * Sets the type of the visit.
     *
     * @param type the type to set
     */
    public void setType(Customer type) {
        this.type = type;
    }

    /**
     * Gets the status of the visit.
     *
     * @return the status of the visit
     */
    public EVisitStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the visit.
     *
     * @param status the status to set
     */
    public void setStatus(EVisitStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}