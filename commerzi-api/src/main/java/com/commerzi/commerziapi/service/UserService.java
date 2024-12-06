package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.model.CommerziUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    public int createUser(CommerziUser commerziUser) {
        // TODO vérif l'email est unique
        // Verif critère mot de passe si on en a
        // TODO throw une erreur custom qui renvoie un message précis sur l'erreur
        CommerziUser u = userRepository.save(commerziUser);
        return u.getUserId();
    }

    public boolean exists(String email, String password) {
        CommerziUser commerziUser = userRepository.getUserByEmail(email);
        return commerziUser != null && commerziUser.getPassword().equals(password);

    }

    public CommerziUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public void updateUser(CommerziUser commerziUser) {
        userRepository.save(commerziUser);
    }

    public void deleteUser(CommerziUser user) {
        userRepository.delete(user);
    }
}