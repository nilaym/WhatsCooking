package com.dubs.whatscooking.whatscooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
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

        List<Integer> thumbIds = new ArrayList<>(Arrays.asList(R.id.chiptole_thumb,
                                                        R.id.no_thai_thumb, R.id.panda_express_thumb,
                                                        R.id.piada_thumb, R.id.pizza_house_thumb));

        final List<String> extras = new ArrayList<>(Arrays.asList("Chipotle", "NoThai",
                                                            "PandaExpress", "Piada",
                                                            "PizzaHouse"));

        for (int i = 0; i < thumbIds.size(); i++) {
            final BootstrapCircleThumbnail thumbnail = findViewById(thumbIds.get(i));
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
