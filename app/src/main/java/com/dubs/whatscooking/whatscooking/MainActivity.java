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

        final Button buttonPandaExpress = findViewById(R.id.b_panda);
        buttonPandaExpress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RestaurantMenuActivity.class);
                intent.putExtra("NAME", "PandaExpress");
                startActivity(intent);
            }
        });

        final Button buttonPizzaHouse = findViewById(R.id.b_pizza);
        buttonPizzaHouse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RestaurantMenuActivity.class);
                intent.putExtra("NAME", "PizzaHouse");
                startActivity(intent);
            }
        });

        final Button buttonChipotle = findViewById(R.id.b_chipotle);
        buttonChipotle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RestaurantMenuActivity.class);
                intent.putExtra("NAME", "Chipotle");
                startActivity(intent);
            }
        });

        final Button buttonPiada = findViewById(R.id.b_piada);
        buttonPiada.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RestaurantMenuActivity.class);
                intent.putExtra("NAME", "Piada");
                startActivity(intent);
            }
        });
    }
}
