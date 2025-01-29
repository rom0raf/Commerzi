package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.address.CheckAddress;
import com.commerzi.commerziapi.dao.CustomerRepository;
import com.commerzi.commerziapi.model.Contact;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.ECustomerType;
import com.commerzi.commerziapi.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing customers in the Commerzi application.
 */
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves all customers.
     *
     * @return a list of all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves all clients.
     *
     * @return a list of all clients
     */
    public List<Customer> getClients() {
        return customerRepository.findByType(ECustomerType.client.toString());
    }

    /**
     * Retrieves all prospects.
     *
     * @return a list of all prospects
     */
    public List<Customer> getProspects() {
        return customerRepository.findByType(ECustomerType.prospect.toString());
    }

    /**
     * Retrieves a customer by its ID.
     *
     * @param id the ID of the customer to retrieve
     * @return the customer with the specified ID, or null if not found
     */
    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    /**
     * Creates a new customer.
     *
     * @param customer the customer to create
     * @return the created customer
     */
    public Customer createCustomer(Customer customer) throws IllegalArgumentException {
        verifyCustomer(customer);

        customer.setGpsCoordinates(
                CheckAddress.getCoordinates(customer.getAddress(), customer.getCity())
        );

        return customerRepository.save(customer);
    }

    /**
     * Updates an existing customer.
     *
     * @param id the ID of the customer to update
     * @param customer the customer data to update
     * @return the updated customer, or null if not found
     */
    public Customer updateCustomer(String id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return null;
        }

        verifyCustomer(customer);

        existingCustomer.merge(customer);

        existingCustomer.setGpsCoordinates(
                CheckAddress.getCoordinates(existingCustomer.getAddress(), existingCustomer.getCity())
        );

        return customerRepository.save(existingCustomer);
    }

    /**
     * Deletes a customer by its ID.
     *
     * @param id the ID of the customer to delete
     * @return true if the customer was deleted, false otherwise
     */
    public Customer deleteCustomer(String id) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return null;
        }
        customerRepository.delete(existingCustomer);
        return existingCustomer;
    }

    /**
     * Verifies the validity of a customer.
     *
     * @param customer the customer to verify
     * @return true if the customer is valid
     * @throws IllegalArgumentException if the customer is invalid
     */
    public static boolean verifyCustomer(Customer customer) throws IllegalArgumentException {
        if (!CheckAddress.checkAddress(customer.getAddress(), customer.getCity())) {
            throw new IllegalArgumentException("Invalid address");
        }
        if (customer.getType() == null) {
            throw new IllegalArgumentException("Customer type is required");
        }
        if (!customer.getType().equals(ECustomerType.client.toString().toLowerCase())
                && !customer.getType().equals(ECustomerType.prospect.toString().toLowerCase())) {
            throw new IllegalArgumentException("Invalid customer type");
        }
        // if type starts with a capital letter, convert it to lowercase
        switch (customer.getType().toString()) {
            case "Client":
                customer.setType(ECustomerType.client);
                break;
            case "Prospect":
                customer.setType(ECustomerType.prospect);
                break;
            default:
                throw new IllegalArgumentException("Invalid customer type");
        }


        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customer.getCity() == null || customer.getCity().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }

        checkContact(customer.getContact());

        return true;
    }

    private static void checkContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact information is required");
        }
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        if (contact.getFirstName() == null || contact.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (contact.getLastName() == null || contact.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

    }
}