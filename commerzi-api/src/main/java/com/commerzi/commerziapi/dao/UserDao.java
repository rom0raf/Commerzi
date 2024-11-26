package com.commerzi.commerziapi.dao;

import com.commerzi.commerziapi.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Classe temporaire avec seulement des users en mémoire
 * TODO: utiliser une base de données
 */
@Repository
public class UserDao {

    private final static ArrayList<User> temporaryUsers = new ArrayList<>() {{
        add(new User("1", "John", "Doe", "john.doe@gmail.com", "john", "1234 Main St"));
        add(new User("2", "Jane", "Doe", "jane.doe@gmail.com", "jane", "1235 Main St"));
    }};

    public boolean exists(String email, String password) {
        for (User user : temporaryUsers) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(String email) {
        for (User user : temporaryUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public User getUserBySession(String session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }
        for (User user : temporaryUsers) {
            if (session.equals(user.getSession())) {
                return user;
            }
        }
        return null;
    }


    public String createUser(User user) {
        // TODO créer un user id et le donner   à l'utilisateur
        // TODO en base de données juste récup le dernier id crée
        temporaryUsers.add(user);

        // TODO return le user id
        return "User created";
    }
}