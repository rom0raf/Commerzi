/*
 *
 */

package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.User;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.service.IAuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/api")
public class AuthentificationController {

    private IAuthentificationService authentificationService;

    /**
     *
     * @param authentificationService
     */
    @Autowired
    public AuthentificationController(IAuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    /**
     *
     * @return
     */
    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody User user) {
        boolean isAuth = authentificationService.checkUserCredentials(user.getEmail(), user.getPassword());

        user = authentificationService.getUser(user.getEmail());
        user.setPassword("");

        if (isAuth) {
            authentificationService.setupSession(user);
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    /**
     * Test endpoint to see if the session passed in the header is valid
     */
    @GetMapping("/test")
    @CommerziAuthenticated
    public ResponseEntity test() {
        return ResponseEntity.ok("Session is valid");
    }


}
