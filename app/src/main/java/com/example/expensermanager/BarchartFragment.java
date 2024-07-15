package com.example.expensermanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;

public class BarchartFragment extends Fragment {

    private FragmentBarchartBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentBarchartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        BarChart barChart = binding.barChart;

        // Example data
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 200));
        entries.add(new BarEntry(2, 300));
        entries.add(new BarEntry(3, 250));
        entries.add(new BarEntry(4, 350));
        entries.add(new BarEntry(5, 180));

        BarDataSet dataSet = new BarDataSet(entries, "Sample Data");

        // set the colors of the bars
        dataSet.setColors(new int[]{Color.rgb(0, 51, 102), Color.rgb(0, 128, 128), Color.rgb(0, 204, 102), Color.rgb(0, 153, 153), Color.rgb(51, 102, 204)});
        dataSet.setValueTextColor(Color.TRANSPARENT); // Hide value text on bars

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);

        barChart.setData(data);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);

        // Y-Axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);        // show grid lines
        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinimum(0f);            // start y from 0
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        barChart.setFitBars(true);      // position bars so all fit in

        barChart.animateY(1500);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
