package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensermanager.databinding.ActivityCalendarBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

public class CalendarActivity extends AppCompatActivity {
    private ActivityCalendarBinding binding;
    private RecyclerView recyclerView;
    private DayAdapter adapter;
    private TextView monthTextView;
    private Calendar calendar;
    private Map<Integer, Map<Integer, Set<Integer>>> events; //ChatGBT
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        recyclerView = binding.recyclerView;
        monthTextView = binding.monthTextView;

        calendar = Calendar.getInstance();
        events = new HashMap<>();

        addEvents();
        updateCalendar();

        binding.previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        binding.nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });
    }

    private void updateCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthTextView.setText(sdf.format(calendar.getTime()));

        // Get current month and year
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        // Generate days of the current month
        calendar.set(currentYear, currentMonth, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int[] days = new int[daysInMonth];

        for (int i = 0; i < daysInMonth; i++) {
            days[i] = i + 1;
        }

        // Get events for the current month and year
        Set<Integer> eventDays = getEventsForMonth(currentYear, currentMonth);

        // Set up the RecyclerView
        adapter = new DayAdapter(this, days, eventDays);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 7 columns for days of the week
        recyclerView.setAdapter(adapter);
    }

    private Set<Integer> getEventsForMonth(int year, int month) {
        Map<Integer, Set<Integer>> yearEvents = events.get(year);
        if (yearEvents != null) {
            Set<Integer> monthEvents = yearEvents.get(month);
            if (monthEvents != null) {
                return monthEvents;
            }
        }
        return new HashSet<>(); // Return an empty set if no events found
    }

    private void addEvents() {
        Set<Integer> allYears = getYear();

        for (Integer years: allYears){
            Map<Integer, Set<Integer>> year = new HashMap<>();
            Set<Integer> januaryEvents = checkMonth(1, years);
            year.put(Calendar.JANUARY, januaryEvents);

            Set<Integer> februaryEvents = checkMonth(2, years);
            year.put(Calendar.FEBRUARY, februaryEvents);

            Set<Integer> marchEvents = checkMonth(3, years);
            year.put(Calendar.MARCH, marchEvents);

            Set<Integer> aprilEvents = checkMonth(4, years);
            year.put(Calendar.APRIL, aprilEvents);

            Set<Integer> mayEvents = checkMonth(5, years);
            year.put(Calendar.MAY, mayEvents);

            Set<Integer> juneEvents = checkMonth(6, years);
            year.put(Calendar.JUNE, juneEvents);

            Set<Integer> julyEvents = checkMonth(7, years);
            year.put(Calendar.JULY, julyEvents);

            Set<Integer> augustEvents = checkMonth(8, years);
            year.put(Calendar.AUGUST, augustEvents);

            Set<Integer> septemberEvents = checkMonth(9, years);
            year.put(Calendar.SEPTEMBER, septemberEvents);

            Set<Integer> octoberEvents = checkMonth(10, years);
            year.put(Calendar.OCTOBER, octoberEvents);

            Set<Integer> novemberEvents = checkMonth(11, years);
            year.put(Calendar.NOVEMBER, novemberEvents);

            Set<Integer> decemberEvents = checkMonth(12, years);
            year.put(Calendar.DECEMBER, decemberEvents);

            events.put(years, year);
        }
    }

    private Set<Integer> checkMonth(int month, int year){
        ArrayList<String> dates = dbHelper.getDates();
        Set<Integer> eventDays = new HashSet<>();
        String val = "-"+month+"-";

        for (int i=0; i<dates.size(); i++){
            if (dates.get(i).contains(val)){
                String[] tmp = dates.get(i).split("-");
                if (Integer.parseInt(tmp[2])==year){
                    eventDays.add(Integer.parseInt(tmp[0]));
                }
            }
        }

        return eventDays;
    }

    private Set<Integer> getYear(){
        ArrayList<String> dates = dbHelper.getDates();
        Set<Integer> years = new HashSet<>();

        for (int i=0; i<dates.size(); i++){
            String[] tmp = dates.get(i).split("-");
            int check = Integer.parseInt(tmp[2]);

            if (!years.contains(check)){
                years.add(check);
            }
        }

        return years;
    }
}