package model.user;

import java.util.List;

import model.Entity;

/**
 * 该类为系统用户类
 * Created by sunshine on 4/23/16.
 */
public class User extends Entity {
    private String userId;

    /* 用户名 */
    private String username;

    /* 密码 */
    private String password;

    /* 角色 */
    private List<Role> role;

    public User() {
        super();
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
