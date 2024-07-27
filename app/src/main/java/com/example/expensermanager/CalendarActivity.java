package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DayAdapter adapter;
    private TextView monthTextView;
    private Calendar calendar;
    private Map<Integer, Map<Integer, Set<Integer>>> events;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        monthTextView = findViewById(R.id.monthTextView);
        Button prevMonthButton = findViewById(R.id.prevMonthButton);
        Button nextMonthButton = findViewById(R.id.nextMonthButton);

        calendar = Calendar.getInstance();
        events = new HashMap<>();
        populateEvents(); // Populate events for testing

        updateCalendar();

        prevMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        nextMonthButton.setOnClickListener(new View.OnClickListener() {
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

    private void populateEvents() {
        // Populate events with dummy data for testing
        // Structure: events.put(year, monthEvents); monthEvents.put(month, daysWithEvents);
        Map<Integer, Set<Integer>> year2024 = new HashMap<>();
        Set<Integer> januaryEvents = new HashSet<>();
        januaryEvents.add(5);
        januaryEvents.add(12);
        year2024.put(Calendar.JANUARY, januaryEvents);

        Set<Integer> februaryEvents = new HashSet<>();
        februaryEvents.add(14);
        februaryEvents.add(28);
        year2024.put(Calendar.FEBRUARY, februaryEvents);

        events.put(2024, year2024);
    }
}