package com.commerzi.commerziapi.controller;

import com.commerzi.commerziapi.model.User;
import com.commerzi.commerziapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody User user) {
        try {
            String userId = userService.createUser(user);
            return ResponseEntity.ok(userId);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
