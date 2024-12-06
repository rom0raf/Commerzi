package com.commerzi.commerziapi.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    /** Nom du header HTTP utilisé pour l'authentification */
    public final static String AUTHENTIFICATION_HEADER_NAME = "X-Commerzi-Auth";

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

    /**
     * Renvoie la session commerzi envoyée par le client
     * Cette session est présente dans les header de la requete
     * @return la session commerzi si présente sinon null
     */
    public static String getSessionFromSpring() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(AUTHENTIFICATION_HEADER_NAME);
    }

}
