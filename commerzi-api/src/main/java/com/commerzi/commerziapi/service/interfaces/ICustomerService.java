package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.Customer;

import java.util.List;

public interface ICustomerService {

    List<Customer> getAllCustomers();

    List<Customer> getClients();

    List<Customer> getProspects();

    Customer getCustomerById(String id);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(String id, Customer customer);

    Customer deleteCustomer(String id);

}
