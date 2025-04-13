package ru.mtuci.Kozazoev_M_BVT2151;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Утилита для вычисления SHA-256 хэша
public class SignatureUtils {
    public static String calculateSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString(); // Возвращаю хэш в виде строки
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating SHA-256", e);
        }
    }
}