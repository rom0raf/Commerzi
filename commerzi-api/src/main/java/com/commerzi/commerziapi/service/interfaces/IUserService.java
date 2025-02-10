package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.exception.UserArgumentException;
import com.commerzi.commerziapi.model.CommerziUser;

public interface IUserService {
    
    int createUser(CommerziUser commerziUser) throws UserArgumentException;

    boolean exists(String email, String password);

    CommerziUser getUserByEmail(String email);

    void updateUser(CommerziUser commerziUser, boolean... checkAddress) throws UserArgumentException;

    void deleteUser(CommerziUser user);
}
