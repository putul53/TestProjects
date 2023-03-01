package com.putul.myapplication.fragments;

import static com.putul.myapplication.utils.StaticFunctions.ToastFunction;
import static com.putul.myapplication.utils.StaticFunctions.getCurrentDateAndTimeInMillis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.putul.myapplication.ExpenseTrackerMain;
import com.putul.myapplication.R;
import com.putul.myapplication.adapters.ExpenseAdapter;
import com.putul.myapplication.database.accounts.User;
import com.putul.myapplication.database.expense.ExpenseModel;
import com.putul.myapplication.utils.SharedPrefManager;
import com.putul.myapplication.utils.StaticFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;

public class ProgressFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ExpenseModel> arrayList = new ArrayList<>();
    Context context;

    RelativeLayout noDataHolder;
    LinearLayout expenseHolder, incomeHolder;
    String email;
    TextView generateReport;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        context = getActivity();
        User user = SharedPrefManager.getInstance(context).getUser();
        email = user.getEmail();
        initM(v);

        getAllDataFromDB("Expense");
        expenseHolder.setSelected(true);
        incomeHolder.setSelected(false);

        onClicks();
        return v;
    }

    private void onClicks() {
        expenseHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                getAllDataFromDB("expense");
                expenseHolder.setSelected(true);
                incomeHolder.setSelected(false);
            }
        });

        incomeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                getAllDataFromDB("Income");
                expenseHolder.setSelected(false);
                incomeHolder.setSelected(true);
            }
        });
        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGranted = StaticFunctions.checkPermission((Activity) context);
                if(isGranted){
                    if(arrayList.size()>0){
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i =0 ; i<arrayList.size();i++){
                            stringBuilder.append(""+(i+1)+". "+arrayList.get(i).getExpenseFor());
                            stringBuilder.append("- Rs."+arrayList.get(i).getTotalExpenseOfTheMonth());
                            stringBuilder.append("("+arrayList.get(i).getDateAndTime()+")");
                            stringBuilder.append("\n"+"----------------------------------"+"\n");
                        }
                        String fileName = "BudgetReport-"+getCurrentDateAndTimeInMillis();
                        createReportPdf(stringBuilder.toString(), fileName);
                    }
                    else{
                        ToastFunction(context, "No data available");
                    }
                } else{
                    ToastFunction(context, "Storage permission is must to generate report");
                }


            }
        });
    }

    private void initM(View v) {
        recyclerView=v.findViewById(R.id.expenseRecyclerView);
        noDataHolder=v.findViewById(R.id.noDataHolder);
        expenseHolder = v.findViewById(R.id.expenseHolder);
        incomeHolder = v.findViewById(R.id.incomeHolder);
        generateReport = v.findViewById(R.id.generateReport);
    }

    private void getAllDataFromDB(String type) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ExpenseModel> expenseModelArrayList = (ArrayList<ExpenseModel>) ExpenseTrackerMain.getDatabaseInstance(context).expenseQuery().getAllData();

                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        if(expenseModelArrayList.size()>1){
                            noDataHolder.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for(int i =1; i<expenseModelArrayList.size();i++){
                                if(expenseModelArrayList.get(i).getCurrentMonth()!=null && !expenseModelArrayList.get(i).getCurrentMonth().equals("")){
                                    if(type.equalsIgnoreCase(expenseModelArrayList.get(i).getType())){
                                        arrayList.add(expenseModelArrayList.get(i));
                                    }
                                }
                            }
                        }
                        else{
                            noDataHolder.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }

                        setExpenseAdapter(type);
                    }
                });
            }
        });
    }

    private void setExpenseAdapter(String type) {
        if(arrayList.size()>0){
            Collections.reverse(arrayList);
            generateReport.setVisibility(View.VISIBLE);
        }
        else{
            generateReport.setVisibility(View.GONE);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        ExpenseAdapter adapter = new ExpenseAdapter(arrayList, context);
        recyclerView.setAdapter(adapter);
    }

    public void createReportPdf(String text, String fileName) {

        Document doc = new Document();

        try {
            File dir = new File(StaticFunctions.commonDocumentDirPath(context));
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, fileName+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

            if(file.exists()){
                ToastFunction(context, "File downloaded at "+file.getAbsolutePath());
            }
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }
    }
}