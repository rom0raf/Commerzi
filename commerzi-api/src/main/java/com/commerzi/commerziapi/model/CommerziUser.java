package com.commerzi.commerziapi.model;

import jakarta.persistence.*;

/**
 * Class representing a user in the Commerzi application.
 */
@Entity
public class CommerziUser {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int userId;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    private String city;

    private String session;

    /**
     * Default constructor.
     */
    public CommerziUser() {}

    /**
     * Parameterized constructor.
     *
     * @param userId the user ID
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @param address the address of the user
     */
    public CommerziUser(int userId, String firstName, String lastName, String email, String password, String address) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email
     */
    public String getEmail() { return email; }

    /**
     * Gets the password of the user.
     *
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Gets the address of the user.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the city of the user
     *
     * @return the city
     */
    public String getCity() { return city; }

    /**
     * Gets the session of the user.
     *
     * @return the session
     */
    public String getSession() { return session; }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Sets the address of the user.
     *
     * @param address the address to set
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Sets the city of the user
     *
     * @param city the city to set
     */
    public void setCity(String city) { this.city = city; }

    /**
     * Sets the session of the user.
     *
     * @param session the session to set
     */
    public void setSession(String session) { this.session = session; }

    /**
     * Creates a clone of the current user.
     *
     * @return a cloned instance of the user
     */
    public CommerziUser clone() {
        CommerziUser cloned = new CommerziUser();

        cloned.userId = this.userId;
        cloned.firstName = this.firstName;
        cloned.lastName = this.lastName;
        cloned.email = this.email;
        cloned.password = this.password;
        cloned.city = this.city;
        cloned.address = this.address;
        cloned.session = this.session;

        return cloned;
    }

    /**
     * Merges the second user into the current user.
     *
     * @param second the user to merge
     */
    public void merge(CommerziUser second) {
        setFirstName(second.getFirstName());
        setLastName(second.getLastName());
        setEmail(second.getEmail());
        setPassword(second.getPassword());
        setAddress(second.getAddress());
        setCity(second.getCity());
        setSession(second.getSession());
    }
}