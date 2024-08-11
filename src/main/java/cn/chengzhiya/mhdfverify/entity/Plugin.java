package cn.chengzhiya.mhdfverify.entity;

import lombok.Data;

@Data
public final class Plugin {
    int id;
    String pluginName;
    String pluginVersion;

    public Plugin(int id, String pluginName, String pluginVersion) {
        this.id = id;
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
    }
}
