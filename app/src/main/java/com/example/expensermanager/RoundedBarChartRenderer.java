package com.example.expensermanager;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

// https://stackoverflow.com/questions/30761082/mpandroidchart-round-edged-bar-chart
// https://github.com/ABTSoftware/SciChart.Android.Examples/blob/SciChart_v4_Release/app/src/main/java/com/scichart/examples/fragments/examples2d/createCustomCharts/RoundedColumnsExampleFragment.java
// https://rrohaill.medium.com/philjay-mpandroidchart-simple-manager-class-for-barchart-dec7dd0a5aeb

public class RoundedBarChartRenderer extends BarChartRenderer {

    public RoundedBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected void drawDataSet(Canvas c, com.github.mikephil.charting.interfaces.datasets.IBarDataSet dataSet, int index) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // initialize the buffer with the predefined bars (fragment)
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        for (int j = 0; j < buffer.size(); j += 4) {
            // bar shape
            RectF barRect = new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3]);
            mRenderPaint.setColor(dataSet.getColor(j / 4));         // set color based on entry index

            float cornerRadius = 25f;
            c.drawRoundRect(barRect, cornerRadius, cornerRadius, mRenderPaint);
        }
    }
}
