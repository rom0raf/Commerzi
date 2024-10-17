package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserDao;
import com.commerzi.commerziapi.model.User;
import com.commerzi.commerziapi.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AuthentificationService implements IAuthentificationService {

    private UserDao userDao;

    @Autowired
    public AuthentificationService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Verify user credentials
     * @param email
     * @param password
     * @return
     */
    public boolean checkUserCredentials(String email, String password) {
        return userDao.exists(email, password);
    }

    /**
     * Set up a new session for the user
     *
     */
    public void setupSession(User user) {
        user.setSession(
            Security.generateRandomSession()
        );
    }

    /**
     * Get user by email
     * @param email
     * @return
     */
    public User getUser(String email) {
        return userDao.getUser(email);
    }

    public User getUserBySession(String session) {
        return userDao.getUserBySession(session);
    }
}
