package com.yesidrangel.dian.xml.validator.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CufeGeneratorUtil {
    /**
     * Genera un hash SHA-384 en formato hexadecimal (minúsculas) de la cadena dada.
     *
     * @param input cadena a hashear (no debe ser null)
     * @return hash SHA-384 en hexadecimal (64 caracteres)
     * @throws RuntimeException si ocurre un error criptográfico inesperado
     */
    public static String sha384(String input) {
        if (input == null) {
            throw new IllegalArgumentException("La entrada no puede ser null");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-384 está garantizado en todas las JVMs modernas
            throw new RuntimeException("Algoritmo SHA-384 no disponible", e);
        }
    }

    /**
     * Convierte un arreglo de bytes a una cadena hexadecimal.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
