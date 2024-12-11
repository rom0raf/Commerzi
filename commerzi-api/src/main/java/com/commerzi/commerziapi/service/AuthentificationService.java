package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.HashPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication-related operations.
 */
@Service
public class AuthentificationService implements IAuthentificationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Verifies user credentials.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the credentials are valid, false otherwise
     */
    public boolean checkUserCredentials(String email, String password) {
        CommerziUser commerziUser = userRepository.getUserByEmail(email);
        return commerziUser != null && HashPassword.validateHash(password, commerziUser.getPassword());
    }

    /**
     * Retrieves a user by their session.
     *
     * @param session the session of the user
     * @return the user associated with the session
     */
    public CommerziUser getUserBySession(String session) {
        return userRepository.getUserBySession(session);
    }
}