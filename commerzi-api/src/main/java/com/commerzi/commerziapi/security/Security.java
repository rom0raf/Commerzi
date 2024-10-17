package com.commerzi.commerziapi.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 
 */
public class Security {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int SESSION_LENGTH = 32;

    /**
     * Generates a random session token
     * @return a random session token
     */
    public static String generateRandomSession() {
        byte[] randomBytes = new byte[SESSION_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}
