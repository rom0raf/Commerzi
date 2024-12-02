package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.dao.UserRepository;
import com.commerzi.commerziapi.model.CommerziUser;
import com.commerzi.commerziapi.security.CommerziAuthenticated;
import com.commerzi.commerziapi.security.Security;
import com.commerzi.commerziapi.service.IAuthentificationService;
import com.commerzi.commerziapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthentificationService authentificationService;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody CommerziUser commerziUser) {
        try {
            String userId = userService.createUser(commerziUser);
            return ResponseEntity.ok(userId);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/")
    @CommerziAuthenticated
    public ResponseEntity edit(@RequestBody CommerziUser modifiedUser) {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());

        user.merge(modifiedUser);
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    @CommerziAuthenticated
    public ResponseEntity delete() {
        CommerziUser user = authentificationService.getUserBySession(Security.getSessionFromSpring());
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

}
