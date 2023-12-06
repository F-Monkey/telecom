package cn.telecom.commons.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface EncryptUtil {
    BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    static String encrypt(String origin) {
        return PASSWORD_ENCODER.encode(origin);
    }

    static boolean match(String password, String encodedPassword) {
        return PASSWORD_ENCODER.matches(password, encodedPassword);
    }
}
