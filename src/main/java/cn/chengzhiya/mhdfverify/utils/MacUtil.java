package cn.chengzhiya.mhdfverify.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;

public final class MacUtil {
    public static String getMACAddress(InetAddress inetAddress) throws Exception {
        byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                stringBuilder.append("-");
            }
            String s = Integer.toHexString(mac[i] & 0xFF);
            stringBuilder.append(s.length() == 1 ? 0 + s : s);
        }
        return stringBuilder.toString().toUpperCase();
    }
}
