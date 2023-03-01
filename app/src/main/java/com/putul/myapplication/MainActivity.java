package com.putul.myapplication;

import static com.putul.myapplication.ExpenseTrackerMain.appMainContext;
import static com.putul.myapplication.utils.StaticFunctions.ToastFunction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.putul.myapplication.adapters.CategoriesAdapter;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.database.expense.ExpenseModel;
import com.putul.myapplication.databinding.ActivityMainBinding;
import com.putul.myapplication.fragments.ExpenseSettingsFragment;
import com.putul.myapplication.fragments.HomeFragment;
import com.putul.myapplication.fragments.ProgressFragment;
import com.putul.myapplication.utils.DatePickerFragment;
import com.putul.myapplication.utils.SharedPrefManager;
import com.putul.myapplication.utils.StaticFunctions;
import com.putul.myapplication.utils.UtilsForAsset;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, CategoriesAdapter.OnCatSelectedListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = MainActivity.this;

        setSupportActionBar(binding.toolbar);
        loadFragment(new HomeFragment());

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnItemSelectedListener(this);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatDialog(context);
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to logout ?") .setTitle("Confirm Logout");

        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPrefManager.getInstance(ExpenseTrackerMain.appMainContext).logout();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Confirm Logout");
        alert.show();
    }

    ArrayList<String> list=new ArrayList<>();
    Dialog dialog;
    public void showCatDialog(final Context context) {
        dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_cat);
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


        list = fetchListFromJSON("expenses_cat_list.json");

        RecyclerView recyclerView=dialog.findViewById(R.id.catRecyclerView);
        LinearLayout expenseHolder = dialog.findViewById(R.id.expenseHolder);
        LinearLayout incomeHolder = dialog.findViewById(R.id.incomeHolder);

        setCatAdapter(recyclerView,"Expense");
        expenseHolder.setSelected(true);
        incomeHolder.setSelected(false);


        expenseHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list = fetchListFromJSON("expenses_cat_list.json");
                setCatAdapter(recyclerView,"Expense");
                expenseHolder.setSelected(true);
                incomeHolder.setSelected(false);
            }
        });

        incomeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list = fetchListFromJSON("income_list_cat.json");
                setCatAdapter(recyclerView,"Income");
                expenseHolder.setSelected(false);
                incomeHolder.setSelected(true);
            }
        });
    }

    private void setCatAdapter(RecyclerView recyclerView, String purpose) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(list, context, purpose,this);
        recyclerView.setAdapter(categoriesAdapter);
    }

    private ArrayList<String> fetchListFromJSON(String s) {
        ArrayList<String> list=new ArrayList<>();
        String response = UtilsForAsset.getJsonFromAssets(context, s);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0;i<jsonArray.length();i++){
                list.add(jsonArray.getString(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;

            case R.id.progress_menu:
                fragment = new ProgressFragment();
                break;

            case R.id.settings_menu:
                fragment = new ExpenseSettingsFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public void onCatSelected(String data, String purpose) {
        if(dialog!=null){
            dialog.dismiss();
        }
        showAddExpensePopup(data, purpose);
    }

    String selectedDate="";
    String selectedMonth="";
    TextView selectedDateText;
    private void showAddExpensePopup(String data, String purpose) {
        Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_expense_income);
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
        TextInputEditText expenseIncomeEdit=dialog.findViewById(R.id.expenseIncomeEdit);
        TextInputEditText commentEdit=dialog.findViewById(R.id.commentEdit);
        TextView titleCat = dialog.findViewById(R.id.titleCat);
        selectedDateText=dialog.findViewById(R.id.selectedDateText);
        LinearLayout dateHolder = dialog.findViewById(R.id.dateHolder);
        LinearLayout cancelHolder = dialog.findViewById(R.id.cancelHolder);
        LinearLayout doneHolder = dialog.findViewById(R.id.doneHolder);

        titleCat.setText("Adding "+purpose+" for: "+data);
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
                String inputExpense = expenseIncomeEdit.getText().toString();
                String inputComment = commentEdit.getText().toString();
                if(TextUtils.isEmpty(inputExpense)){
                    ToastFunction(context, "Please enter total expense");
                }
                else if(TextUtils.isEmpty(selectedDate) || selectedDate.equals("")){
                    ToastFunction(context, "Please select date");
                }
                else{
                    if(inputComment==null){
                        inputComment = "";
                    }
                    User user = SharedPrefManager.getInstance(context).getUser();
                    ExpenseModel expenseModel = new ExpenseModel(inputExpense, selectedDate, selectedMonth, inputComment, purpose, data,"0", user.getEmail());
                    insertDataIntoDatabase(expenseModel);
                }
            }
        });

        dateHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = "";
                selectedMonth="";
                showDatePicker();
            }
        });
    }

    private void insertDataIntoDatabase(ExpenseModel expenseModel) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ExpenseTrackerMain.getDatabaseInstance(context).expenseQuery().insert(expenseModel);
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        loadFragment(new HomeFragment());
                    }
                });
            }
        });
    }

    private void showDatePicker( ) {
        DatePickerFragment date = new DatePickerFragment();
        showDateFragment(date);
        date.setCallBack(ondate);
    }

    private void showDateFragment(DatePickerFragment date) {
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        //date.setMinDate(System.currentTimeMillis() - 1000);
        calender.add(Calendar.DATE, 0);

        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            int currentmonth=monthOfYear+1;
            String monthString=String.valueOf(currentmonth);
            String yearString=String.valueOf(year);
            String dayString=String.valueOf(dayOfMonth);

            if(String.valueOf(currentmonth).length()==1)
            {
                monthString="0"+monthString;
            }
            if(dayString.length()==1)
            {
                dayString="0"+dayString;
            }

            selectedDate=yearString+"-"+monthString+"-"+dayString ;
            selectedMonth = monthString;
            if (selectedDateText != null)
                selectedDateText.setText(selectedDate);
        }
    };

}