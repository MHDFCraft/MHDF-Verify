package cn.chengzhiya.mhdfverify.util;

import lombok.Getter;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;

public final class RSAUtil {
    @Getter
    private static KeyPair KeyPair;

    public static void initRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(2048);
        KeyPair = rsa.generateKeyPair();
    }

    public static PrivateKey getPrivateKey() {
        return getKeyPair().getPrivate();
    }

    public static PublicKey getPublicKey() {
        return getKeyPair().getPublic();
    }

    public static byte[] encode(byte[] bytes) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.ENCRYPT_MODE, getKeyPair().getPublic());
        return decryptCipher.doFinal(bytes);
    }

    public static byte[] encode(String string) throws Exception {
        return encode(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(byte[] bytes) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, getKeyPair().getPrivate());
        return new String(decryptCipher.doFinal(bytes));
    }

    public static String decode(String string) throws Exception {
        return decode(string.getBytes(StandardCharsets.UTF_8));
    }
}
