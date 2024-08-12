package cn.chengzhiya.example;

import cn.chengzhiya.example.task.HeartBeat;
import cn.chengzhiya.example.util.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.chengzhiya.example.util.ConfigUtil.createDir;
import static cn.chengzhiya.example.util.LogUtil.colorLog;
import static cn.chengzhiya.example.util.RSAUtil.updatePublicKey;
import static cn.chengzhiya.example.util.VerifyUtil.connectWebsocketServer;
import static cn.chengzhiya.example.util.VerifyUtil.verify;

public final class Example extends JavaPlugin {
    public static Example instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        createDir(getDataFolder());
        ConfigUtil.saveResource(getDataFolder().getPath(), "config.yml", "config.yml", false);
        reloadConfig();

        try {
            updatePublicKey();
            if (verify(getConfig().getString("VerifySettings.User"), getConfig().getString("VerifySettings.Password"))) {
                connectWebsocketServer();
                new HeartBeat().runTaskTimerAsynchronously(this, 0L, 20L);
                colorLog("&a插件验证成功,感谢您的购买!");
            } else {
                colorLog("&a插件验证失败,请查阅https://github.com/Love-MHDF/MHDF-Verify");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;

    }
}
