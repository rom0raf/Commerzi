package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.model.CommerziUser;

public interface IUserService {
    
    String createUser(CommerziUser commerziUser);

    boolean exists(String email, String password);

    CommerziUser getUserByEmail(String email);

    void updateUser(CommerziUser commerziUser);

    void deleteUser(CommerziUser user);
}
