package com.iakuil.seed.util;


import com.iakuil.seed.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

@Slf4j
public class CryptoDecrypt {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String decrypt(String sessionKey, String encryptedData, String iv) {
        byte[] result;
        BASE64Decoder decoder = new BASE64Decoder();

        try {
            Key key = new SecretKeySpec(decoder.decodeBuffer(sessionKey), "AES");
            AlgorithmParameters algorithm = AlgorithmParameters.getInstance("AES");
            algorithm.init(new IvParameterSpec(decoder.decodeBuffer(iv)));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, algorithm);
            result = cipher.doFinal(decoder.decodeBuffer(encryptedData));
        } catch (Exception e) {
            log.error("decrypt fail: ", e);
            throw new BusinessException("[Occurring an exception during data decrypting!]", e);
        }

        return new String(result);
    }
}