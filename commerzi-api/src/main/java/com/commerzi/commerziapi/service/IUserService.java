package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.model.CommerziUser;

public interface IUserService {
    
    String createUser(CommerziUser commerziUser);

    boolean exists(String email, String password);
}
