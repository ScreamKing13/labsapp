package com.myprogs.labsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Scanner;


public class Lab2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);

        Button manualSortBtn = findViewById(R.id.lab2ManualSortButton);
        Button fileSortButton = findViewById(R.id.lab2FileSortButton);
        Button makeGraphicsBtn = findViewById(R.id.lab2MakeGraphicsBtn);
        Button saveInputBtn = findViewById(R.id.lab2SaveInputBtn);

        manualSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText manualData = findViewById(R.id.lab2ManualDataEditText);
                TextView sortedDataTextView = findViewById(R.id.lab2ResultsTextView);
                TextView lab2TimeTextView = findViewById(R.id.lab2TimeTextView);

                if (!manualData.getText().toString().isEmpty()) {

                    String[] stringManualData = manualData.getText().toString().split(" ");
                    int[] intManualData = new int[stringManualData.length];

                    for (int i = 0; i < stringManualData.length; i++) {
                        intManualData[i] = Integer.parseInt(stringManualData[i].trim());
                    }

                    long startTime = System.nanoTime();
                    int[] sortedData = sort(intManualData);
                    long timeTaken = System.nanoTime() - startTime;

                    StringBuilder stringBuilder = new StringBuilder("[ ");
                    for (int elem: sortedData) {
                        stringBuilder.append(elem);
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append("]");
                    sortedDataTextView.setText(stringBuilder.toString());
                    lab2TimeTextView.setText("The sorting took: " + timeTaken + " nanoseconds.");

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fileSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    new MaterialFilePicker()
                            .withActivity(Lab2Activity.this)
                            .withRequestCode(1)
                            .withHiddenFiles(false) // Show hidden files and folders
                            .start();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(Lab2Activity.this, "This permission is needed to save results.", Toast.LENGTH_SHORT)
                                .show();
                    }
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
                }
            }
        });

        makeGraphicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent startIntent = new Intent(getApplicationContext(), GraphicsActivity.class);
                    startActivity(startIntent);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(Lab2Activity.this, "This permission is needed to build graphic plots!", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 14);
                }
            }
        });

        saveInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText customInputEdtxt = findViewById(R.id.lab2ManualDataEditText);
                String customInputText = customInputEdtxt.getText().toString();

                if (!customInputText.isEmpty()){
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                        File customInputFile = new File(path, "userInput.txt");
                        try {
                            customInputFile.createNewFile();
                            OutputStream output = new FileOutputStream(customInputFile);
                            byte[] byteData = customInputText.getBytes();
                            output.write(byteData);
                            output.close();
                            Toast.makeText(getApplicationContext(), "Data Saved Successfully!", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(Lab2Activity.this, "This permission is needed to save your data!", Toast.LENGTH_SHORT).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 15);
                    }

                } else {
                    Toast.makeText(Lab2Activity.this, "Input is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            TextView lab2ResultsTextView = findViewById(R.id.lab2ResultsTextView);
            TextView lab2TimeTExtView = findViewById(R.id.lab2TimeTextView);

            // Do your job here
            try {
                File chosenFile = new File(filePath);
                FileInputStream fileInputStream = new FileInputStream(chosenFile);
                Scanner scanner = new Scanner(fileInputStream, "UTF-8");
                StringBuilder another = new StringBuilder();
                while (scanner.hasNextLine()) {
                    another.append(scanner.nextLine());
                }
                fileInputStream.close();
                scanner.close();

                String[] extracted;
                if (filePath.contains("userInput")) {
                    extracted = another.toString().split(" ");
                } else {
                    extracted = another.toString().split(", ");
                }
                int[] fileIntData = new int[extracted.length];

                for (int i = 0; i < extracted.length; i++) {
                    fileIntData[i] = Integer.parseInt(extracted[i].trim());
                }

                long startTime = System.nanoTime();
                int[] sortedData = sort(fileIntData);
                long timeTaken = System.nanoTime() - startTime;

                StringBuilder stringBuilder = new StringBuilder("[ ");
                for (int elem: sortedData) {
                    stringBuilder.append(elem);
                    stringBuilder.append(" ");
                }
                stringBuilder.append("]");

                lab2ResultsTextView.setText(stringBuilder.toString());
                lab2TimeTExtView.setText("Sorting took: " + timeTaken + " nanoseconds");
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 13){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();

                new MaterialFilePicker()
                        .withActivity(Lab2Activity.this)
                        .withRequestCode(1)
                        .withHiddenFiles(false) // Show hidden files and folders
                        .start();
            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == 14) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent startIntent = new Intent(getApplicationContext(), GraphicsActivity.class);
                startActivity(startIntent);
            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == 15) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                EditText customInputEdtxt = findViewById(R.id.lab2ManualDataEditText);
                String customInputText = customInputEdtxt.getText().toString();

                File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString());
                File customInputFile = new File(path, "userInput.txt");
                try {
                    customInputFile.createNewFile();
                    OutputStream output = new FileOutputStream(customInputFile);
                    byte[] byteData = customInputText.getBytes();
                    output.write(byteData);
                    output.close();
                    Toast.makeText(getApplicationContext(), "Data Saved Successfully!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(Lab2Activity.this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static int[] sort(int[] arrayToSort) {
        if(arrayToSort.length < 2) return arrayToSort;
        int m = arrayToSort.length / 2;
        int[] arr1 = Arrays.copyOfRange(arrayToSort, 0, m);
        int[] arr2 = Arrays.copyOfRange(arrayToSort, m, arrayToSort.length);
        return merge(sort(arr1), sort(arr2));
    }

    public static int[] merge(int[] arr1,int arr2[]) {
        int n = arr1.length + arr2.length;
        int[] arr = new int[n];
        int i1=0;
        int i2=0;
        for(int i=0;i<n;i++){
            if(i1 == arr1.length){
                arr[i] = arr2[i2++];
            }else if(i2 == arr2.length){
                arr[i] = arr1[i1++];
            }else{
                if(arr1[i1] < arr2[i2]){
                    arr[i] = arr1[i1++];
                }else{
                    arr[i] = arr2[i2++];
                }
            }
        }
        return arr;
    }
}
