package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.Security;
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

    /**
     * Set up a new session for the user
     *
     */
    public void setupSession(CommerziUser commerziUser) {
        commerziUser.setSession(
            Security.generateRandomSession()
        );
    }

    /**
     * Get user by email
     * @param email
     * @return
     */
    public CommerziUser getUser(String email) {
        return userRepository.getUserByEmail(email);
    }

    public CommerziUser getUserBySession(String session) {
        return userRepository.getUserBySession(session);
    }
}
