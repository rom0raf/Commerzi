package com.commerzi.commerziapi.dto;

import com.commerzi.commerziapi.model.Customer;

import java.util.List;

public class NearbyCustomersDTO {

    private List<Customer> customers;

    public NearbyCustomersDTO() {}

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}