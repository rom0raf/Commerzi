package com.commerzi.commerziapi.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe utilitaire de sécurité pour Commerzi
 */
public class Security {

    /** Générateur aléatoire sécurisé */
    private static final SecureRandom secureRandom = new SecureRandom();

    /** Taille d'un token de session */
    private static final int SESSION_LENGTH = 64;

    /**
     * Genère un token de session aléatoire
     * de la taille {@code SESSION_LENGTH}
     * @return le token de session
     */
    public static String generateRandomSession() {
        byte[] randomBytes = new byte[SESSION_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}
