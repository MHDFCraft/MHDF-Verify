package cn.chengzhiya.mhdfverify.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class Sha256Util {
    public static String sha256(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuffer = new StringBuilder();
            for (final byte aByte : messageDigest.digest()) {
                final String temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(temp);
            }
            return stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
