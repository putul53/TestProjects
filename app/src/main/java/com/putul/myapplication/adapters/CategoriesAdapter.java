package com.putul.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.putul.myapplication.R;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    ArrayList<String> modelArrayList=new ArrayList<>();
    Context context;
    String purpose;
    LayoutInflater layoutInflater;
    OnCatSelectedListener onCatSelectedListener;

    public CategoriesAdapter(ArrayList<String> modelArrayList, Context context, String purpose, OnCatSelectedListener onCatSelectedListener) {
        this.modelArrayList = modelArrayList;
        this.purpose =  purpose;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        this.onCatSelectedListener = onCatSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=layoutInflater.inflate(R.layout.custom_categories,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        viewHolder.titleText.setText(modelArrayList.get(position));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCatSelectedListener.onCatSelected(modelArrayList.get(position), purpose);
            }
        });
    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText=itemView.findViewById(R.id.titleText);
           linearLayout=itemView.findViewById(R.id.linear_cat_holder);
        }
    }

    public interface OnCatSelectedListener{
        void onCatSelected(String data, String purpose);
    }
}
