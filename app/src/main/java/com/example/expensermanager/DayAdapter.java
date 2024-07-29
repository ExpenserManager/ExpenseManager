package com.example.expensermanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensermanager.databinding.DayItemBinding;
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

    //From Android Developers: https://developer.android.com/topic/libraries/data-binding/expressions?hl=de
    //Used when a new ViewHolder of the viewType is needed
    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        DayItemBinding dayItemBinding = DayItemBinding.inflate(layoutInflater, parent, false);

        return new DayViewHolder(dayItemBinding);
    }

    //Method from ChatGBT
    //Displays the eventMarker on the calendar
    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        int day = days[position];
        holder.dayItemBinding.dayTextView.setText(String.valueOf(day));

        //Visibility of eventMarker -> checks if there is an event on that day
        if (eventDays.contains(day)) {
            holder.dayItemBinding.eventMarker.setVisibility(View.VISIBLE);
        } else {
            holder.dayItemBinding.eventMarker.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        private DayItemBinding dayItemBinding;

        //Help from ChatGBT for binding
        public DayViewHolder(@NonNull DayItemBinding dayItemBinding) {
            super(dayItemBinding.getRoot());
            this.dayItemBinding = dayItemBinding;
        }
    }
}
