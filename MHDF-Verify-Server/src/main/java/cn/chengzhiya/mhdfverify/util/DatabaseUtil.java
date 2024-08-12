package cn.chengzhiya.mhdfverify.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TimeZone;

public final class DatabaseUtil {
    public static HikariDataSource dataSource;

    public static void connectDatabase(String host, String database, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + "/" + database + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public static void initDatabase() throws SQLException {
        runSQLUpdate("CREATE TABLE IF NOT EXISTS mhdfverify_plugin" +
                "(" +
                "    `ID` BIGINT NOT NULL AUTO_INCREMENT," +
                "    `PluginName` VARCHAR(100) NOT NULL DEFAULT ''," +
                "    `PluginVersion` VARCHAR(50) NOT NULL DEFAULT '1.0.0'," +
                "    `MaxClient` BIGINT NOT NULL DEFAULT -1," +
                "    PRIMARY KEY (`ID`)," +
                "    INDEX `PluginName` (`PluginName`)" +
                ")" +
                "COLLATE=utf8mb4_bin;");

        runSQLUpdate("CREATE TABLE IF NOT EXISTS mhdfverify_user" +
                "(" +
                "    `ID` BIGINT NOT NULL AUTO_INCREMENT," +
                "    `User` VARCHAR(100) NOT NULL DEFAULT ''," +
                "    `Password` VARCHAR(100) NOT NULL DEFAULT ''," +
                "    PRIMARY KEY (`ID`)," +
                "    INDEX `User` (`User`)" +
                ")" +
                "COLLATE=utf8mb4_bin;");

        runSQLUpdate("CREATE TABLE IF NOT EXISTS mhdfverify_buy" +
                "(" +
                "    `ID` BIGINT NOT NULL AUTO_INCREMENT," +
                "    `User` BIGINT NOT NULL DEFAULT 1," +
                "    `Plugin` BIGINT NOT NULL DEFAULT 1," +
                "    PRIMARY KEY (`ID`)," +
                "    INDEX `User` (`User`)" +
                ")" +
                "COLLATE=utf8mb4_bin;");

        runSQLUpdate("CREATE TABLE IF NOT EXISTS mhdfverify_log" +
                "(" +
                "    `ID` BIGINT NOT NULL AUTO_INCREMENT," +
                "    `User` VARCHAR(100) NOT NULL DEFAULT ''," +
                "    `Plugin` VARCHAR(100) NOT NULL DEFAULT ''," +
                "    `IP` VARCHAR(100) NOT NULL DEFAULT ''," +
                "    `Log` LONGTEXT  NOT NULL DEFAULT ''," +
                "    PRIMARY KEY (`ID`)," +
                "    INDEX `User` (`User`)" +
                ")" +
                "COLLATE=utf8mb4_bin;");
    }

    public static void runSQLUpdate(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.executeUpdate();
            }
        }
    }
}
