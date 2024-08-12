package cn.chengzhiya.mhdfverify;

import cn.chengzhiya.mhdfverify.util.ConfigUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import static cn.chengzhiya.mhdfverify.util.ConfigUtil.saveDefaultConfig;
import static cn.chengzhiya.mhdfverify.util.DatabaseUtil.connectDatabase;
import static cn.chengzhiya.mhdfverify.util.DatabaseUtil.initDatabase;
import static cn.chengzhiya.mhdfverify.util.RSAUtil.initRSAKeyPair;

@SpringBootApplication
@EnableScheduling
@EnableWebSocket
public class Application {
    public static void main(String[] args) throws Exception {
        initRSAKeyPair();
        saveDefaultConfig();
        connectDatabase(
                ConfigUtil.getConfig().getString("DatabaseSettings.Host"),
                ConfigUtil.getConfig().getString("DatabaseSettings.Database"),
                ConfigUtil.getConfig().getString("DatabaseSettings.User"),
                ConfigUtil.getConfig().getString("DatabaseSettings.Password")
        );
        initDatabase();
        SpringApplication.run(Application.class, args);

    }
}
