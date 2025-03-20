package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.ActualRoute;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActualRouteRepository extends MongoRepository<ActualRoute, String> {

    ActualRoute findByPlannedRouteId(String routeId);

    List<ActualRoute> findByUserId(String userId);

}
