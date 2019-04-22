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

public class LinearActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 112;
    public static final int REQUEST_READ_STORAGE = 113;

    public void doExactSave(){
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"lab1");
            File linearFile = new File(path, "linear.txt");
            EditText xEditText = findViewById(R.id.xLinearEditText);
            EditText aEditText = findViewById(R.id.linearAEditText);
            EditText bEditText = findViewById(R.id.linearBEditText);

            try {
                path.mkdir();
                linearFile.createNewFile();
                OutputStream output = new FileOutputStream(linearFile);
                String data = xEditText.getText() + "\n" + aEditText.getText() + "\n"
                        + bEditText.getText();
                byte[] byteData = data.getBytes();
                output.write(byteData);
                output.close();
                Toast.makeText(getApplicationContext(), "Data Saved Successfully!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("SMTH");
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
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "lab1");
            File linearFile = new File(path, "linear.txt");
            EditText xEditText = findViewById(R.id.xLinearEditText);
            EditText aEditText = findViewById(R.id.linearAEditText);
            EditText bEditText = findViewById(R.id.linearBEditText);

            try {
                FileInputStream inputStream = new FileInputStream(linearFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                xEditText.setText(bufferedReader.readLine());
                aEditText.setText(bufferedReader.readLine());
                bEditText.setText(bufferedReader.readLine());
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
        setContentView(R.layout.activity_linear);

        Button calcBtn = findViewById(R.id.linearCalcBtn);
        Button algBtn = findViewById(R.id.linearAlgBtn);
        final Button linearSaveBtn = findViewById(R.id.linearFileSaveBtn);
        Button linearCalcBtn = findViewById(R.id.linearFileCalcBtn);

        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText xLinear = findViewById(R.id.xLinearEditText);
                EditText aLinear = findViewById(R.id.linearAEditText);
                EditText bLinear = findViewById(R.id.linearBEditText);
                TextView resultView = findViewById(R.id.linearResultTextView);
                String result;

                if (!(xLinear.getText().toString().isEmpty()
                        || aLinear.getText().toString().isEmpty()
                        || bLinear.getText().toString().isEmpty())){
                    float x = Float.parseFloat(xLinear.getText().toString());
                    float a = Float.parseFloat(aLinear.getText().toString());
                    float b = Float.parseFloat(bLinear.getText().toString());
                    if (b != 0 & x > 0){
                        result = "Y1 = " + String.valueOf((float) (Math.sin(a / b) + Math.pow(Math.sin(a / b), 2)
                                + Math.cos(Math.pow(x, 2)) + Math.cos(Math.sqrt(x))));
                    }
                    else if (b == 0) {
                        result = "Division by zero!";
                    }
                    else if (x < 0) {
                        result  = "Can't get sqrt from negative!";
                    }
                    else {
                        result = "Unknown input";
                    }
                }
                else {
                    result = "Not enough data!";
                }
                resultView.setText(result);
            }
        });

        algBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent linearIntent = new Intent(getApplicationContext(), AlgImageActivity.class);
                linearIntent.putExtra("com.myprogs.labsapp.ALG_TYPE", "linear");
                startActivity(linearIntent);
            }
        });

        linearSaveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                EditText xEditText = findViewById(R.id.xLinearEditText);
                EditText aEditText = findViewById(R.id.linearAEditText);
                EditText bEditText = findViewById(R.id.linearBEditText);

                if (!(xEditText.getText().toString().isEmpty()
                        || aEditText.getText().toString().isEmpty()
                        || bEditText.getText().toString().isEmpty())) {
                    doFileThings("save");
                } else {
                    Toast.makeText(getApplicationContext(), "Not enough data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linearCalcBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "lab1/linear.txt");

                if (file.exists()) {
                    doFileThings("read");
                } else {
                    Toast.makeText(getApplicationContext(), "Not enough data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
