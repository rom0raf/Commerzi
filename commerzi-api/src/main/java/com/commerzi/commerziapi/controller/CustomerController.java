package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.dao.CustomerRepository;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * REST controller for managing customers in the Commerzi application.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves all customers.
     *
     * @return a list of all customers
     */
    @CommerziAuthenticated
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves all customers of type "client".
     *
     * @return a list of customers with type "client"
     */
    @CommerziAuthenticated
    @GetMapping("/clients")
    public List<Customer> getClients() {
        return customerRepository.findByType("client");
    }

    /**
     * Retrieves all customers of type "prospect".
     *
     * @return a list of customers with type "prospect"
     */
    @CommerziAuthenticated
    @GetMapping("/prospects")
    public ResponseEntity<List<Customer>> getProspects() {
        List<Customer> prospects = customerRepository.findByType("prospect");
        return ResponseEntity.ok(prospects);
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id the ID of the customer
     * @return the customer with the specified ID, or null if not found
     */
    @CommerziAuthenticated
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customer);
    }

    /**
     * Creates a new customer.
     *
     * @param customer the customer to create
     * @return the created customer
     */
    @CommerziAuthenticated
    @PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {

        String customerType = customer.getType().toLowerCase();

        if (Customer.CLIENT.equals(customerType) || Customer.PROSPECT.equals(customerType)) {
            customer.setType(customerType);
        } else {
            return null;
        }

        customerRepository.save(customer);
        return ResponseEntity.ok(customer);
    }

    @CommerziAuthenticated
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return null;
        }

        customerRepository.save(customer);
        return ResponseEntity.ok(customer);
    }

    /**
     * Endpoint to check if the Customer API is working.
     *
     * @return a string indicating the API is working
     */
    @CommerziAuthenticated
    @GetMapping("/ping")
    public String ping() {
        return "Customer API is working!";
    }
}