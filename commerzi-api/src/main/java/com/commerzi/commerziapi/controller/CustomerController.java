package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.security.Security;
import com.commerzi.commerziapi.service.interfaces.IAuthentificationService;
import com.commerzi.commerziapi.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing customers in the Commerzi application.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IAuthentificationService authentificationService;

    /**
     * Retrieves all customers.
     *
     * @return a list of all customers
     */
    @CommerziAuthenticated
    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Customer> customers = customerService.getAllCustomers("" + user.getUserId());
        return ResponseEntity.ok(customers);
    }

    @CommerziAuthenticated
    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers2() {
        return getAllCustomers();
    }

    /**
     * Retrieves all customers of type "client".
     *
     * @return a list of customers with type "client"
     */
    @CommerziAuthenticated
    @GetMapping("/clients")
    public ResponseEntity<List<Customer>> getClients() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Customer> clients = customerService.getClients("" + user.getUserId());
        return ResponseEntity.ok(clients);
    }

    /**
     * Retrieves all customers of type "prospect".
     *
     * @return a list of customers with type "prospect"
     */
    @CommerziAuthenticated
    @GetMapping("/prospects")
    public ResponseEntity<List<Customer>> getProspects() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Customer> prospects = customerService.getProspects("" + user.getUserId());
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
        Customer customer = customerService.getCustomerById(id);
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
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        customer.setUserId("" + user.getUserId());

        try {
            customerService.createCustomer(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * Updates an existing customer.
     *
     * @param id the ID of the customer to update
     * @param customer the customer data to update
     * @return the updated customer
     */
    @CommerziAuthenticated
    @PutMapping("/{id}")
    public ResponseEntity updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Customer existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            customerService.updateCustomer(existingCustomer, customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     * @return the deleted customer
     */
    @CommerziAuthenticated
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable String id) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        }

        Customer result = customerService.deleteCustomer(id);
        return ResponseEntity.ok(result);
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