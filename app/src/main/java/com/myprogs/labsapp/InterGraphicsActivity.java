package com.myprogs.labsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class InterGraphicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter_graphics);

        Intent graphIntent = getIntent();
        String graphType = graphIntent.getStringExtra("com.myprogs.labsapp.GRAPH_TYPE");
        GraphView intrGraphView = findViewById(R.id.InterpolationGraphView);
        GraphView devGraphView = findViewById(R.id.DeviationGraphView);
        double precision = 0.05;
        switch (graphType) {
            case "usual":
                DataPoint[] calcDataPoints = new DataPoint[(int) (4/precision + 1)];
                DataPoint[] theorDataPoints = new DataPoint[(int) (4/precision + 1)];
                DataPoint[] deviationDataPoints = new DataPoint[(int) (4/precision + 1)];
                for (int i = 0; i < (int) (4/precision + 1); i++) {
                    double y_calc = interpolate(precision * i);
                    double y_theor = Math.pow(Math.sin(precision * i), 3) + 3 * Math.pow(Math.cos(precision * i), 2);
                    calcDataPoints[i] = new DataPoint(i * precision, y_calc);
                    theorDataPoints[i] = new DataPoint(i * precision, y_theor);
                    deviationDataPoints[i] = new DataPoint(i * precision, Math.abs(y_theor - y_calc));
                }

                LineGraphSeries<DataPoint> calcSeries = new LineGraphSeries<>(calcDataPoints);
                calcSeries.setAnimated(true);
                calcSeries.setColor(Color.BLUE);
                calcSeries.setTitle("Interpolated");

                LineGraphSeries<DataPoint> theorSeries = new LineGraphSeries<>(theorDataPoints);
                theorSeries.setAnimated(true);
                theorSeries.setColor(Color.RED);
                theorSeries.setTitle("Theoretical");

                LineGraphSeries<DataPoint> deviationSeries = new LineGraphSeries<>(deviationDataPoints);
                deviationSeries.setAnimated(true);
                deviationSeries.setColor(Color.GREEN);
                deviationSeries.setTitle("Deviation");

                intrGraphView.addSeries(theorSeries);
                intrGraphView.addSeries(calcSeries);
                intrGraphView.setLegendRenderer(new LegendRenderer(intrGraphView));
                intrGraphView.getViewport().setScalable(true);
                intrGraphView.getViewport().setScalableY(true);

                intrGraphView.getLegendRenderer().setVisible(true);
                intrGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                devGraphView.addSeries(deviationSeries);
                devGraphView.setLegendRenderer(new LegendRenderer(devGraphView));
                devGraphView.getViewport().setScalable(true);
                devGraphView.getViewport().setScalableY(true);

                devGraphView.getLegendRenderer().setVisible(true);
                devGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                break;

            case "sin":
                DataPoint[] sinTheor = new DataPoint[(int) (4/precision + 1)];
                DataPoint[] sinIntr = new DataPoint[(int) (4/precision + 1)];
                DataPoint[] deviationSin = new DataPoint[(int) (4/precision + 1)];
                for (int i = 0; i < (int) (4/precision + 1); i++) {
                    double y_calc = interpolateSin(precision * i);
                    double y_theor = Math.sin(precision * i);
                    sinIntr[i] = new DataPoint(i * precision, y_calc);
                    sinTheor[i] = new DataPoint(i * precision, y_theor);
                    deviationSin[i] = new DataPoint(i * precision, Math.abs(y_theor - y_calc));
                }

                LineGraphSeries<DataPoint> sinIntrSeries = new LineGraphSeries<>(sinIntr);
                sinIntrSeries.setAnimated(true);
                sinIntrSeries.setColor(Color.BLUE);
                sinIntrSeries.setTitle("Interpolated");

                LineGraphSeries<DataPoint> sinTheorSeries = new LineGraphSeries<>(sinTheor);
                sinTheorSeries.setAnimated(true);
                sinTheorSeries.setColor(Color.RED);
                sinTheorSeries.setTitle("Theoretical");

                LineGraphSeries<DataPoint> deviationSinSeries = new LineGraphSeries<>(deviationSin);
                deviationSinSeries.setAnimated(true);
                deviationSinSeries.setColor(Color.GREEN);
                deviationSinSeries.setTitle("Deviation");

                intrGraphView.addSeries(sinTheorSeries);
                intrGraphView.addSeries(sinIntrSeries);
                intrGraphView.setLegendRenderer(new LegendRenderer(intrGraphView));
                intrGraphView.getViewport().setScalable(true);
                intrGraphView.getViewport().setScalableY(true);

                intrGraphView.getLegendRenderer().setVisible(true);
                intrGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                devGraphView.addSeries(deviationSinSeries);
                devGraphView.setLegendRenderer(new LegendRenderer(devGraphView));
                devGraphView.getViewport().setScalable(true);
                devGraphView.getViewport().setScalableY(true);

                devGraphView.getLegendRenderer().setVisible(true);
                devGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                break;
        }



    }

    public static double interpolate(double x0) {
        double a = 0;
        double b = 4;
        double h = ((b - a) / 10);
        double[] xs = new double[11];
        for (int i = 0; i < xs.length; i++) {
            xs[i] = a + h * i;
        }
        double[] ys = new double[11];
        for (int i = 0; i < ys.length; i++) {
            ys[i] = Math.pow(Math.sin(xs[i]), 3) + 3 * Math.pow(Math.cos(xs[i]), 2);
        }

        int n = xs.length;
        int k1 = 0;
        for (int i = 0; i < n - 1; i++) {
            if (x0 >= xs[i] && x0 <= xs[i+1]) {
                k1 = i;
                break;
            }
        }
        ArrayList<Double> Ms = new ArrayList<>();
        for (int i = k1; i < n - 1; i++) {
            Ms.add(((x0 - xs[i]) * ys[i+1] - (x0 - xs[i+1]) * ys[i]) / (xs[i+1] - xs[i]));
        }

        double newRes = Ms.get(0) - ys[k1];
        double firstRes = newRes + 1;
        ArrayList<Double> newList = new ArrayList<>(Ms);
        ArrayList<Double> oldList = new ArrayList<>(Ms);
        int leap = 2;

        while (Math.abs(firstRes) >= Math.abs(newRes)) {
            if (newList.size() == 1) {
                break;
            }
            firstRes = newRes;
            oldList.clear();
            oldList.addAll(newList);
            newList.clear();

            for (int i = 0; i < oldList.size() - 1; i++) {
                newList.add(((x0 - xs[k1 + i]) * oldList.get(i + 1) - (x0 - xs[k1 + i + leap]) * oldList.get(i)) / (xs[k1+i+leap] - xs[k1+i]));
            }
            leap++;
            newRes = newList.get(0) - oldList.get(0);
        }
        return newList.get(0);
    }

    public double interpolateSin(double x0) {
        double a = 0;
        double b = 4;
        double h = ((b - a) / 10);
        double[] xs = new double[11];
        for (int i = 0; i < xs.length; i++) {
            xs[i] = a + h * i;
        }
        double[] ys = new double[11];
        for (int i = 0; i < ys.length; i++) {
            ys[i] = Math.sin(xs[i]);
        }

        int n = xs.length;
        int k1 = 0;
        for (int i = 0; i < n - 1; i++) {
            if (x0 >= xs[i] && x0 <= xs[i+1]) {
                k1 = i;
                break;
            }
        }
        ArrayList<Double> Ms = new ArrayList<>();
        for (int i = k1; i < n - 1; i++) {
            Ms.add(((x0 - xs[i]) * ys[i+1] - (x0 - xs[i+1]) * ys[i]) / (xs[i+1] - xs[i]));
        }

        double newRes = Ms.get(0) - ys[k1];
        double firstRes = newRes + 1;
        ArrayList<Double> newList = new ArrayList<>(Ms);
        ArrayList<Double> oldList = new ArrayList<>(Ms);
        int leap = 2;

        while (Math.abs(firstRes) >= Math.abs(newRes)) {
            if (newList.size() == 1) {
                break;
            }
            firstRes = newRes;
            oldList.clear();
            oldList.addAll(newList);
            newList.clear();

            for (int i = 0; i < oldList.size() - 1; i++) {
                newList.add(((x0 - xs[k1 + i]) * oldList.get(i + 1) - (x0 - xs[k1 + i + leap]) * oldList.get(i)) / (xs[k1+i+leap] - xs[k1+i]));
            }
            leap++;
            newRes = newList.get(0) - oldList.get(0);
        }
        return newList.get(0);
    }
}
