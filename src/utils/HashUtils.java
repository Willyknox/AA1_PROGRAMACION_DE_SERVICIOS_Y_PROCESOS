package utils;

import java.security.MessageDigest;

public class HashUtils {

    public static String sha256(String input) {
        try {
            // in this case I comenze creating a hash function (message digest)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // convert string to bytes
            byte[] hash = digest.digest(input.getBytes());
            // convert byte to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                // ths is to avoid negative numbers it just give absolute values in hexadecimal
                String hex = Integer.toHexString(0xff & b);
                // this appends a 0 when the value is less than 16 (in hexadecimal, this is
                // avoid creating a incorrect hash code as all bytes are displayes with two
                // numbers
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
