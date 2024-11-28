/*
 *
 */

package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.service.IAuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    @Autowired
    private IAuthentificationService authentificationService;

    /**
     *
     * @return
     */
    @PostMapping("/")
    public ResponseEntity auth(@RequestBody CommerziUser commerziUser) {
        boolean isAuth = authentificationService.checkUserCredentials(commerziUser.getEmail(), commerziUser.getPassword());

        CommerziUser realCommerziUser = authentificationService.getUser(commerziUser.getEmail());
        CommerziUser commerziUserCopy = realCommerziUser.clone();
        commerziUserCopy.setPassword("");

        if (isAuth) {
            authentificationService.setupSession(realCommerziUser);
            commerziUserCopy.setSession(realCommerziUser.getSession());
            return ResponseEntity.ok(commerziUserCopy);
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
