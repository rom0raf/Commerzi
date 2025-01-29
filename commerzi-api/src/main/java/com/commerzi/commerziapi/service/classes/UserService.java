package com.commerzi.commerziapi.service.classes;

import com.commerzi.commerziapi.address.CheckAddress;
import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.exception.UserArgumentException;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.HashPassword;
import com.commerzi.commerziapi.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Service class for managing users in the Commerzi application.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Pattern for validating email addresses.
     */
    private static final Pattern EMAIL_VALIDE = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Creates a new user.
     *
     * @param commerziUser the user to create
     * @return the ID of the created user
     * @throws UserArgumentException if any user argument is invalid
     */
    public int createUser(CommerziUser commerziUser) throws UserArgumentException {

        verifyUser(commerziUser);

        String hashedPassword = HashPassword.hash(commerziUser.getPassword());
        commerziUser.setPassword(hashedPassword);
        CommerziUser u = userRepository.save(commerziUser);
        return u.getUserId();
    }

    /**
     * Checks if a user exists with the given email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the user exists, false otherwise
     */
    public boolean exists(String email, String password) {
        CommerziUser commerziUser = userRepository.getUserByEmail(email);
        return commerziUser != null && HashPassword.validateHash(password, commerziUser.getPassword());
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the user with the specified email
     */
    public CommerziUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    /**
     * Updates an existing user.
     *
     * @param commerziUser the user to update
     */
    public void updateUser(CommerziUser commerziUser) throws UserArgumentException {
        verifyUser(commerziUser);

        userRepository.save(commerziUser);
    }

    /**
     * Deletes an existing user.
     *
     * @param user the user to delete
     */
    public void deleteUser(CommerziUser user) {
        userRepository.delete(user);
    }


    public static boolean verifyUser(CommerziUser commerziUser) throws UserArgumentException {
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

        if (!CheckAddress.checkAddress(commerziUser.getAddress(), commerziUser.getCity())) {
            throw new UserArgumentException("L'addresse n'est pas valide");
        }

        if (commerziUser.getPassword().length() < 8) {
            throw new UserArgumentException("Password is too short");
        }

        return true;
    }
}