package com.commerzi.commerziapi.service.interfaces;

import com.commerzi.commerziapi.model.CommerziUser;

public interface IAuthentificationService {

    boolean checkUserCredentials(String email, String password);

    CommerziUser getUserBySession(String session);
}
