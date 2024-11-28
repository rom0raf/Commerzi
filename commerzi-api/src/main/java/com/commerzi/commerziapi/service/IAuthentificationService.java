package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.model.CommerziUser;

public interface IAuthentificationService {

    boolean checkUserCredentials(String email, String password);

    void setupSession(CommerziUser commerziUser);

    CommerziUser getUser(String email);

    CommerziUser getUserBySession(String session);
}
