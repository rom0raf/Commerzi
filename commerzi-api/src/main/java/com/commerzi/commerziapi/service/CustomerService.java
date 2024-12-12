package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.CustomerRepository;
import com.commerzi.commerziapi.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getClients() {
        return customerRepository.findByType("client");
    }

    public List<Customer> getProspects() {
        return customerRepository.findByType("prospect");
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(String id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return null;
        }
        existingCustomer.setName(customer.getName());
        existingCustomer.setType(customer.getType());
        existingCustomer.setAddress(customer.getAddress());
        return customerRepository.save(existingCustomer);
    }

    public boolean deleteCustomer(String id) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return false;
        }
        customerRepository.delete(existingCustomer);
        return true;
    }


    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
