package com.commerzi.app.customers;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Model class representing a contact in the Commerzi application.
 */
public class Contact implements Parcelable {
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Contact(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the first name of the contact.
     *
     * @return the first name of the contact
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the contact.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the contact.
     *
     * @return the last name of the contact
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the contact.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the phone number of the contact.
     *
     * @return the phone number of the contact
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the contact.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getCleanInfos() {
        return firstName + " " + lastName + " - " + phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
//        dest.writeParcelable(this, flags);
    }

     public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            String firstName = in.readString();
            String lastName = in.readString();
            String phoneNumber = in.readString();
            return new Contact(firstName, lastName, phoneNumber);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}