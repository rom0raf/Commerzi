package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.security.Security;
import com.commerzi.commerziapi.service.interfaces.IAuthentificationService;
import com.commerzi.commerziapi.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling authentication in the Commerzi application.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private IAuthentificationService authentificationService;

    @Autowired
    private IUserService userService;

    /**
     * Authenticates a user based on their credentials.
     *
     * @param commerziUser the user credentials
     * @return the response entity with the authenticated user if successful, or an error message if failed
     */
    @PostMapping("/")
    public ResponseEntity auth(@RequestBody CommerziUser commerziUser) {
        boolean isAuth = authentificationService.checkUserCredentials(commerziUser.getEmail(), commerziUser.getPassword());

        if (!isAuth) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        CommerziUser realCommerziUser = userService.getUserByEmail(commerziUser.getEmail());
        realCommerziUser.setSession(Security.generateRandomSession());
        try {
            userService.updateUser(realCommerziUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(realCommerziUser);
    }

    /**
     * Test endpoint to see if the session passed in the header is valid.
     *
     * @return the response entity with "pong" if the session is valid
     */
    @GetMapping("/ping")
    @CommerziAuthenticated
    public ResponseEntity ping() {
        return ResponseEntity.ok("pong");
    }
}