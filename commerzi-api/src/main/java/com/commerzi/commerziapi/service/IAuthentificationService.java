package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.model.User;

public interface IAuthentificationService {

    boolean checkUserCredentials(String email, String password);

    void setupSession(User user);

    User getUser(String email);

    User getUserBySession(String session);
}
