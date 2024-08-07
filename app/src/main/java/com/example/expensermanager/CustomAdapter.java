package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensermanager.databinding.ListItemBinding;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList imagePaths;
    Context context;
    ArrayList<String> id, category, description, amount, date;
    DatabaseHelper dbHelper;

    CustomAdapter(Context context,
                  ArrayList id,
                  ArrayList category,
                  ArrayList description,
                  ArrayList amount, ArrayList date, ArrayList imagePaths, DatabaseHelper dbHelper){

        this.context = context;
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.dbHelper = dbHelper;
        this.imagePaths = imagePaths;

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
        holder.description_holder.setText(String.valueOf(description.get(position)));
        String amountText = "- €" + String.valueOf(amount.get(position));

        holder.amount_holder.setText(amountText);
        holder.date_holder.setText(date.get(position));

        String imagePath = (String) imagePaths.get(position);
        holder.deleteButton.setOnClickListener(v -> {
            removeItem(position, "expense_manager");
        });

        holder.receiptButton.setOnClickListener(v -> {
            showImageDialog((String) imagePaths.get(position));
        });
        //enable clicking on the listitems
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateDataActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("description", String.valueOf(description.get(position)));
                intent.putExtra("amount", String.valueOf(amount.get(position)));
                intent.putExtra("date", String.valueOf(date.get(position)));
                intent.putExtra("image_path", imagePath);
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, 1);
                } else {
                    context.startActivity(intent);
                }
            }
        });



    }

    public void removeItem(int position, String tableName){
        String deleteItemID = id.get(position);
        dbHelper.deleteData(dbHelper, deleteItemID, tableName);

        //remove data from ArrayLists
        id.remove(position);
        category.remove(position);
        description.remove(position);
        amount.remove(position);
        date.remove(position);
        imagePaths.remove(position);
        notifyItemRemoved(position); //update RecyclerView
        notifyItemRangeChanged(position, getItemCount()); //notify the adapter that the range has changed
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id_holder, category, description_holder, amount_holder, date_holder;
        RelativeLayout listitem;
        RecyclerView recyclerView;
        ImageButton deleteButton; //accessing the deleteButton was done with help of chatGPT
        ImageButton receiptButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_holder = itemView.findViewById(R.id.ID);
            description_holder = itemView.findViewById(R.id.description);
            amount_holder = itemView.findViewById(R.id.amount);
            date_holder = itemView.findViewById(R.id.date_textView);
            listitem = itemView.findViewById(R.id.relativeLayout);
            recyclerView = itemView.findViewById(R.id.rv);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            receiptButton = itemView.findViewById(R.id.receiptButton);
        }
    }

    public void setFilteredList(ArrayList<String> filteredListDescription, ArrayList<String> filteredListId,ArrayList<String> filteredListAmount, ArrayList<String> filteredListDate, ArrayList<String> filteredListImagePath) {
        this.description = filteredListDescription;
        this.id = filteredListId;
        this.amount = filteredListAmount;
        this.date = filteredListDate;
        this.imagePaths = filteredListImagePath;
        notifyDataSetChanged();
    }
    private void showImageDialog(String imagePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_image, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView imageView = dialogView.findViewById(R.id.dialog_image_view);

        // Load and display the image
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Log.d("Path", "imagepath 2"+imagePath);

            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.placeholderreceipt); // Placeholder image
        }

        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
