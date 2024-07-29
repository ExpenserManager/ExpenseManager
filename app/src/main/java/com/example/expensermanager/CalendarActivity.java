package com.example.expensermanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        calendar = Calendar.getInstance(); //Get the current date
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

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method from ChatGBT
    //Calendar gets updated to display the current month and year
    private void updateCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthTextView.setText(sdf.format(calendar.getTime()));

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        //Sets the calendar to the 1st day of the month
        calendar.set(currentYear, currentMonth, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int[] days = new int[daysInMonth];

        //Fills the calendar
        for (int i = 0; i < daysInMonth; i++) {
            days[i] = i + 1;
        }

        //Get the event days for that month
        Set<Integer> eventDays = getEventsForMonth(currentYear, currentMonth);

        adapter = new DayAdapter(this, days, eventDays);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        recyclerView.setAdapter(adapter);
    }

    //Method from ChatGBT
    //Gives the event days for that month and year
    private Set<Integer> getEventsForMonth(int year, int month) {
        Map<Integer, Set<Integer>> yearEvents = events.get(year);
        if (yearEvents != null) {
            Set<Integer> monthEvents = yearEvents.get(month);
            if (monthEvents != null) {
                return monthEvents;
            }
        }

        return new HashSet<>();
    }

    //Adds the event days in the calendar
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

    //Checks if there are event days in that month and saves them in a Hash Set
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

    //Checks if there is an event in that year and saves it in a Hash set
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