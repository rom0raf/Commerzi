package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.model.Customer;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

public interface ICustomerService {

    List<Customer> getAllCustomers(String userId);

    List<Customer> getClients(String userId);

    List<Customer> getProspects(String userId);

    Customer getCustomerById(String id);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer existingCustomer, Customer customer);

    Customer deleteCustomer(String id);

    List<Customer> getNearbyClients(Coordinates coordinates, CommerziUser user);
}
