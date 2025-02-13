package com.commerzi.app.communication.responses;

import com.commerzi.app.customers.Customer;

import java.util.ArrayList;

public class CustomerResponse {
    public final ArrayList<Customer> customers;
    public final String message;

    public CustomerResponse(ArrayList<Customer> customers, String message) {
        this.customers = customers;
        this.message = message;
    }
}
