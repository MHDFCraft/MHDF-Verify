package cn.chengzhiya.mhdfverify.entity;

import lombok.Data;

@Data
public final class User {
    int id;
    String userName;
    String password;

    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
}
