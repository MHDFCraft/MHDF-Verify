package cn.chengzhiya.mhdfverify.utils;

import cn.chengzhiya.mhdfverify.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cn.chengzhiya.mhdfverify.utils.DatabaseUtil.dataSource;

public final class UserUtil {
    public static User getUser(String userName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdfverify_user where User = ?")) {
                ps.setString(1, userName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new User(rs.getInt("ID"), userName, rs.getString("Password"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Integer> getUserPlugins(String userName) {
        User user = getUser(userName);
        if (user != null) {
            List<Integer> pluginList = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM mhdfverify_buy where User = ?")) {
                    ps.setInt(1, user.getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            pluginList.add(rs.getInt("Plugin"));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return pluginList;
        } else {
            return new ArrayList<>();
        }
    }
}
