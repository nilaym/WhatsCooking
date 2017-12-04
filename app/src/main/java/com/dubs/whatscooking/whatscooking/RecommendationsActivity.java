package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class RecommendationsActivity extends AppCompatActivity {
    private final static String RECOMMENDATIONS_FILENAME = "recommendations.json";

    private HashMap<String, HashMap<String, Integer>> history;
    private HashMap<String, ArrayList<String>> dishToRecs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);


        try {
            FileInputStream fis = openFileInput(JsonReader.HISTORY_FILENAME);
            JSONObject obj = JsonReader.readJsonFromFile(fis);
            history = JsonReader.readHistoryMapFromJson(obj);

            InputStream is = this.getApplicationContext().getAssets().open("recommendations.json");
            JSONObject recsObj = JsonReader.readJsonFromFile(is);
            dishToRecs = JsonReader.readMapFromJson(recsObj);
            System.out.println(dishToRecs.toString());
        }
        catch (FileNotFoundException e) {
            System.out.println("you shouldn't access this until history is totally set");
        }
        catch (IOException e) {
            System.out.println("ioexception in recommendation activity?");
        }
        catch (JSONException e) {
            System.out.println("im tired of exceptions");
        }

        ArrayList<String> names = new ArrayList<String>();
        for(String restaurantName : history.keySet()) {
            for (String dish : history.get(restaurantName).keySet()) {
                System.out.println(dish);
                names.add(dishToRecs.get(dish).get(0));
            }
        }

        ListView lv = findViewById(R.id.recs_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.content_restaurant_menu, names)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row = super.getView(position, convertView, parent);
                switch (position % 5)
                {
                    case 0:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec1));
                        break;
                    case 1:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec2));
                        break;
                    case 2:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec3));
                        break;
                    case 3:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec4));
                        break;
                    case 4:
                        row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec5));
                        break;
                }
                return row;
            }
        };
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemClicked = (String) adapterView.getItemAtPosition(i);

                // search for the associated URL in dishToRecs
                for (String key : dishToRecs.keySet()) {
                    if (dishToRecs.get(key).get(0).equals(itemClicked)) {
                        String url = dishToRecs.get(key).get(1);
                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setData(Uri.parse(url));
                        //startActivity(intent);

                        Intent webViewIntent = new Intent(getBaseContext(), WebViewActivity.class);
                        webViewIntent.putExtra("url", url);
                        startActivity(webViewIntent);
                    }
                }
            }
        });
    }
}
