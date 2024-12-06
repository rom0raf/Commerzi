package com.commerzi.commerziapi.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class HashPassword {

    // Parametres du hash

    public static final int SALT_BYTES = 32;

    public static final int HASH_BYTES = 32;

    public static final int ITERATIONS = 100000;

    public static final String NOM_ALGORITHME = "PBKDF2WithHmacSHA256";

    public static final SecureRandom GENERATEUR_ALEATOIRE = new SecureRandom();

    public static String hash(String password) throws RuntimeException {
        // On génère un salt pour le mot de passe
        byte[] salt = new byte[SALT_BYTES];
        GENERATEUR_ALEATOIRE.nextBytes(salt);

        // On créer le hash avec le mot de passe et le salt
        try {
            byte[] hash = execute_algorithm(password, salt);
            return byteToHexa(salt) + ":" + byteToHexa(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

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

    private static byte[] hexaToByte(String hexa) {
        byte[] binary = new byte[hexa.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hexa.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }

    private static byte[] execute_algorithm(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_BYTES * 8);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(NOM_ALGORITHME);
        return secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
    }

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
