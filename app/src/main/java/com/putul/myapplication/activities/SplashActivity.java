package com.putul.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.putul.myapplication.MainActivity;
import com.putul.myapplication.R;
import com.putul.myapplication.activities.auth.LoginActivity;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.utils.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context=SplashActivity.this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    private void gotoActivity() {
        User user = SharedPrefManager.getInstance(context).getUser();

        Intent i;
        if(user!=null && user.getUserIslogged_in()!=null && user.getUserIslogged_in().equals("true")){
            i = new Intent(getApplicationContext(), MainActivity.class);
        }
        else{
            i = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(i);
        finish();

    }
}