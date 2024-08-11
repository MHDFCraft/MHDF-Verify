package cn.chengzhiya.mhdfverify.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static cn.chengzhiya.mhdfverify.utils.DatabaseUtil.dataSource;

@Slf4j
public final class LogUtil {
    public static void log(String user, String plugin, String ip, String msg) {
        log.info("user: {} | plugin: {} | ip: {} | msg: {}", user, plugin, ip, msg);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO mhdfverify_log (User, Plugin, IP, Log) VALUES (?, ?, ?, ?)")) {
                ps.setObject(1, user);
                ps.setObject(2, plugin);
                ps.setObject(3, ip);
                ps.setObject(4, msg);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
