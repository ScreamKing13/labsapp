package com.myprogs.labsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Lab4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);

        Button xCalcBtn = findViewById(R.id.xCalcButton);
        xCalcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText aEditText = findViewById(R.id.aEditText);
                EditText bEditText = findViewById(R.id.bEditText);
                EditText accuracyEditText = findViewById(R.id.accuracyEditText);
                TextView xResultTextView = findViewById(R.id.xResultTextView);
                TextView fxResultTextView = findViewById(R.id.fxResultTextView);

                double a = Double.parseDouble(aEditText.getText().toString());
                double b = Double.parseDouble(bEditText.getText().toString());
                int accuracy = Integer.parseInt(accuracyEditText.getText().toString());
                if (func(a) * func(b) < 0) {
                    double x = combMethod(a, b, accuracy);
                    BigDecimal fx = new BigDecimal(func(x)).setScale(accuracy, RoundingMode.UP);

                    xResultTextView.setText("X0 = " + String.valueOf(x));
                    fxResultTextView.setText("F(X0) = " + String.valueOf(fx));
                }
                else {
                    Toast.makeText(Lab4Activity.this, "В даних межах кореня немає!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        GraphView lab4GraphView = findViewById(R.id.lab4GraphView);
        DataPoint[] dataPoints = new DataPoint[1000];
        for (int i = 0; i < 1000; i++) {
            double step = (double) i / 500;
            double x = -1 + step;
            dataPoints[i] = new DataPoint(x, func(x));
        }

        PointsGraphSeries<DataPoint> routesSeries = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(combMethod(-0.6, -0.5, 6),
                        func(combMethod(-0.6, -0.5, 6))),
                new DataPoint(combMethod(-0.4, -0.2, 6),
                        func(combMethod(-0.4, -0.2, 6))),
                new DataPoint(combMethod(-0.1, 0.1, 6),
                        func(combMethod(-0.1, 0.1, 6))),
                new DataPoint(combMethod(0.2, 0.4, 6),
                        func(combMethod(0.2, 0.4, 6)))
        });
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(dataPoints);

        routesSeries.setColor(Color.RED);
        routesSeries.setShape(PointsGraphSeries.Shape.POINT);
        routesSeries.setTitle("Routes");

        lineSeries.setAnimated(true);
        lineSeries.setColor(Color.BLUE);
        lineSeries.setTitle("Function");

        lab4GraphView.addSeries(lineSeries);
        lab4GraphView.addSeries(routesSeries);
        lab4GraphView.setLegendRenderer(new LegendRenderer(lab4GraphView));
        lab4GraphView.getViewport().setScalable(true);
        lab4GraphView.getViewport().setScalableY(true);
    }

    public static double func(double x) {
        return 1.8 * Math.pow(x, 2) - Math.sin(10 * x);
    }

    public static double dfunc(double x) {
        return 3.6 * x - 10 * Math.cos(10 * x);
    }

    public static double d2func(double x) {
        return 3.6 + 100 * Math.sin(10 * x);
    }

    public static double combMethod(double a, double b, int accuracy) {
        boolean rule1 = func(b) * d2func(b) > 0;
        double xCordOld;
        double xTangOld;
        double xCordNew;
        double xTangNew;

        if (rule1) {
            xCordOld = a;
            xTangOld = b;
        }
        else {
            xCordOld = b;
            xTangOld = a;
        }

        do {
            xTangNew = xTangOld - func(xTangOld)/dfunc(xTangOld);

            if (rule1) {
                xCordNew = xCordOld - func(xCordOld)*(xTangOld - xCordOld)/(func(xTangOld) - func(xCordOld));
            } else {
                xCordNew = xCordOld - func(xCordOld)*(xCordOld - xTangOld)/(func(xCordOld) - func(xTangOld));
            }
            xCordOld = xCordNew;
            xTangOld = xTangNew;
        } while (Math.abs(xTangNew - xCordNew) >= 1 / Math.pow(10, accuracy));

        return new BigDecimal((xCordNew + xTangNew) / 2).setScale(accuracy, RoundingMode.UP)
                .doubleValue();
    }
}
