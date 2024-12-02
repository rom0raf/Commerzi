/*
 *
 */

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
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    @Autowired
    private IAuthentificationService authentificationService;

    @Autowired
    private IUserService userService;

    /**
     *
     * @param commerziUser
     * @return
     */
    @PostMapping("/")
    public ResponseEntity auth(@RequestBody CommerziUser commerziUser) {
        boolean isAuth = authentificationService.checkUserCredentials(commerziUser.getEmail(), commerziUser.getPassword());

        if (!isAuth) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        CommerziUser realCommerziUser = userService.getUserByEmail(commerziUser.getEmail());
        realCommerziUser.setSession(Security.generateRandomSession());
        userService.updateUser(realCommerziUser);

        return ResponseEntity.ok(realCommerziUser);
    }

    /**
     * Test endpoint to see if the session passed in the header is valid
     */
    @GetMapping("/ping")
    @CommerziAuthenticated
    public ResponseEntity ping() {
        return ResponseEntity.ok("pong");
    }
}
