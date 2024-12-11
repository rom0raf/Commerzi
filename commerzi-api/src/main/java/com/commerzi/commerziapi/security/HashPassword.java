package com.commerzi.commerziapi.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Utility class for hashing passwords using PBKDF2 with HMAC SHA-256.
 */
public class HashPassword {

    // Parameters for the hash

    /**
     * Number of bytes for the salt.
     */
    public static final int SALT_BYTES = 32;

    /**
     * Number of bytes for the hash.
     */
    public static final int HASH_BYTES = 32;

    /**
     * Number of iterations for the hashing algorithm.
     */
    public static final int ITERATIONS = 100000;

    /**
     * Name of the hashing algorithm.
     */
    public static final String NOM_ALGORITHME = "PBKDF2WithHmacSHA256";

    /**
     * Secure random number generator.
     */
    public static final SecureRandom GENERATEUR_ALEATOIRE = new SecureRandom();

    /**
     * Hashes a password using PBKDF2 with HMAC SHA-256.
     *
     * @param password the password to hash
     * @return the hashed password in hexadecimal format with the salt
     * @throws RuntimeException if the hashing algorithm is not available or the key specification is invalid
     */
    public static String hash(String password) throws RuntimeException {
        // Generate a salt for the password
        byte[] salt = new byte[SALT_BYTES];
        GENERATEUR_ALEATOIRE.nextBytes(salt);

        // Create the hash with the password and the salt
        try {
            byte[] hash = execute_algorithm(password, salt);
            return byteToHexa(salt) + ":" + byteToHexa(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates a password against a hashed password.
     *
     * @param password the password to validate
     * @param validHash the hashed password to validate against
     * @return true if the password is valid, false otherwise
     * @throws RuntimeException if the hashing algorithm is not available or the key specification is invalid
     */
    public static boolean validateHash(String password, String validHash) {
        String[] params = validHash.split(":");
        byte[] salt = hexaToByte(params[0]);
        String realHash = params[1];

        try {
            byte[] realHashToCompare = execute_algorithm(password, salt);
            return realHash.equals(byteToHexa(realHashToCompare));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param hexa the hexadecimal string
     * @return the byte array
     */
    private static byte[] hexaToByte(String hexa) {
        byte[] binary = new byte[hexa.length() / 2];
        for(int i = 0; i < binary.length; i++) {
            binary[i] = (byte)Integer.parseInt(hexa.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    /**
     * Executes the PBKDF2 algorithm with the given password and salt.
     *
     * @param password the password
     * @param salt the salt
     * @return the hashed password
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    private static byte[] execute_algorithm(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_BYTES * 8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(NOM_ALGORITHME);
        return secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param array the byte array
     * @return the hexadecimal string
     */
    private static String byteToHexa(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}