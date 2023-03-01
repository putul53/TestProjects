package com.putul.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.putul.myapplication.activities.auth.LoginActivity;
import com.putul.myapplication.database.accounts.User;


public class SharedPrefManager {

    private static final String SharedPrefName="SHARED_PREF_MANAGER";
    private static final String KeyPassword="PASSWORD";
    private static final String KeyUSERID="USERID";
    private static final String KeyName="NAME";
    private static final String KeyEMAIL="EMAIL";
    private static final String KeyUserIsLoggedIn="IS_USER_LOGGED_IN";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void Login(User user)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KeyUSERID,user.getUserId());
        editor.putString(KeyEMAIL,user.getEmail());
        editor.putString(KeyName,user.getName());
        editor.putString(KeyPassword,user.getPassword());
        editor.putString(KeyUserIsLoggedIn,user.getUserIslogged_in());
        editor.apply();
    }


    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyUSERID, "") != "";
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KeyUSERID, ""),
                sharedPreferences.getString(KeyEMAIL, null),
                sharedPreferences.getString(KeyName, null),
                sharedPreferences.getString(KeyPassword, null),
                sharedPreferences.getString(KeyUserIsLoggedIn, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Intent i=new Intent(mCtx, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(i);
        ((Activity)mCtx).finish();
    }
    
}
