package cn.telecom.commons.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface AESUtils {

    String AES_ALGORITHM = "AES";

    String CIPHER_PADDING = "/AES/ECB/PKCS5Padding";

    String CIPHER_CBC_PADDING = "/AES/CBC/PKCS5Padding";

    static String encrypt(String content, String aesKey) {
        byte[] aesKeyBytes = aesKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(aesKeyBytes, AES_ALGORITHM);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] bytes = cipher.doFinal(content.getBytes());
            byte[] encode = Base64.getEncoder().encode(bytes);
            return new String(encode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    static String decrypt(String content, String aesKey) {
        byte[] aesKeyBytes = aesKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(aesKeyBytes, AES_ALGORITHM);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decode = Base64.getDecoder().decode(content.getBytes());
            byte[] bytes = cipher.doFinal(decode);
            return new String(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
