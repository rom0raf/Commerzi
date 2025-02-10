package com.commerzi.commerziapi.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Security utility class for Commerzi.
 */
public class Security {

    /** Secure random number generator */
    private static final SecureRandom secureRandom = new SecureRandom();

    /** Length of a session token */
    private static final int SESSION_LENGTH = 64;

    /** Name of the HTTP header used for authentication */
    public final static String AUTHENTIFICATION_HEADER_NAME = "X-Commerzi-Auth";

    /**
     * Generates a random session token of length {@code SESSION_LENGTH}.
     *
     * @return the session token
     */
    public static String generateRandomSession() {
        byte[] randomBytes = new byte[SESSION_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Returns the Commerzi session sent by the client.
     * This session is present in the request headers.
     *
     * @return the Commerzi session if present, otherwise null
     */
    public static String getSessionFromSpring() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(AUTHENTIFICATION_HEADER_NAME);
    }

}