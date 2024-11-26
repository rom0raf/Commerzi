/*
 *
 */

package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.database.mysql.MySQLConnection;
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

    @Autowired
    private IAuthentificationService authentificationService;

    @Autowired
    private MySQLConnection test;

    /**
     *
     * @return
     */
    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody User user) {
        boolean isAuth = authentificationService.checkUserCredentials(user.getEmail(), user.getPassword());

        User realUser = authentificationService.getUser(user.getEmail());
        User userCopy = realUser.clone();
        userCopy.setPassword("");

        if (isAuth) {
            authentificationService.setupSession(realUser);
            userCopy.setSession(realUser.getSession());
            return ResponseEntity.ok(userCopy);
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
