package com.putul.myapplication.database.accounts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private String userIslogged_in;
    private String password;
    String userId;

    public User(String userId, String email, String name,String password, String userIslogged_in) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.userIslogged_in = userIslogged_in;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserIslogged_in() {
        return userIslogged_in;
    }

    public void setUserIslogged_in(String userIslogged_in) {
        this.userIslogged_in = userIslogged_in;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
