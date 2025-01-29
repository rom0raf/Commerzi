package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.PlannedRoute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlannedRouteRepository extends MongoRepository<PlannedRoute, String> {
}
