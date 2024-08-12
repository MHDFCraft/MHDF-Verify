package cn.chengzhiya.mhdfverify.util;

import cn.chengzhiya.mhdfverify.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cn.chengzhiya.mhdfverify.util.DatabaseUtil.dataSource;

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
}
