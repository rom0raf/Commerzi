package com.commerzi.app.communication.responses;

import com.commerzi.app.auth.User;

public class AuthResponse {
    public final String message;
    public final User user;

    public AuthResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }
}
