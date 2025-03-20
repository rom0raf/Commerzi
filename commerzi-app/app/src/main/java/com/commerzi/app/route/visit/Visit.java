package com.commerzi.app.route.visit;

import android.os.Parcel;
import android.os.Parcelable;

import com.commerzi.app.customers.Customer;

/**
 * Model class representing a visit in the Commerzi application.
 */
public class Visit implements Parcelable {
    private String id;
    private Customer customer; // "client" or "prospect"
    private EVisitStatus status;
    private static final int idCounter = 0;

    public Visit(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        this.id = "visit_" + idCounter;
        this.customer = customer;
        this.status = EVisitStatus.NOT_VISITED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public EVisitStatus getStatus() {
        return status;
    }

    public void setStatus(EVisitStatus status) {
        this.status = status;
    }

    protected Visit(Parcel in) {
        id = in.readString();
        customer = in.readParcelable(Customer.class.getClassLoader());
        status = EVisitStatus.valueOf(in.readString());
    }

    public static final Creator<Visit> CREATOR = new Creator<Visit>() {
        @Override
        public Visit createFromParcel(Parcel in) {
            return new Visit(in);
        }

        @Override
        public Visit[] newArray(int size) {
            return new Visit[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(customer, flags);
        dest.writeString(status.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters...

    @Override
    public String toString() {
        return "Visit{" +
                "id='" + id + '\'' +
                ", type=" + customer +
                ", status=" + status +
                '}';
    }
}