package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.address.CheckAddress;
import com.commerzi.commerziapi.dao.CustomerRepository;
import com.commerzi.commerziapi.dao.PlannedRouteRepository;
import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.*;
import com.commerzi.commerziapi.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
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

    @Autowired
    private PlannedRouteRepository plannedRouteRepository;

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
        return customerRepository.findByTypeAndUserId(ECustomerType.client, userId);
    }

    /**
     * Retrieves all prospects.
     *
     * @return a list of all prospects
     */
    public List<Customer> getProspects(String userId) {
        return customerRepository.findByTypeAndUserId(ECustomerType.prospect, userId);
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

        List<PlannedRoute> plannedRoutes = plannedRouteRepository.findByCustomersId(existingCustomer.getId());

        if (existingCustomer.getAddress() != null && existingCustomer.getCity() != null) {
            existingCustomer.setGpsCoordinates(
                    CheckAddress.getCoordinates(existingCustomer.getAddress(), existingCustomer.getCity())
            );

            for (PlannedRoute plannedRoute : plannedRoutes) {
                plannedRouteRepository.deleteById(plannedRoute.getId());
            }
        }

        plannedRoutes.forEach(plannedRoute -> {
            plannedRoute.getCustomers().forEach(c -> {
                if (c.getId().equals(existingCustomer.getId())) {
                    c.merge(existingCustomer);
                }
            });
            plannedRouteRepository.save(plannedRoute);
        });

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

        List<PlannedRoute> plannedRoutes = plannedRouteRepository.findByCustomersId(existingCustomer.getId());
        plannedRoutes.forEach(plannedRoute -> {
            plannedRoute.getCustomers().removeIf(c -> c.getId().equals(existingCustomer.getId()));
            plannedRouteRepository.save(plannedRoute);
        });

        customerRepository.delete(existingCustomer);
        return existingCustomer;
    }

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

        String phoneNumber = contact.getPhoneNumber();
        if (!phoneNumber.matches("(\\+\\d{1,3})?\\d{6,15}")) {
            throw new IllegalArgumentException("Invalid phone number format. Must contain only numbers and may start with '+'.");
        }

        if (contact.getFirstName() == null || contact.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (contact.getLastName() == null || contact.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }

    public List<Customer> getNearbyClients(Coordinates coordinates, CommerziUser user) {

        Point point = new Point(coordinates.getLatitude(), coordinates.getLongitude());

        Distance customerDistance = new Distance(0.2, Metrics.KILOMETERS);
        Distance propectDistance = new Distance(1, Metrics.KILOMETERS);

        System.out.println("Coordinates: " + point);

        // get customers that are in a radius from the coordinates
        List<Customer> customers = customerRepository.findByTypeAndUserIdAndGpsCoordinatesNear(ECustomerType.client, "" + user.getUserId(), new GeoJsonPoint(point), customerDistance);

        // get prospects that are in a radius from the coordinates
        List<Customer> prospects = customerRepository.findByTypeAndUserIdAndGpsCoordinatesNear(ECustomerType.prospect, "" + user.getUserId(), new GeoJsonPoint(point), propectDistance);

        customers.addAll(prospects);

        return customers;
    }
}