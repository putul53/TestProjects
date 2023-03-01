package com.putul.myapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.putul.myapplication.ExpenseTrackerMain;
import com.putul.myapplication.R;
import com.putul.myapplication.adapters.ExpenseAdapter;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.database.expense.ExpenseModel;
import com.putul.myapplication.utils.SharedPrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ExpenseModel> arrayList = new ArrayList<>();
    Context context;

    RelativeLayout noDataHolder;
    TextView leftText, leftLabel, expenseText, incomeText;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        initM(v);
        User user = SharedPrefManager.getInstance(context).getUser();
        String email = user.getEmail();
        getAllDataFromDB(email);
        return v;
    }

    private void getAllDataFromDB(String email) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ExpenseModel> expenseModelArrayList = (ArrayList<ExpenseModel>) ExpenseTrackerMain.getDatabaseInstance(context).expenseQuery().getAllData();
                if(expenseModelArrayList.size()==0){
                    String month = getCurrentMonth();
                    String dateAndTime = getCurrentDate();
                    ExpenseModel expenseModel = new ExpenseModel("", dateAndTime, month,"", "", "","0", email);
                    ExpenseTrackerMain.getDatabaseInstance(context).expenseQuery().insert(expenseModel);
                }

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
                            noDataHolder.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for(int i =1; i<expenseModelArrayList.size();i++){
                                if(expenseModelArrayList.get(i).getCurrentMonth()!=null && !expenseModelArrayList.get(i).getCurrentMonth().equals("")){
                                    int month = Integer.parseInt(expenseModelArrayList.get(i).getCurrentMonth());
                                    int currentMonth = Integer.parseInt(expenseModelArrayList.get(0).getCurrentMonth());
                                    if(month==currentMonth){
                                        arrayList.add(expenseModelArrayList.get(i));
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
                        else{
                            noDataHolder.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        setHeaderData(totalSpendGoal, totalExpense, totalIncome);
                        setExpenseAdapter();
                    }
                });
            }
        });
    }

    private void setHeaderData(String totalSpendGoal, Double totalExpense, Double totalIncome) {
        if(totalSpendGoal.equals("0")){
            leftLabel.setVisibility(View.GONE);
            leftText.setText("Set this Month Goal");
        }
        else{
            if(totalSpendGoal!=null && !totalSpendGoal.trim().equals("")){
                Double totalGoal = Double.parseDouble(totalSpendGoal);
                totalGoal = totalGoal - totalExpense;
                leftText.setText(""+totalGoal);
            }
            else{
                leftText.setText(""+totalSpendGoal);
            }
            leftLabel.setVisibility(View.VISIBLE);

        }
        incomeText.setText(""+totalIncome);
        expenseText.setText(""+totalExpense);
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

    private String getCurrentDate() {
        try {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
            String formattedDate = df.format(c);
            return formattedDate;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private void initM(View v) {
        recyclerView=v.findViewById(R.id.expenseRecyclerView);
        noDataHolder=v.findViewById(R.id.noDataHolder);
        leftText=v.findViewById(R.id.leftText);
        leftLabel=v.findViewById(R.id.leftLabel);
        expenseText=v.findViewById(R.id.expenseText);
        incomeText=v.findViewById(R.id.incomeText);
    }

    private void setExpenseAdapter() {
        if(arrayList.size()>0){
            Collections.reverse(arrayList);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        ExpenseAdapter adapter = new ExpenseAdapter(arrayList, context);
        recyclerView.setAdapter(adapter);
    }
}