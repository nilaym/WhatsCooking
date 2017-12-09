package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbae);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Restaurant Selector");

        List<Integer> buttonIds = new ArrayList<>(Arrays.asList(R.id.chipotle_button,
                                                        R.id.no_thai_button, R.id.panda_express_button,
                                                        R.id.piada_button, R.id.pizza_house_button));

        final List<String> extras = new ArrayList<>(Arrays.asList("Chipotle", "NoThai",
                                                            "PandaExpress", "Piada",
                                                            "PizzaHouse"));

        for (int i = 0; i < buttonIds.size(); i++) {
            final Button thumbnail = findViewById(buttonIds.get(i));
            final int index = i;  // copying into final b/c of access in onClick()
            thumbnail.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), RestaurantMenuActivity.class);
                    intent.putExtra("NAME", extras.get(index));
                    startActivity(intent);
                }
            });
            thumbnail.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            v.setAlpha(.5f);
        } else {
            v.setAlpha(1f);
        }

        return false;
    }
}
