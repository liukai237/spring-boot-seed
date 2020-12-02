package com.iakuil.bf.web.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EncTest {
    private static String mpCryptoPassword = "Bda7bXaExAA";

    @Test
    public void testGenEncPassWd() throws Exception {
        String value = "181818";

        System.out.println("Original Value : " + value);
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(mpCryptoPassword);
        String encryptedPassword = encryptor.encrypt(value);
        System.out.println("Encrypted Value : " + encryptedPassword);
        assertNotNull(encryptedPassword);

        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(mpCryptoPassword);
        assertEquals(value, decryptor.decrypt(encryptedPassword));
    }
}
