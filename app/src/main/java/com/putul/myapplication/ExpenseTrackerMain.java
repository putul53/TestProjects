package com.putul.myapplication;

import android.app.Application;
import android.content.Context;

import com.putul.myapplication.database.AppDatabase;
import com.putul.myapplication.database.DatabaseInstance;

public class ExpenseTrackerMain extends Application {
   public static Context appMainContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appMainContext= getApplicationContext();
    }

    private static Context getAppContext() {
        return appMainContext;
    }

    public static AppDatabase getDatabaseInstance(Context context)  {
        if(null== getAppContext()) {
            appMainContext= context;
        }
        return DatabaseInstance.getInstance(context).getAppDatabase();
    }
}
