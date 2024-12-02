package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.model.CommerziUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AuthentificationService implements IAuthentificationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Verify user credentials
     * @param email
     * @param password
     * @return
     */
    public boolean checkUserCredentials(String email, String password) {
        System.out.println("Checking user credentials");
        CommerziUser commerziUser = userRepository.getUserByEmail(email);
        return commerziUser != null && commerziUser.getPassword().equals(password);
    }

    public CommerziUser getUserBySession(String session) {
        return userRepository.getUserBySession(session);
    }
}
