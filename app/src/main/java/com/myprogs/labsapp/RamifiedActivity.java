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

public class RamifiedActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 112;
    public static final int REQUEST_READ_STORAGE = 113;

    public void doExactSave(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"lab1");
            File ramifiedFile = new File(path, "ramified.txt");
            EditText rEditText = findViewById(R.id.rEditText);
            EditText bEditText = findViewById(R.id.bEditText);
            EditText cEditText = findViewById(R.id.cEditText);

            try {
                path.mkdir();
                ramifiedFile.createNewFile();
                OutputStream output = new FileOutputStream(ramifiedFile);
                String data = rEditText.getText() + "\n" + bEditText.getText() + "\n"
                        + cEditText.getText();
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
            File ramifiedFile = new File(path, "ramified.txt");
            EditText rEditText = findViewById(R.id.rEditText);
            EditText bEditText = findViewById(R.id.bEditText);
            EditText cEditText = findViewById(R.id.cEditText);

            try {
                FileInputStream inputStream = new FileInputStream(ramifiedFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                rEditText.setText(bufferedReader.readLine());
                bEditText.setText(bufferedReader.readLine());
                cEditText.setText(bufferedReader.readLine());
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
        setContentView(R.layout.activity_ramified);

        Button algBtn = findViewById(R.id.ramAlgBtn);
        Button calcBtn = findViewById(R.id.ramCalcBtn);
        Button saveBtn = findViewById(R.id.ramFileSaveBtn);
        Button readBtn = findViewById(R.id.ramFileCalcBtn);
        algBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ramIntent = new Intent(getApplicationContext(), AlgImageActivity.class);
                ramIntent.putExtra("com.myprogs.labsapp.ALG_TYPE", "ramified");
                startActivity(ramIntent);
            }
        });
        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText rRamified = findViewById(R.id.rEditText);
                EditText bRamified = findViewById(R.id.bEditText);
                EditText cRamified = findViewById(R.id.cEditText);
                TextView resultView = findViewById(R.id.ramResultTextView);
                String result;

                if (!(rRamified.getText().toString().isEmpty()
                        || bRamified.getText().toString().isEmpty()
                        || cRamified.getText().toString().isEmpty())){
                    float r = Float.parseFloat(rRamified.getText().toString());
                    float b = Float.parseFloat(bRamified.getText().toString());
                    float c = Float.parseFloat(cRamified.getText().toString());
                    if(r > 0) {
                        result = "y = " + String.valueOf((float) (Math.PI * Math.pow(r, 2) / (2 * Math.PI * r + 21 * r)));
                    }
                    else if (r < 0) {
                        result = "y = " + String.valueOf((float) ((Math.pow(c, 2) + Math.pow(b, 2)) / (Math.PI * Math.pow(r, 2))));
                    }
                    else if (r == 0){
                        result = "Division by zero!";
                    }
                    else{
                        result = "Unknown input";
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
                EditText rEditText = findViewById(R.id.rEditText);
                EditText bEditText = findViewById(R.id.bEditText);
                EditText cEditText = findViewById(R.id.cEditText);

                if (!(rEditText.getText().toString().isEmpty()
                        || bEditText.getText().toString().isEmpty()
                        || cEditText.getText().toString().isEmpty())) {
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
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "lab1/ramified.txt");

                if (file.exists()) {
                    doFileThings("read");
                } else {
                    Toast.makeText(getApplicationContext(), "Not enough data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
