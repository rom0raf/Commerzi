package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.ActualRoute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActualRouteRepository extends MongoRepository<ActualRoute, String> {
}
