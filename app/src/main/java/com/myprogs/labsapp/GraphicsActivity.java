package com.myprogs.labsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static com.myprogs.labsapp.Lab2Activity.sort;

public class GraphicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);

        GraphView expTimeGraph = findViewById(R.id.timeDataGraphic);


        File lab2Folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "lab2");
        DataPoint[] dataPoints = new DataPoint[10];
        int currentPoint = 0;
        List<File> sortedFiles = Arrays.asList(lab2Folder.listFiles());
        Collections.sort(sortedFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName().replace(".txt", "")) <
                        Integer.parseInt(o2.getName().replace(".txt", ""))) {
                    return -1;
                }
                else if (Integer.parseInt(o1.getName().replace(".txt", "")) ==
                        Integer.parseInt(o2.getName().replace(".txt", ""))) {
                    return 0;
                }
                return 1;
            }
        });

        for (File testFile: sortedFiles) {
            dataPoints[currentPoint] = new DataPoint(
                    Integer.parseInt(testFile.getName().replace(".txt", "")),
                    getAlgorithmExecTime(testFile));
            currentPoint++;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        series.setAnimated(true);
        series.setColor(Color.BLUE);
        series.setTitle("Calculated complexity");
        expTimeGraph.addSeries(series);
        expTimeGraph.getViewport().setScalable(true);
        expTimeGraph.getViewport().setScalableY(true);

        LineGraphSeries<DataPoint> theorSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(10, 10 * Math.log(10) / Math.log(2) / 35000),
                new DataPoint(100, 100 * Math.log(100) / Math.log(2) / 35000),
                new DataPoint(1000, 1000 * Math.log(1000) / Math.log(2) / 35000),
                new DataPoint(5000, 5000 * Math.log(5000) / Math.log(2) / 35000),
                new DataPoint(10000, 10000 * Math.log(10000) / Math.log(2) / 35000),
                new DataPoint(20000, 20000 * Math.log(20000) / Math.log(2) / 35000),
                new DataPoint(30000, 30000 * Math.log(30000) / Math.log(2) / 35000),
                new DataPoint(40000, 40000 * Math.log(40000) / Math.log(2) / 35000),
                new DataPoint(80000, 80000 * Math.log(80000) / Math.log(2) / 35000)
        });
        theorSeries.setAnimated(true);
        theorSeries.setColor(Color.RED);
        theorSeries.setTitle("Theoretical complexity");
        expTimeGraph.addSeries(theorSeries);
        expTimeGraph.setLegendRenderer(new LegendRenderer(expTimeGraph));

        expTimeGraph.getLegendRenderer().setVisible(true);
        expTimeGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    public long getAlgorithmExecTime (File dataFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(dataFile);
            Scanner scanner = new Scanner(fileInputStream, "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            fileInputStream.close();
            scanner.close();

            String[] extracted = stringBuilder.toString().split(", ");
            int[] fileIntData = new int[extracted.length];

            for (int i = 0; i < extracted.length; i++) {
                fileIntData[i] = Integer.parseInt(extracted[i].trim());
            }

            long startTime = System.currentTimeMillis();
            sort(fileIntData);

            return System.currentTimeMillis() - startTime;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
