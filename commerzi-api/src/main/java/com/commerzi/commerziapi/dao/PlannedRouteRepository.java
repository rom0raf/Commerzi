package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.PlannedRoute;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlannedRouteRepository extends MongoRepository<PlannedRoute, String> {

    List<PlannedRoute> findByUserId(String userId);

    List<PlannedRoute> findByCustomersId(String customerId);
}
