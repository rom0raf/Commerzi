package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserDao;
import com.commerzi.commerziapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserDao userDao;

    public String createUser(User user) {
        // TODO vérif l'email est unique
        // Verif critère mot de passe si on en a
        // TODO throw une erreur custom qui renvoie un message précis sur l'erreur
        return userDao.createUser(user);
    }

    public boolean exists(String email, String password) {
        return userDao.exists(email, password);
    }

}