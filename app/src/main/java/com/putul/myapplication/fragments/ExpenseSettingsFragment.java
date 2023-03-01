package com.putul.myapplication.fragments;

import static com.putul.myapplication.utils.StaticFunctions.ToastFunction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.putul.myapplication.ExpenseTrackerMain;
import com.putul.myapplication.R;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.database.expense.ExpenseModel;
import com.putul.myapplication.utils.SharedPrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

public class ExpenseSettingsFragment extends Fragment {


    LinearLayout linearTotalGoalEditHolder;
    TextView totalText;
    TextView leftText, leftLabel, expenseText, incomeText;
    ProgressBar progress;
    Context context;

    public ExpenseSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_expense_settings, container, false);
        context = getActivity();
        initM(v);
        getAllDataFromDB();
        onClicks();
        return v;
    }

    private void onClicks() {
        linearTotalGoalEditHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddGoalDialog();
            }
        });
    }

    private void getAllDataFromDB() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ExpenseModel> expenseModelArrayList = (ArrayList<ExpenseModel>) ExpenseTrackerMain.getDatabaseInstance(context).expenseQuery().getAllData();

                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        String totalSpendGoal = "0";
                        Double totalExpense = 0.0;
                        Double totalIncome = 0.0;

                        if(expenseModelArrayList!=null && expenseModelArrayList.size()>0){
                            if(expenseModelArrayList.get(0).getTotalGoalExpense()!=null && !expenseModelArrayList.get(0).getTotalGoalExpense().equals("")){
                                totalSpendGoal = expenseModelArrayList.get(0).getTotalGoalExpense();
                            }
                        }
                        if(expenseModelArrayList.size()>1){
                            for(int i =1; i<expenseModelArrayList.size();i++){
                                if(expenseModelArrayList.get(i).getCurrentMonth()!=null && !expenseModelArrayList.get(i).getCurrentMonth().equals("")){
                                    int month = Integer.parseInt(expenseModelArrayList.get(i).getCurrentMonth());
                                    int currentMonth = Integer.parseInt(expenseModelArrayList.get(0).getCurrentMonth());
                                    if(month==currentMonth){
                                        if(expenseModelArrayList.get(i).getType().equalsIgnoreCase("expense")){
                                            if(expenseModelArrayList.get(i).getTotalExpenseOfTheMonth()!=null && !expenseModelArrayList.get(i).getTotalGoalExpense().equalsIgnoreCase("")){
                                                totalExpense = totalExpense + Double.parseDouble(expenseModelArrayList.get(i).getTotalExpenseOfTheMonth());
                                            }
                                        }

                                        if(expenseModelArrayList.get(i).getType().equalsIgnoreCase("income")){
                                            if(expenseModelArrayList.get(i).getTotalExpenseOfTheMonth()!=null && !expenseModelArrayList.get(i).getTotalGoalExpense().equalsIgnoreCase("")){
                                                totalIncome = totalIncome + Double.parseDouble(expenseModelArrayList.get(i).getTotalExpenseOfTheMonth());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        setHeaderData(totalSpendGoal, totalExpense, totalIncome);
                    }
                });
            }
        });
    }

    private void setHeaderData(String totalSpendGoal, Double totalExpense, Double totalIncome) {
        totalText.setText(""+totalSpendGoal);
        if(totalSpendGoal.equals("0")){
            leftLabel.setVisibility(View.GONE);
            leftText.setText("Set this Month Goal");
            progress.setProgress(0);
        }
        else{
            leftLabel.setVisibility(View.VISIBLE);
            if(totalSpendGoal!=null && !totalSpendGoal.trim().equals("")){
                Double totalGoal = Double.parseDouble(totalSpendGoal);
                totalGoal = totalGoal - totalExpense;
                leftText.setText(""+totalGoal);

                //set progressbar
                int percentage = (int) ((totalExpense/totalGoal)*100);
                progress.setProgress(percentage);
            }
            else{
                leftText.setText(""+totalSpendGoal);
                progress.setProgress(0);
            }
        }
        incomeText.setText(""+totalIncome);
        expenseText.setText(""+totalExpense);


    }

    private void initM(View v) {
        linearTotalGoalEditHolder=v.findViewById(R.id.linearTotalGoalEditHolder);
        totalText=v.findViewById(R.id.totalText);
        leftText=v.findViewById(R.id.leftText);
        leftLabel=v.findViewById(R.id.leftLabel);
        expenseText=v.findViewById(R.id.expenseText);
        incomeText=v.findViewById(R.id.incomeText);
        progress=v.findViewById(R.id.progress);
    }

    private void showAddGoalDialog() {
        Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_edit_monthly_goal);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        //set data
        TextInputEditText totalGoalEdit=dialog.findViewById(R.id.totalGoalEdit);
        LinearLayout cancelHolder = dialog.findViewById(R.id.cancelHolder);
        LinearLayout doneHolder = dialog.findViewById(R.id.doneHolder);

        cancelHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doneHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String totalGoal = totalGoalEdit.getText().toString();
                if(TextUtils.isEmpty(totalGoal)){
                    ToastFunction(context, "Please enter total goal expense for the month");
                }
                else{
                    User user = SharedPrefManager.getInstance(context).getUser();
                    String email = user.getEmail();
                    String currentMonth = getCurrentMonth();
                    updateDataIntoDatabase(totalGoal, email, currentMonth);
                }
            }
        });
    }
    private String getCurrentMonth() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM");
            Date date = new Date();
            return dateFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private void updateDataIntoDatabase(String totalGoal, String email, String currentMonth) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ExpenseTrackerMain.getDatabaseInstance(context).expenseQuery().updateAnItem(totalGoal, currentMonth, email);
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        loadFragment(new HomeFragment());
                    }
                });
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}