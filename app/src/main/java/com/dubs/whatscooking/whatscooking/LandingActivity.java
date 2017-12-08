package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.landing_toolbae);
        setSupportActionBar(toolbar);

        Button buttonEatOut = findViewById(R.id.b_eat);
        Button buttonCookHome = findViewById(R.id.b_cook);

        buttonEatOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        buttonCookHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to recipe rec activity.
                Intent intent = new Intent(getBaseContext(), RecommendationsActivity.class);
                startActivity(intent);
            }
        });
    }
}
