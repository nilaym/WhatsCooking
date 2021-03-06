package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantMenuActivity extends AppCompatActivity {
    private static HashMap<String, ArrayList<String>> nameToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        Bundle bundle = getIntent().getExtras();
        final String restaurantName = bundle.getString("NAME");
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbae);
        toolbar.setTitle(restaurantName.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2"));
        setSupportActionBar(toolbar);

//        // TODO: FOR TESTING!!!
//        File dir = getFilesDir();
//        File file = new File(dir, JsonReader.HISTORY_FILENAME);
//        file.delete();

        // Read in the menu via menu.json
        JSONObject menuObj = null;
        try {
            InputStream is = this.getApplicationContext().getAssets().open("menu.json");
            menuObj = JsonReader.readJsonFromFile(is);
        }
        catch (FileNotFoundException e) {
            System.out.println("hello");
        }
        catch (IOException e) {
            System.out.println("world");
        }
        catch (JSONException e) {
            System.out.println("yay");
        }

        nameToMenu = JsonReader.readMapFromJson(menuObj);

        System.out.println(restaurantName);
        System.out.println(nameToMenu.get(restaurantName));

        ListView lv = findViewById(R.id.menu_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.content_restaurant_menu, nameToMenu.get(restaurantName))
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row = super.getView(position, convertView, parent);
                switch (position % 2)
                {
                    case 0:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.resA));
                        break;
                    case 1:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.resB));
                        break;
//                    case 2:
//                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.res3));
//                        break;
//                    case 3:
//                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.res4));
//                        break;
//                    case 4:
//                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.res5));
//                        break;
                }
                return row;
            }
        };
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                String dishName = (String) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), SuccessConfirmRating.class);
                intent.putExtra("Restaurant", restaurantName);
                intent.putExtra("Dish", dishName);
                startActivity(intent);
            }
        });
    }
}
