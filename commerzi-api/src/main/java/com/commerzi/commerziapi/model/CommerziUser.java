package com.commerzi.commerziapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Classe représentant un utilisateur
 */
@Entity
public class CommerziUser {

    @Id
    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String address;

    private String session;

    public CommerziUser() {}

    public CommerziUser(String userId, String firstName, String lastName, String email, String password, String address) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getAddress() {
        return address;
    }

    public String getSession() { return session; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setAddress(String address) { this.address = address; }

    public void setSession(String session) { this.session = session; }

    public CommerziUser clone() {
        CommerziUser cloned = new CommerziUser();

        cloned.userId = this.userId;
        cloned.firstName = this.firstName;
        cloned.lastName = this.lastName;
        cloned.email = this.email;
        cloned.password = this.password;
        cloned.address = this.address;
        cloned.session = this.session;

        return cloned;
    }

    /**
     * Merge the second user into the current user
     * @param second
     */
    public void merge(CommerziUser second) {
        setFirstName(second.getFirstName());
        setLastName(second.getLastName());
        setEmail(second.getEmail());
        setPassword(second.getPassword());
        setAddress(second.getAddress());
        setSession(second.getSession());
    }
}
