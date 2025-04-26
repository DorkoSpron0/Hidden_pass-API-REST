package com.sena.hidden_pass.infrastructure.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UT_AESUtilTest {

    private AESUtil aesUtil;

    @BeforeEach
    void init(){
        aesUtil = new AESUtil();
    }

    @Test
    void testEncryptAndDecryptSuccess() throws Exception {
        String original = "miContraseñaSegura123!";
        String encrypted = aesUtil.encrypt(original);
        String decrypted = aesUtil.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    @Test
    void testEncryptEmptyString() throws Exception {
        String encrypted = aesUtil.encrypt("");
        assertNotNull(encrypted);
    }

    @Test
    void testDecryptInvalidBase64ShouldThrowException() {
        assertThrows(Exception.class, () -> {
            aesUtil.decrypt("texto-no-base64");
        });
    }

    @Test
    void testEncryptNullShouldThrowException() {
        assertThrows(NullPointerException.class, () -> aesUtil.encrypt(null));
    }

    @Test
    void testPassTest(){
        // When
        boolean result = this.aesUtil.passTest();

        // Then
        assertTrue(result);
    }
}
