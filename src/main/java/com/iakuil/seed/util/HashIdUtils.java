package com.iakuil.seed.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.hashids.Hashids;

import java.util.Objects;

@UtilityClass
public class HashIdUtils {
    private static final String DEFAULT_SALT = "itsADemo4Hash";
    private static final int DEFAULT_HASH_LENGTH = 8;

    public static String encrypt(Long plainid) {
        return encrypt(plainid, null);
    }

    public static String encrypt(Long plainid, String salt) {
        Objects.requireNonNull(plainid, "Id must not be empty!");
        Hashids hashids = new Hashids(ObjectUtils.defaultIfNull(salt, DEFAULT_SALT), DEFAULT_HASH_LENGTH);
        return hashids.encode(plainid);
    }

    public static Long decrypt(String ciphertext) {
        return decrypt(ciphertext, null);
    }

    public static Long decrypt(String ciphertext, String salt) {
        Objects.requireNonNull(ciphertext, "Text must not be empty!");
        Hashids hashids = new Hashids(ObjectUtils.defaultIfNull(salt, DEFAULT_SALT), DEFAULT_HASH_LENGTH);
        long[] decoded = hashids.decode(ciphertext);
        return decoded.length > 0 ? decoded[0] : null;
    }
}