package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.maps.coordinates.Coordinates;
import com.commerzi.commerziapi.model.Customer;
import com.commerzi.commerziapi.model.ECustomerType;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByType(String type);

    List<Customer> findByUserId(String userId);

    List<Customer> findByTypeAndUserId(ECustomerType type, String userId);

    List<Customer> findByTypeAndUserIdAndGpsCoordinatesNear(ECustomerType eCustomerType, String s, GeoJsonPoint geoJsonPoint, Distance customerDistance);
}
