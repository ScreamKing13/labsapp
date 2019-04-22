package com.myprogs.labsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class CycledActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 112;
    public static final int REQUEST_READ_STORAGE = 113;

    public void doExactSave(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"lab1");
            File cycledFile = new File(path, "cycled.txt");
            EditText aArray = findViewById(R.id.aArrayEditText);
            EditText bArray = findViewById(R.id.bArrayEditText);

            try {
                path.mkdir();
                cycledFile.createNewFile();
                OutputStream output = new FileOutputStream(cycledFile);
                String data = aArray.getText() + "\n" + bArray.getText();
                byte[] byteData = data.getBytes();
                output.write(byteData);
                output.close();
                Toast.makeText(getApplicationContext(), "Data Saved Successfully!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void doExactRead(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"lab1");
            File cycledFile = new File(path, "cycled.txt");
            EditText aArray = findViewById(R.id.aArrayEditText);
            EditText bArray = findViewById(R.id.bArrayEditText);

            try {
                FileInputStream inputStream = new FileInputStream(cycledFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                aArray.setText(bufferedReader.readLine());
                bArray.setText(bufferedReader.readLine());
                inputStream.close();
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doFileThings(String thing) {
        switch (thing) {
            case "save":
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    doExactSave();
                } else {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "This permission is needed to save results.", Toast.LENGTH_SHORT)
                                .show();
                    }

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                }
                break;

            case "read":
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    doExactRead();
                } else {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "This permission is needed to save results.", Toast.LENGTH_SHORT)
                                .show();
                    }

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        if (requestCode == REQUEST_WRITE_STORAGE){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doExactSave();

            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == REQUEST_READ_STORAGE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doExactRead();

            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();

            }
        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycled);

        Button algBtn = findViewById(R.id.cycledAlgBtn);
        Button calcBtn = findViewById(R.id.cycledCalcBtn);
        Button saveBtn = findViewById(R.id.cycledFileSaveBtn);
        Button readBtn = findViewById(R.id.cycledFileCalcBtn);

        algBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cycledIntent = new Intent(getApplicationContext(), AlgImageActivity.class);
                cycledIntent.putExtra("com.myprogs.labsapp.ALG_TYPE", "cycled");
                startActivity(cycledIntent);
            }
        });
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText aArrayEditText = findViewById(R.id.aArrayEditText);
                EditText bArrayEditText = findViewById(R.id.bArrayEditText);
                TextView resultView = findViewById(R.id.cycledResultTextview);
                String result;

                if (!(aArrayEditText.getText().toString().isEmpty()
                        || bArrayEditText.getText().toString().isEmpty())){

                    String[] aString = aArrayEditText.getText().toString().split(",");
                    float[] aFloat = new float[aString.length];
                    for (int i = 0; i < aString.length; i++) {
                        aFloat[i] = Float.parseFloat(aString[i].trim());
                    }

                    String[] bString = bArrayEditText.getText().toString().split(",");
                    float[] bFloat = new float[bString.length];
                    for (int j = 0; j < bString.length; j++) {
                        bFloat[j] = Float.parseFloat(bString[j].trim());
                    }

                    float lowerSum = 0;
                    float leftMult = 1;
                    float rightSum = 0;
                    for (float anAFloat : aFloat) {
                        for (float aBFloat : bFloat) {
                            lowerSum += anAFloat + aBFloat;
                            rightSum += aBFloat;
                        }
                        leftMult *= anAFloat;
                    }
                    rightSum /= aFloat.length;

                    if (lowerSum == 0) {
                        result = "Division by zero!";
                    }
                    else {
                        result = "f = " + String.valueOf(((leftMult + rightSum) / lowerSum)) ;
                    }
                }
                else {
                    result = "Not enough data!";
                }
                resultView.setText(result);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                EditText aArray = findViewById(R.id.aArrayEditText);
                EditText bArray = findViewById(R.id.bArrayEditText);

                if (!(aArray.getText().toString().isEmpty() || bArray.getText().toString().isEmpty())) {
                    doFileThings("save");

                } else {
                    Toast.makeText(getApplicationContext(), "Not enough data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "lab1/cycled.txt");
                if (file.exists()) {
                    doFileThings("read");

                } else {
                    Toast.makeText(getApplicationContext(), "Not enough data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
