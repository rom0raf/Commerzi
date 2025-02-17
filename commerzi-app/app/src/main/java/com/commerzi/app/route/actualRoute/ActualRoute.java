package com.commerzi.app.route.actualRoute;

import android.os.Parcel;
import android.os.Parcelable;

import com.commerzi.app.customers.Coordinates;
import com.commerzi.app.route.utils.ERouteStatus;
import com.commerzi.app.route.visit.Visit;

import java.util.List;

/**
 * Model class representing an itinerary in the Commerzi application.
 */
public class ActualRoute implements Parcelable {
    private String id;
    private String date;
    private String userId;
    private String plannedRouteId;
    private List<Visit> visits;
    private ERouteStatus status;
    private List<Coordinates> coordinates;

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

    /**
     * Gets the status of the journey.
     *
     * @return the status of the journey
     */
    public ERouteStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the journey.
     *
     * @param status the status to set
     */
    public void setStatus(ERouteStatus status) {
        this.status = status;
    }

    /**
     * Gets the current location of the journey.
     *
     * @return the current location of the journey
     */
    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the current location of the journey.
     *
     * @param coordinates the current location to set
     */
    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "ActualRoute{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", userId='" + userId + '\'' +
                ", plannedRouteId='" + plannedRouteId + '\'' +
                ", visits=" + visits +
                ", status=" + status +
                ", coordinates=" + coordinates +
                '}';
    }

    public ActualRoute(Parcel in) {
        id = in.readString();
        date = in.readString();
        userId = in.readString();
        plannedRouteId = in.readString();
        visits = in.readArrayList(Visit.class.getClassLoader());
        status = ERouteStatus.valueOf(in.readString());
        coordinates = in.createTypedArrayList(Coordinates.CREATOR);
    }

    public static final Creator<ActualRoute> CREATOR = new Creator<ActualRoute>() {
        @Override
        public ActualRoute createFromParcel(Parcel in) {
            return new ActualRoute(in);
        }

        @Override
        public ActualRoute[] newArray(int size) {
            return new ActualRoute[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(date);
        dest.writeString(userId);
        dest.writeString(plannedRouteId);
        dest.writeList(visits);
        dest.writeString(status.name());
        dest.writeTypedList(coordinates);
    }
}