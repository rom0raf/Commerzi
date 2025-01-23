package com.commerzi.commerziapi.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashPasswordTest {

    // Test de la méthode hash()
    @Test
    void testHash() {
        String password = "monMotDePasseSecurise";

        String hashedPassword = HashPassword.hash(password);

        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.contains(":"));

        String[] parts = hashedPassword.split(":");
        assertTrue(parts[0].matches("[0-9a-fA-F]+"));
        assertTrue(parts[1].matches("[0-9a-fA-F]+"));
    }

    // Test de la méthode validateHash()
    @Test
    void testValidateHash() {
        String password = "monMotDePasseSecurise";
        String hashedPassword = HashPassword.hash(password);

        assertTrue(HashPassword.validateHash(password, hashedPassword));

        assertFalse(HashPassword.validateHash("mauvaisMotDePasse", hashedPassword));
    }
}