package com.dubs.whatscooking.whatscooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by SamGuan on 11/27/2017.
 */

public class SuccessConfirmRating extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_rating);

        final Context CTX = this.getApplicationContext();
        Button mainMenu = findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                HashMap<String, HashMap<String, Integer>> history = null;

                Bundle bundle = getIntent().getExtras();
                final String restaurantName = bundle.getString("Restaurant");
                final String dishName = bundle.getString("Dish");

                System.out.println(restaurantName);
                System.out.println(dishName);


                try {
                    FileInputStream fis = openFileInput(JsonReader.HISTORY_FILENAME);
                    JSONObject obj = JsonReader.readJsonFromFile(fis);
                    history = JsonReader.readHistoryMapFromJson(obj);
                    System.out.println("HISTORY:" + history);
                }
                catch(FileNotFoundException e) {
                    history = new HashMap<String, HashMap<String,Integer>>();
                    // create file
                    File f = new File(CTX.getFilesDir(), JsonReader.HISTORY_FILENAME);

                    try {
                        f.createNewFile();
                    }
                    catch(IOException ex) {
                        System.out.print("ruh-roh there was an IOException within an exception! go fix it!");
                    }
                }
                catch(IOException e) {
                    System.out.print("ruh-roh there was an IOException! go fix it!");
                }
                catch(JSONException e) {
                    System.out.println("ruh-roh there was a JSON exception in readHistoryMapFromJSON");
                }

                // update history
                if (!history.containsKey(restaurantName)) {
                    history.put(restaurantName, new HashMap<String, Integer>());
                }

                if (!history.get(restaurantName).containsKey(dishName)) {
                    history.get(restaurantName).put(dishName, 0);
                }
                history.get(restaurantName).put(dishName, (int)((RatingBar)findViewById(R.id.meal_rating)).getRating());

                String userHistoryJson = "{";
                for (String key : history.keySet()) {
                    userHistoryJson += "\"" + key + "\":{";
                    for (String innerKey : history.get(key).keySet()) {
                        userHistoryJson += "\"" + innerKey + "\":" + history.get(key).get(innerKey) + ",";
                    }
                    // substring trailing comma out
                    userHistoryJson = userHistoryJson.substring(0, userHistoryJson.length() - 1);
                    userHistoryJson += "},";
                }
                // substring trailing comma out
                userHistoryJson = userHistoryJson.substring(0, userHistoryJson.length() - 1);
                userHistoryJson += "}";
                System.out.println("user history json: " + userHistoryJson);
                // write back history
                try {
                    FileOutputStream fos = openFileOutput(JsonReader.HISTORY_FILENAME, Context.MODE_PRIVATE);
                    fos.write(userHistoryJson.getBytes());
                    fos.close();
                }
                catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException in writing back history!");
                }
                catch (IOException e) {
                    System.out.println("IOException in writing back history!");
                }

                Intent intent = new Intent(getBaseContext(), LandingActivity.class);
                startActivity(intent);
            }
        });
    }
}
