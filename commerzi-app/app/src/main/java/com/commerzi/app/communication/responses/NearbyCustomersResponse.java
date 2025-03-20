package com.commerzi.app.communication.responses;

import com.commerzi.app.customers.Customer;
import com.commerzi.app.dto.NearbyCustomersDTO;

import java.util.List;

public class NearbyCustomersResponse {

    public List<Customer> customers;

    public String message;

    public NearbyCustomersResponse(List<Customer> nearCustomers, String message) {
        customers = nearCustomers;
        message = message;

    }
}
