package cn.chengzhiya.mhdfverify;

import cn.chengzhiya.mhdfverify.utils.ConfigUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static cn.chengzhiya.mhdfverify.utils.ConfigUtil.saveDefaultConfig;
import static cn.chengzhiya.mhdfverify.utils.DatabaseUtil.connectDatabase;
import static cn.chengzhiya.mhdfverify.utils.DatabaseUtil.initDatabase;
import static cn.chengzhiya.mhdfverify.utils.RSAUtil.initRSAKeyPair;

@SpringBootApplication
@EnableScheduling
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
