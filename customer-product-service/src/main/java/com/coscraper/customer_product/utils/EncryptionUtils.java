package com.coscraper.customer_product.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private SecretKeySpec secretKey;

    @Value("${spring.secret}")
    private String encryptionKey;

    // Method to initialize the secret key
    private void initializeKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    if (encryptionKey == null) {
                        throw new IllegalArgumentException("spring.secret property is not set");
                    }
                    byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
                    secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
                }
            }
        }
    }

    public String encrypt(String data) throws Exception {
        initializeKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedData) throws Exception {
        initializeKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(original);
    }
}