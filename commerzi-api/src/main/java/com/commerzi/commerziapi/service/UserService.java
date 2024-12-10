package com.commerzi.commerziapi.service;

import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.exception.UserArgumentException;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.HashPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    private static final Pattern EMAIL_VALIDE = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public int createUser(CommerziUser commerziUser) throws UserArgumentException {

        if (userRepository.getUserByEmail(commerziUser.getEmail()) != null) {
            throw new UserArgumentException("L'email est déjà utilisée");
        }
        if (commerziUser.getEmail() == null || commerziUser.getEmail().isEmpty()) {
            throw new UserArgumentException("L'Email est vide");
        }
        if (!EMAIL_VALIDE.matcher(commerziUser.getEmail()).matches()) {
            throw new UserArgumentException("L'email n'est pas valide");
        }
        if (commerziUser.getPassword() == null || commerziUser.getPassword().isEmpty()) {
            throw new UserArgumentException("Le mot de passe est vide");
        }
        if (commerziUser.getFirstName() == null || commerziUser.getFirstName().isEmpty()) {
            throw new UserArgumentException("Le prénom est vide");
        }
        if (commerziUser.getLastName() == null || commerziUser.getLastName().isEmpty()) {
            throw new UserArgumentException("Le nom de famille est vide");
        }
        if (commerziUser.getAddress() == null || commerziUser.getAddress().isEmpty()) {
            throw new UserArgumentException("L'addresse est vide");
        }

//         TODO Verif critère mot de passe si on en a
//        if (commerziUser.getPassword().length() < 8) {
//            throw new UserArgumentException("Password is too short");
//        }

        String hashedPassword = HashPassword.hash(commerziUser.getPassword());
        commerziUser.setPassword(hashedPassword);
        CommerziUser u = userRepository.save(commerziUser);
        return u.getUserId();
    }

    public boolean exists(String email, String password) {
        CommerziUser commerziUser = userRepository.getUserByEmail(email);
        return commerziUser != null && HashPassword.validateHash(password, commerziUser.getPassword());

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