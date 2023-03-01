package com.putul.myapplication.database.accounts;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AccountQuery {

    @Insert
    void insert(User model);

    @Query("select * from account where email = :email and password = :password")
    User getUserData(String email, String password);
}
