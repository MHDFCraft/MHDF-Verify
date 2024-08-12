package cn.chengzhiya.mhdfverify.entity;

import lombok.Data;

@Data
public final class Plugin {
    int id;
    String pluginName;
    String pluginVersion;
    int maxClient;

    public Plugin(int id, String pluginName, String pluginVersion, int maxClient) {
        this.id = id;
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.maxClient = maxClient;
    }
}
