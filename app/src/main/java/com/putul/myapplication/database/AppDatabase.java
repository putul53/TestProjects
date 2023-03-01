package com.putul.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.putul.myapplication.database.accounts.AccountQuery;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.database.expense.ExpenseModel;
import com.putul.myapplication.database.expense.ExpenseQuery;

@Database(entities = {ExpenseModel.class, User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExpenseQuery expenseQuery();
    public abstract AccountQuery accountQuery();
}
