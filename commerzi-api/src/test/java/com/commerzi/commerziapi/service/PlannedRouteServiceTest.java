package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.PlannedRouteRepository;
import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.Contact;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.classes.PlannedRouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlannedRouteServiceTest {

    @Mock
    private PlannedRouteRepository plannedRouteRepository;

    @InjectMocks
    private PlannedRouteService plannedRouteService;

    private List<Customer> smallCustomers;
    private List<Customer> customers;
    private List<Customer> largeCustomers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        smallCustomers = new ArrayList<>();
        smallCustomers.add(new Customer() {{
            setGpsCoordinates( new Coordinates(40.7128, -74.0060) );
            setName("Customer 1");
            setUserId("1");
            setAddress("123 Main St");
            setCity("New York");
            setContact( new Contact() {{ setFirstName("Oui"); setLastName("Non"); setPhoneNumber("0123456789"); }} );
        }});

        customers = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            customers.add(new Customer() {{
                setGpsCoordinates( new Coordinates(40.7128, -74.0060) );
                setName("Customer " + finalI);
                setUserId("1");
                setAddress("123 Main St");
                setCity("New York");
                setContact( new Contact() {{ setFirstName("Oui"); setLastName("Non"); setPhoneNumber("0123456789"); }} );
            }});
        }

        largeCustomers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            int finalI = i;
            largeCustomers.add(new Customer() {{
                setGpsCoordinates( new Coordinates(40.7128, -74.0060) );
                setName("Customer " + finalI);
                setUserId("1");
                setAddress("123 Main St");
                setCity("New York");
                setContact( new Contact() {{ setFirstName("Oui"); setLastName("Non"); setPhoneNumber("0123456789"); }} );
            }});
        }
    }

    @Test
    void testCreateRoute() throws Exception {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setCustomersAndProspects(customers);
        route.setStartingPoint(new Coordinates(40.7128, -74.0060));
        route.setEndingPoint(new Coordinates(40.7128, -74.0060));
        route.setTotalDistance(0.0);
        route.setUserId("1");

        when(plannedRouteRepository.save(route)).thenReturn(route);

//        String id = plannedRouteService.createRoute();
//        assertEquals("1", id);
    }

    @Test
    void testGetPlannedRouteById() {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setCustomersAndProspects(customers);
        route.setStartingPoint(new Coordinates(40.7128, -74.0060));
        route.setEndingPoint(new Coordinates(40.7128, -74.0060));
        route.setTotalDistance(0.0);
        route.setUserId("1");

        when(plannedRouteRepository.findById("1")).thenReturn(Optional.of(route));

        PlannedRoute foundRoute = plannedRouteService.getPlannedRouteById("1");

        assertEquals(route, foundRoute);
    }

    @Test
    void testGetAll() {
        PlannedRoute route1 = new PlannedRoute();
        route1.setId("1");
        route1.setCustomersAndProspects(customers);
        route1.setStartingPoint(new Coordinates(40.7128, -74.0060));
        route1.setEndingPoint(new Coordinates(40.7128, -74.0060));
        route1.setTotalDistance(0.0);
        route1.setUserId("1");

        PlannedRoute route2 = new PlannedRoute();
        route2.setId("2");
        route2.setCustomersAndProspects(customers);
        route2.setStartingPoint(new Coordinates(40.7128, -74.0060));
        route2.setEndingPoint(new Coordinates(40.7128, -74.0060));
        route2.setTotalDistance(0.0);
        route2.setUserId("1");

        when(plannedRouteRepository.findByUserId("1")).thenReturn(List.of(route1, route2));

        List<PlannedRoute> foundRoutes = plannedRouteService.getAll("1");

        assertEquals(2, foundRoutes.size());
        assertTrue(foundRoutes.contains(route1));
        assertTrue(foundRoutes.contains(route2));
    }

    @Test
    void testUpdateRoute() throws Exception {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setCustomersAndProspects(customers);
        route.setStartingPoint(new Coordinates(40.71028, -74.00060));
        route.setEndingPoint(new Coordinates(40.71128, -74.00160));
        route.setTotalDistance(0.0);
        route.setUserId("1");

        when(plannedRouteRepository.save(route)).thenReturn(route);

        String name = "Route 1";
        plannedRouteService.updateRoute(name, route);

        verify(plannedRouteRepository, times(1)).save(route);
    }

    @Test
    void testDeleteRouteById() {
        plannedRouteService.deleteRouteById("1");

        verify(plannedRouteRepository, times(1)).deleteById("1");
    }
}