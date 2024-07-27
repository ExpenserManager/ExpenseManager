package com.example.expensermanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Set;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private final Context context;
    private final Set<Integer> eventDays;
    private final int[] days;

    public DayAdapter(Context context, int[] days, Set<Integer> eventDays) {
        this.context = context;
        this.days = days;
        this.eventDays = eventDays;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        int day = days[position];
        holder.dayTextView.setText(String.valueOf(day));

        // Check if there is an event on this day
        if (eventDays.contains(day)) {
            holder.eventMarker.setVisibility(View.VISIBLE);
        } else {
            holder.eventMarker.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        ImageView eventMarker;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            eventMarker = itemView.findViewById(R.id.eventMarker);
        }
    }
}
