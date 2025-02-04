package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByType(String type);

    List<Customer> findByUserId(String userId);

    List<Customer> findByTypeAndUserId(String type, String userId);
}
