package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.security.Security;
import com.commerzi.commerziapi.service.IAuthentificationService;
import com.commerzi.commerziapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing users in the Commerzi application.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthentificationService authentificationService;

    /**
     * Creates a new user.
     *
     * @param commerziUser the user to create
     * @return the response entity with the user ID if successful, or an error message if failed
     */
    @PostMapping("/")
    public ResponseEntity create(@RequestBody CommerziUser commerziUser) {
        try {
            int userId = userService.createUser(commerziUser);
            return ResponseEntity.ok(userId);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Edits an existing user.
     *
     * @param modifiedUser the modified user data
     * @return the response entity indicating success or failure
     */
    @PutMapping("/")
    @CommerziAuthenticated
    public ResponseEntity edit(@RequestBody CommerziUser modifiedUser) {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        user.merge(modifiedUser);
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }

    /**
     * Deletes the current user.
     *
     * @return the response entity indicating success or failure
     */
    @DeleteMapping("/")
    @CommerziAuthenticated
    public ResponseEntity delete() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

}