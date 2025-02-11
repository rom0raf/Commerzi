package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.PlannedRouteRepository;
import com.commerzi.commerziapi.model.PlannedRoute;
import com.commerzi.commerziapi.service.classes.PlannedRouteService;
import com.opencagedata.jopencage.model.JOpenCageLatLng;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlannedRouteServiceTest {

    @Mock
    private PlannedRouteRepository plannedRouteRepository;

    @InjectMocks
    private PlannedRouteService plannedRouteService;

    private List<JOpenCageLatLng> fixtures = new ArrayList<>() {{
        add(new JOpenCageLatLng() {{ setLat(1.0); setLng(1.0); }});
        add(new JOpenCageLatLng() {{ setLat(2.0); setLng(2.0); }});
        add(new JOpenCageLatLng() {{ setLat(3.0); setLng(3.0); }});
    }};

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoute() throws Exception {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setStartingPoint(
                
        );
        route.setCustomersAndProspects(new ArrayList<>());

        when(plannedRouteRepository.save(any(PlannedRoute.class))).thenReturn(route);

        String id = plannedRouteService.createRoute(route, false);

        assertEquals("1", id);
        verify(plannedRouteRepository, times(1)).save(route);
    }

    @Test
    void testGetPlannedRouteById() {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");

        when(plannedRouteRepository.findById("1")).thenReturn(Optional.of(route));

        PlannedRoute result = plannedRouteService.getPlannedRouteById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(plannedRouteRepository, times(1)).findById("1");
    }

    @Test
    void testGetAll() {
        List<PlannedRoute> routes = new ArrayList<>();
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        routes.add(route);

        when(plannedRouteRepository.findByUserId("user1")).thenReturn(routes);

        List<PlannedRoute> result = plannedRouteService.getAll("user1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        verify(plannedRouteRepository, times(1)).findByUserId("user1");
    }

    @Test
    void testUpdateRoute() {
        PlannedRoute route = new PlannedRoute();
        route.setId("1");
        route.setStartingPoint("Start Point");
        route.setCustomersAndProspects(new ArrayList<>());

        when(plannedRouteRepository.save(any(PlannedRoute.class))).thenReturn(route);

        plannedRouteService.updateRoute(route);

        verify(plannedRouteRepository, times(1)).save(route);
    }

    @Test
    void testDeleteRouteById() {
        doNothing().when(plannedRouteRepository).deleteById("1");

        plannedRouteService.deleteRouteById("1");

        verify(plannedRouteRepository, times(1)).deleteById("1");
    }
}