package com.commerzi.commerziapi.model;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlannedRouteTest {

    @Test
    void testUpdateRouteInvalidCustomers() {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setStartingPoint(new Coordinates(40.7128, -74.0060));

        // invalid customers with setter

        // empty customers
        assertThrows(IllegalArgumentException.class, () -> route.setCustomers(new ArrayList<>()));

        // null customers
        assertThrows(IllegalArgumentException.class, () -> route.setCustomers(null));

        // less than 2 customers
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        assertThrows(IllegalArgumentException.class, () -> route.setCustomers(customers));

        // more than 8 customers
        for (int i = 0; i < 9; i++) {
            customers.add(new Customer());
        }
        assertThrows(IllegalArgumentException.class, () -> route.setCustomers(customers));
    }

    @Test
    void testPlannedRouteCreation() {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setUserId("user1");
        route.setStartingPoint(new Coordinates(40.7128, -74.006));
        route.setEndingPoint(new Coordinates(34.0522, 118.2437));
        route.setTotalDistance(3940.0);

        List<Customer> customers = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        Customer customer2 = new Customer();
        customer2.setName("Jane Doe");
        customers.add(customer1);
        customers.add(customer2);

        route.setCustomers(customers);

        assertEquals("1", route.getId());
        assertEquals("user1", route.getUserId());
        assertEquals(new Coordinates(40.7128, -74.0060), route.getStartingPoint());
        assertEquals(new Coordinates(34.0522, 118.2437), route.getEndingPoint());
        assertEquals(3940.0, route.getTotalDistance());
        assertEquals(2, route.getCustomers().size());
        assertEquals("John Doe", route.getCustomers().get(0).getName());
        assertEquals("Jane Doe", route.getCustomers().get(1).getName());
    }

    @Test
    void testSetTotalDistance() {
        PlannedRoute route = new PlannedRoute();
        route.setTotalDistance(100.0);
        assertEquals(100.0, route.getTotalDistance());
    }

}