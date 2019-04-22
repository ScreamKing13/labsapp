package com.myprogs.labsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Lab1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);

        Button linearBtn = findViewById(R.id.linearBtn);
        Button ramifiedBtn = findViewById(R.id.ramifiedBtn);
        Button cycledBtn = findViewById(R.id.cycledBtn);

        linearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), LinearActivity.class);
                startActivity(startIntent);
            }
        });

        ramifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), RamifiedActivity.class);
                startActivity(startIntent);
            }
        });

        cycledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), CycledActivity.class);
                startActivity(startIntent);
            }
        });
    }
}
