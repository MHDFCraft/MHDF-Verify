package cn.chengzhiya.mhdfverify.utils;

import cn.chengzhiya.mhdfverify.entity.Plugin;
import cn.chengzhiya.mhdfverify.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cn.chengzhiya.mhdfverify.utils.DatabaseUtil.dataSource;

public final class PluginUtil {
    public static Plugin getPlugin(String pluginName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdfverify_plugin WHERE PluginName = ?")) {
                ps.setString(1, pluginName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Plugin(rs.getInt("ID"), pluginName, rs.getString("PluginVersion"), rs.getInt("MaxClient"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Plugin getPlugin(Integer pluginID) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdfverify_plugin WHERE ID = ?")) {
                ps.setInt(1, pluginID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Plugin(rs.getInt("ID"), rs.getString("PluginName"), rs.getString("PluginVersion"), rs.getInt("MaxClient"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean verifyPlugin(User user, Plugin plugin) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdfverify_buy WHERE User = ? AND Plugin = ?")) {
                ps.setInt(1, user.getId());
                ps.setInt(2, plugin.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
