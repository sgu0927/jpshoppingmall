package com.jpshoppingmall.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

    @Test
    public void jasypt_encrypt_decrypt() {
        String plainText = "test_text";

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("test_password"); // TODO setPassword
        jasypt.setAlgorithm("PBEWithMD5AndDES");

        String encryptedText = jasypt.encrypt(plainText);
        String decryptedText = jasypt.decrypt(encryptedText);

        System.out.println("plainText : " + plainText);
        System.out.println("encryptedText : " + encryptedText);
        System.out.println("decryptedText : " + decryptedText);

        assertEquals(plainText, decryptedText);
    }
}