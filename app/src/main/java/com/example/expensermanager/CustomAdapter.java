package com.example.expensermanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    DatabaseHelper dbHelper;

    CustomAdapter(Context context,
                  ArrayList id,
                  ArrayList category,
                  ArrayList description,
                  ArrayList amount, DatabaseHelper dbHelper){

        this.context = context;
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.dbHelper = dbHelper;

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



        holder.deleteButton.setOnClickListener(v -> {
            removeItem(position);
        });

        //enable clicking on the listitems
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateDataActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("description", String.valueOf(description.get(position)));
                intent.putExtra("amount", String.valueOf(amount.get(position)));

                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, 1);
                } else {
                    context.startActivity(intent);
                }
            }
        });



    }

    private void removeItem(int position){
        String deleteItemID = id.get(position);
        dbHelper.deleteData(dbHelper, deleteItemID);

        //remove data from ArrayLists
        id.remove(position);
        category.remove(position);
        description.remove(position);
        amount.remove(position);
        notifyItemRemoved(position); //update RecyclerView
        notifyItemRangeChanged(position, getItemCount()); //notify the adapter that the range has changed
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView id_holder, category, description_holder, amount_holder;
       RelativeLayout listitem;
       RecyclerView recyclerView;
       ImageButton deleteButton; //accessing the deleteButton was done with help of chatCPT
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_holder = itemView.findViewById(R.id.ID);
            description_holder = itemView.findViewById(R.id.description);
            amount_holder = itemView.findViewById(R.id.amount);
            listitem = itemView.findViewById(R.id.relativeLayout);
            recyclerView = itemView.findViewById(R.id.rv);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }
}
