package com.myprogs.labsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class Lab5Activity extends AppCompatActivity {

    private double[][] aMatrix;
    private double[] bMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5);

        Button routesCalcBtn = findViewById(R.id.lab5RoutesCalcButton);
        Button saveInputBtn = findViewById(R.id.lab5SaveButton);
        Button readFromFileBtn = findViewById(R.id.lab5ReadButton);

        readFromFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    doExactRead();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(Lab5Activity.this, "This permission is needed to use your saved data!", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 13);
                }
            }
        });

        saveInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    doExactSave();
                } else {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(Lab5Activity.this, "This permission is needed to save your data!", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 15);
                }
            }
        });

        routesCalcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout matrixTable = findViewById(R.id.coefMatixTableLayout);
                LinearLayout bCoefs = findViewById(R.id.bCoefsLinearLayout);
                LinearLayout resultLayout = findViewById(R.id.routesLinearLayout);
                EditText omegaEditText = findViewById(R.id.omegaEditText);
                TextView resultsHeader = findViewById(R.id.routesHeaderTextView);
                aMatrix = new double[3][3];
                bMatrix = new double[3];
                double omega = Double.parseDouble(omegaEditText.getText().toString());

                for (int i = 0; i < 3; i++) {
                    TableRow row = (TableRow) matrixTable.getChildAt(i);
                    EditText bRow = (EditText) bCoefs.getChildAt(i);

                    for (int j = 0; j < 3; j++) {
                        EditText elem = (EditText) row.getChildAt(j);
                        aMatrix[i][j] = Double.parseDouble(elem.getText().toString());
                    }
                    bMatrix[i] = Double.parseDouble(bRow.getText().toString());
                }

                double[] result = matrixSolve(aMatrix, bMatrix, omega);
                resultsHeader.setText("Корені системи:");
                for (int i = 0; i < 3; i++) {
                    TextView routeTextView = (TextView) resultLayout.getChildAt(i);
                    routeTextView.setText("X" + (i+1) +" = " + result[i]);
                }
            }
        });
    }

    private void doExactSave() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File customInputFile = new File(path, "/lab5/userInput.txt");
        StringBuilder builder = new StringBuilder();
        for (double[] row: aMatrix) {
            for (double elem: row) {
                builder.append(elem);
                builder.append(",");
            }
            builder.append("\n");
        }
        for (double coef: bMatrix) {
            builder.append(coef);
            builder.append(",");
        }

        try {
            customInputFile.createNewFile();
            OutputStream output = new FileOutputStream(customInputFile);
            byte[] byteData = builder.toString().getBytes();
            output.write(byteData);
            output.close();
            Toast.makeText(getApplicationContext(), "Data Saved Successfully!", Toast.LENGTH_SHORT).show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doExactRead() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File customOutputFile = new File(path, "/lab5/userInput.txt");
        TableLayout matrixTable = findViewById(R.id.coefMatixTableLayout);
        LinearLayout bCoefs = findViewById(R.id.bCoefsLinearLayout);

        try {
        FileInputStream fileInputStream = new FileInputStream(customOutputFile);
        Scanner scanner = new Scanner(fileInputStream, "UTF-8");
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        fileInputStream.close();
        scanner.close();

        String[] data = builder.toString().split(",");
        int k = 0;
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) matrixTable.getChildAt(i);

            for (int j = 0; j < 3; j++) {
                EditText elem = (EditText) row.getChildAt(j);
                elem.setText(data[k]);
                k++;
            }
        }

        for (int i = 0; i < 3; i++) {
            EditText bRow = (EditText) bCoefs.getChildAt(i);
            bRow.setText(data[k]);
            k++;
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] matrixSolve(double[][] aMatrix, double[] bMatrix, double omega){
        double[] oldX;
        double[] newX = new double[]{bMatrix[0], 0, 0};
        double[] result = new double[3];

        int counter = 0;
        do {
            counter++;
            oldX = newX.clone();
            newX[0] = (bMatrix[0] - oldX[1] * aMatrix[0][1] - oldX[2] * aMatrix[0][2]) / aMatrix[0][0];
            newX[1] = (bMatrix[1] - newX[0] * aMatrix[1][0] - oldX[2] * aMatrix[1][2]) / aMatrix[1][1];
            newX[2] = (bMatrix[2] - newX[0] * aMatrix[2][0] - newX[1] * aMatrix[2][1]) / aMatrix[2][2];

            for (int j = 0; j < 3; j++){
                result[j] = oldX[j] + omega * (newX[j] - oldX[j]);
            }
        } while (Math.abs(result[0] - oldX[0]) > 0.00001);

        System.out.println(counter);
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 13){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doExactSave();

            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == 15) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doExactSave();

            } else {
                Toast.makeText(Lab5Activity.this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
