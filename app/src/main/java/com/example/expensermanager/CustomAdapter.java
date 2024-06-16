package com.example.expensermanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensermanager.databinding.ListItemBinding;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {


    Context context;
    ArrayList<String> id, category, description, amount;
    Activity activity;

    CustomAdapter(Context context,
                  ArrayList id,
                  ArrayList category,
                  ArrayList description,
                  ArrayList amount){

        this.context = context;
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.id_holder.setText(String.valueOf(id.get(position)));
        holder.description_holder.setText(String.valueOf(description.get(position)));
        holder.amount_holder.setText(String.valueOf(amount.get(position)));

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView id_holder, category, description_holder, amount_holder;
       RelativeLayout listitem;
       RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_holder = itemView.findViewById(R.id.ID);
            description_holder = itemView.findViewById(R.id.description);
            amount_holder = itemView.findViewById(R.id.amount);
            listitem = itemView.findViewById(R.id.relativeLayout);
            recyclerView = itemView.findViewById(R.id.rv);
        }
    }
}
