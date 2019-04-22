package com.myprogs.labsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.myprogs.labsapp.InterGraphicsActivity.interpolate;

public class Lab3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);

        Button makeGraphicsBtn = findViewById(R.id.lab3GraphicsBtn);
        Button makeSinGrphBtn = findViewById(R.id.sinIntrBtn);
        Button calcButton = findViewById(R.id.lab3CalcBtn);

        makeGraphicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InterGraphicsActivity.class);
                intent.putExtra("com.myprogs.labsapp.GRAPH_TYPE", "usual");
                startActivity(intent);
            }
        });

        makeSinGrphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InterGraphicsActivity.class);
                intent.putExtra("com.myprogs.labsapp.GRAPH_TYPE", "sin");
                startActivity(intent);
            }
        });

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText xEditText = findViewById(R.id.lab3XEditText);
                TextView yIntrTextView = findViewById(R.id.lab3IntrResultTextView);
                TextView yTheorTextView = findViewById(R.id.lab3TheorResultTextView);
                TextView yDevTextView = findViewById(R.id.lab3DeviationTextView);

                double x = Double.parseDouble(xEditText.getText().toString());
                double y_calc = interpolate(x);
                double y_theor = Math.pow(Math.sin(x), 3) + 3 * Math.pow(Math.cos(x), 2);
                double deviation = Math.abs(y_calc - y_theor);

                yIntrTextView.setText("Інтерполяційне: " + y_calc);
                yTheorTextView.setText("Теоретичне: " + y_theor);
                yDevTextView.setText("Похибка: " + deviation);
            }
        });
    }
}
