package com.myprogs.labsapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class AlgImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alg_image);

        Resources resources = getResources();
        ImageView algImageView = findViewById(R.id.AlgimageView);
        Intent imageIntent = getIntent();
        String algType = imageIntent.getStringExtra("com.myprogs.labsapp.ALG_TYPE");
        switch (algType) {
            case "linear":
                algImageView.setImageDrawable(resources.getDrawable(R.drawable.linear_alg));
                break;
            case "cycled":
                algImageView.setImageDrawable(resources.getDrawable(R.drawable.cycled_alg));
                break;
            case "ramified":
                algImageView.setImageDrawable(resources.getDrawable(R.drawable.ramified_alg));
                break;
        }

    }
}
