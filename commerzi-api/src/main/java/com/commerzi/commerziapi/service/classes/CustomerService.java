package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.address.CheckAddress;
import com.commerzi.commerziapi.dao.CustomerRepository;
import com.commerzi.commerziapi.dao.PlannedRouteRepository;
import com.commerzi.commerziapi.model.Contact;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.ECustomerType;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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
    public List<Customer> getAllCustomers(String userId) {
        return customerRepository.findByUserId(userId);
    }

    /**
     * Retrieves all clients.
     *
     * @return a list of all clients
     */
    public List<Customer> getClients(String userId) {
        return customerRepository.findByTypeAndUserId(ECustomerType.client.toString(), userId);
    }

    /**
     * Retrieves all prospects.
     *
     * @return a list of all prospects
     */
    public List<Customer> getProspects(String userId) {
        return customerRepository.findByTypeAndUserId(ECustomerType.prospect.toString(), userId);
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
     * @throws IllegalArgumentException if the customer is invalid
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
     * @param existingCustomer the existing customer to update
     * @param customer the customer data to update
     * @return the updated customer, or null if not found
     * @throws IllegalArgumentException if the customer is invalid
     */
    public Customer updateCustomer(Customer existingCustomer, Customer customer) throws IllegalArgumentException {
        verifyCustomer(customer);

        existingCustomer.merge(customer);

        if (existingCustomer.getAddress() != null && existingCustomer.getCity() != null) {
            existingCustomer.setGpsCoordinates(
                    CheckAddress.getCoordinates(existingCustomer.getAddress(), existingCustomer.getCity())
            );
        }

        return customerRepository.save(existingCustomer);
    }

    /**
     * Deletes a customer by its ID.
     *
     * @param id the ID of the customer to delete
     * @return the deleted customer, or null if not found
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
     * Retrieves all customers near the user.
     *
     * @param userId the ID of the user
     * @return a list of customers near the user
     */
//    public List<Customer> getNearCustomers(String userId, GeoJsonPoint location, double distance) {
//        return customerRepository.findByLocationNear(
//                userId,
//                location,
//                new Distance(distance)
//        );
//    }

    /**
     * Verifies the validity of a customer.
     *
     * @param customer the customer to verify
     * @param checkAddress optional parameter to check the address
     * @throws IllegalArgumentException if the customer is invalid
     */
    public static void verifyCustomer(Customer customer, boolean... checkAddress) throws IllegalArgumentException {

        if (customer.getType() == null) {
            throw new IllegalArgumentException("Customer type is required");
        }
        if (customer.getType() != ECustomerType.client && customer.getType() != ECustomerType.prospect) {
            throw new IllegalArgumentException("Invalid customer type");
        }
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        if (checkAddress.length > 0 && checkAddress[0]) {
            if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
                throw new IllegalArgumentException("Address is required");
            }

            if (customer.getCity() == null || customer.getCity().isEmpty()) {
                throw new IllegalArgumentException("City is required");
            }

            if (CheckAddress.isAddressInvalid(customer.getAddress(), customer.getCity())) {
                throw new IllegalArgumentException("Invalid address");
            }
        }

        checkContact(customer.getContact());
    }

    /**
     * Checks the validity of a contact.
     *
     * @param contact the contact to check
     * @throws IllegalArgumentException if the contact is invalid
     */
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