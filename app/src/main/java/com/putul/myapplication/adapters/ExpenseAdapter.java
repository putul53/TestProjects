package com.putul.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.putul.myapplication.R;
import com.putul.myapplication.database.expense.ExpenseModel;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    ArrayList<ExpenseModel> modelArrayList=new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public ExpenseAdapter(ArrayList<ExpenseModel> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=layoutInflater.inflate(R.layout.custom_expenses_home,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        ExpenseModel model = modelArrayList.get(position);
        String type = "";
        if( model.getType().equalsIgnoreCase("income")){
            type = "+";
            viewHolder.expenseAmountText.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else{
            type  = "-";
            viewHolder.expenseAmountText.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }
        viewHolder.titleText.setText(model.getExpenseFor());
        viewHolder.expenseAmountText.setText(type+"Rs."+model.getTotalExpenseOfTheMonth());
        viewHolder.dateText.setText(model.getDateAndTime());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // onCatSelectedListener.onCatSelected(modelArrayList.get(position), purpose);
            }
        });
    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, dateText, expenseAmountText;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText=itemView.findViewById(R.id.titleText);
            expenseAmountText=itemView.findViewById(R.id.expenseAmountText);
            dateText=itemView.findViewById(R.id.dateText);
            linearLayout=itemView.findViewById(R.id.linear_expense_holder);
        }
    }

    public interface OnCatSelectedListener{
        void onCatSelected(String data, String purpose);
    }
}
