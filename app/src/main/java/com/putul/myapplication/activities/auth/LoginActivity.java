package com.putul.myapplication.activities.auth;

import static com.putul.myapplication.ExpenseTrackerMain.appMainContext;
import static com.putul.myapplication.utils.StaticFunctions.ToastFunction;
import static com.putul.myapplication.utils.StaticFunctions.hideDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.putul.myapplication.ExpenseTrackerMain;
import com.putul.myapplication.MainActivity;
import com.putul.myapplication.R;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.utils.SharedPrefManager;
import com.putul.myapplication.utils.StaticFunctions;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    ImageView backButton;
    TextView registrationBtn;
    Button loginAccount;

    TextInputEditText emailEdit,passwordEdit;
    TextView loginerrorText;
    Context context;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        activity = (Activity)context;
        initM();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticFunctions.showDialog(activity);
                loginNow();
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void initM() {
        loginAccount=findViewById(R.id.loginAccount);
        registrationBtn=findViewById(R.id.registrationBtn);

        emailEdit=findViewById(R.id.emailEdit);
        passwordEdit=findViewById(R.id.passEdit);
    }

    private void loginNow() {

        final String passwordString = passwordEdit.getText().toString();
        final String userEmail = emailEdit.getText().toString();

        if (TextUtils.isEmpty(userEmail)) {
            emailEdit.setError("Please enter your Email");
            emailEdit.requestFocus();
            hideDialog(activity);
            return;
        } else if (TextUtils.isEmpty(passwordString)) {
            passwordEdit.setError("Enter your password");
            passwordEdit.requestFocus();
            hideDialog(activity);
            return;
        } else {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    User userData = ExpenseTrackerMain.getDatabaseInstance(context).accountQuery().getUserData(userEmail, passwordString);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            StaticFunctions.hideDialog(activity);
                            if(userData!=null){
                                if(userData.getEmail().equals(userEmail) && userData.getPassword().equals(passwordString)){
                                    User user=new User(userEmail,userEmail,userData.getName(),passwordString, "true");
                                    SharedPrefManager.getInstance(appMainContext).Login(user);

                                    ToastFunction(context, "Successfully Login");
                                    Intent i = new Intent(appMainContext, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    ToastFunction(appMainContext, "Wrong credentials");
                                }
                            }
                            else{
                                ToastFunction(appMainContext, "No user found");
                            }
                        }
                    });
                }
            });
        }
    }
}