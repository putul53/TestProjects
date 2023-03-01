package com.putul.myapplication.database.expense;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface ExpenseQuery {

    @Insert
    void insert(ExpenseModel model);

    @Query("select * from expense")
    List<ExpenseModel> getAllData();

    @Query("UPDATE expense SET totalGoalExpense = :totalGoalExpense WHERE email = :email AND currentMonth = :currentMonth")
    void updateAnItem(String totalGoalExpense,String currentMonth,String email);
}
