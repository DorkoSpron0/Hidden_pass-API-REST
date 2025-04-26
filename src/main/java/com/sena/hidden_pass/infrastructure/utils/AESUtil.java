package com.sena.hidden_pass.infrastructure.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESUtil {

    private static final String SECRET_KEY = "MiClaveAES123456";
    private static final String ALGORITHM = "AES";

    // cifrar
    public String encrypt(String rawPassword) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(rawPassword.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // decifrar
    public String decrypt(String encryptedText) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] originalBytes = cipher.doFinal(decodedBytes);
        return new String(originalBytes);
    }

    public boolean passTest(){
        return true;
    }
}
