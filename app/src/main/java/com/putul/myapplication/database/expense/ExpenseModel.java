package com.putul.myapplication.database.expense;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense")
public class ExpenseModel {
    // id for each course.
    @PrimaryKey(autoGenerate = true)
    private int id;

    String totalExpenseOfTheMonth;
    String dateAndTime;
    String comment;
    String type;
    String expenseFor;
    String totalGoalExpense;
    String currentMonth;
    String email;

    public ExpenseModel(String totalExpenseOfTheMonth, String dateAndTime,String currentMonth, String comment, String type, String expenseFor, String totalGoalExpense, String email) {
        this.totalExpenseOfTheMonth = totalExpenseOfTheMonth;
        this.dateAndTime = dateAndTime;
        this.currentMonth = currentMonth;
        this.comment = comment;
        this.type = type;
        this.expenseFor = expenseFor;
        this.totalGoalExpense = totalGoalExpense;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getTotalGoalExpense() {
        return totalGoalExpense;
    }

    public void setTotalGoalExpense(String totalGoalExpense) {
        this.totalGoalExpense = totalGoalExpense;
    }

    public String getExpenseFor() {
        return expenseFor;
    }

    public void setExpenseFor(String expenseFor) {
        this.expenseFor = expenseFor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotalExpenseOfTheMonth() {
        return totalExpenseOfTheMonth;
    }

    public void setTotalExpenseOfTheMonth(String totalExpenseOfTheMonth) {
        this.totalExpenseOfTheMonth = totalExpenseOfTheMonth;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
