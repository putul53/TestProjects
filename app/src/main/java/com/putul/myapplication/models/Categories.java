package com.putul.myapplication.models;

import java.util.ArrayList;

public class Categories {
    String status;
    ArrayList<String> data = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
