package com.dubs.whatscooking.whatscooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonNoThai = findViewById(R.id.b_nothai);
        buttonNoThai.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RestaurantMenuActivity.class);
                intent.putExtra("NAME", "NoThai");
                startActivity(intent);
            }
        });
    }
}
