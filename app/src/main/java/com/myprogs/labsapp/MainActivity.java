package com.myprogs.labsapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView labsListView;
    String[] labs;
    String[] themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Resources res = getResources();
        labsListView = findViewById(R.id.labsListView);
        labs = res.getStringArray(R.array.labs);
        themes = res.getStringArray(R.array.themes);

        ItemAdapter itemAdapter = new ItemAdapter(this, labs, themes);
        labsListView.setAdapter(itemAdapter);

        labsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent lab1Intent = new Intent(getApplicationContext(), Lab1Activity.class);
                        startActivity(lab1Intent);
                        break;

                    case 1:
                        Intent lab2Intent = new Intent(getApplicationContext(), Lab2Activity.class);
                        startActivity(lab2Intent);
                        break;

                    case 2:
                        Intent lab3Intent = new Intent(getApplicationContext(), Lab3Activity.class);
                        startActivity(lab3Intent);
                        break;

                    case 3:
                        Intent lab4Intent = new Intent(getApplicationContext(), Lab4Activity.class);
                        startActivity(lab4Intent);
                        break;
                    case 4:
                        Intent lab5Intent = new Intent(getApplicationContext(), Lab5Activity.class);
                        startActivity(lab5Intent);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent infoIntent = new Intent(getApplicationContext(), StudentInfoActivity.class);
            startActivity(infoIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
