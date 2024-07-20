package com.example.expensermanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.expensermanager.databinding.FragmentBarchartBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

public class BarchartFragment extends Fragment {

    private FragmentBarchartBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentBarchartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        BarChart barChart = binding.barChart;
        dbHelper = new DatabaseHelper(getContext());

        List<BarEntry> entries = dbHelper.getBarEntries();
        List<String> categories = dbHelper.getAllCategories();

        BarDataSet dataSet = new BarDataSet(entries, "Category Expenses");

        // set color of the bars
        dataSet.setColors(new int[]{Color.rgb(0, 51, 102), Color.rgb(0, 128, 128), Color.rgb(0, 204, 102), Color.rgb(0, 153, 153), Color.rgb(51, 102, 204)});
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);

        barChart.setData(data);

        barChart.getDescription().setEnabled(true);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setText("");

        // set the custom renderer
        barChart.setRenderer(new RoundedBarChartRenderer(barChart, barChart.getAnimator(), barChart.getViewPortHandler()));

        // X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories)); //    use the category names as description beneath the bars

        // Y-Axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);        // show grid lines
        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(0f);            // start y from 0
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.setFitBars(true);      // position bars so all fit in

        barChart.animateY(1500);    // start animation (bars go up slowly)

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                BarEntry entry = (BarEntry) e;
                int index = (int) entry.getX();
            }

            @Override
            public void onNothingSelected() {
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
