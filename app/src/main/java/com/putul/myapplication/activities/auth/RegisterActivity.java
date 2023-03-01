package com.putul.myapplication.activities.auth;

import static com.putul.myapplication.ExpenseTrackerMain.appMainContext;
import static com.putul.myapplication.utils.StaticFunctions.ToastFunction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.putul.myapplication.ExpenseTrackerMain;
import com.putul.myapplication.MainActivity;
import com.putul.myapplication.R;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.utils.SharedPrefManager;
import com.putul.myapplication.utils.StaticFunctions;

import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    Button createAccount;
    TextInputEditText fullName,emailAddress,password;
    TextView gotoLogin;
    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        activity = (Activity)context;

        initM();
        setOnClickListeners();

    }

    private void initM() {
        createAccount=findViewById(R.id.createAccount);
        fullName=findViewById(R.id.fullname);
        emailAddress=findViewById(R.id.emailAdddress);
        password=findViewById(R.id.password);
        gotoLogin=findViewById(R.id.gotoLoginText);
    }

    private void setOnClickListeners() {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNow();

            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void registerNow() {

        final String passwordString = password.getText().toString();

        final String username = fullName.getText().toString();
        final String userEmail = emailAddress.getText().toString();

        if (TextUtils.isEmpty(username)) {
            fullName.setError("Please enter  name");
            fullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            emailAddress.setError("Please enter userEmail");
            emailAddress.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(passwordString)) {
            password.setError("Enter your password");
            password.requestFocus();
            return;
        }

        else {
            StaticFunctions.showDialog(activity);
            insertDataIntoDatabase(new User(userEmail, userEmail, username , passwordString, "true"));
        }
    }

    private void insertDataIntoDatabase(User user) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ExpenseTrackerMain.getDatabaseInstance(context).accountQuery().insert(user);

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        StaticFunctions.hideDialog(activity);
                        if(user!=null){
                            SharedPrefManager.getInstance(appMainContext).Login(user);

                            ToastFunction(context, "Successfully Registered");
                            Intent i = new Intent(appMainContext, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            ToastFunction(appMainContext, "Register again");
                        }
                    }
                });

            }
        });
    }
}