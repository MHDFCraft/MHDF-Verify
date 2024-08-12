package cn.chengzhiya.example.util;

import cn.chengzhiya.example.Example;

public final class SpigotUtil {
    public static String getPluginName() {
        return Example.instance.getDescription().getName();
    }
}
