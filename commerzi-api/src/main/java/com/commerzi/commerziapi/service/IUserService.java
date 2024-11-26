package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.model.User;

public interface IUserService {
    
    String createUser(User user);

    boolean exists(String email, String password);
}
