package cn.chengzhiya.example.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class RSAUtil {
    @Getter
    private static PublicKey publicKey;

    public static void updatePublicKey() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://" + VerifyUtil.getServerURL() + "/rsa/getPublic").openConnection();

        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JSONObject Data = JSON.parseObject(in.readLine());

        in.close();
        conn.disconnect();

        byte[] decodedPublicKey = Base64Util.decode(Data.getString("data"));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(keySpec);
    }

    public static byte[] encode(byte[] bytes) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        return decryptCipher.doFinal(bytes);
    }

    public static byte[] encode(String string) throws Exception {
        return encode(string.getBytes(StandardCharsets.UTF_8));
    }
}
