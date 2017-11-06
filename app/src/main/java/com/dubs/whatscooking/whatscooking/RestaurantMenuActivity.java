package com.dubs.whatscooking.whatscooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RestaurantMenuActivity extends AppCompatActivity {
    private static HashMap<String, ArrayList<String>> nameToMenu;
    private static HashMap<String, HashMap<String, Integer>> history;

//    private HashMap<String, ArrayList<String>> readMapFromJson(JSONObject obj) {
//        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
//        System.out.println("OBJ IS: " + obj.toString());
//
//        Iterator<String> it = obj.keys();
//        while (it.hasNext()) {
//            String key = it.next();
//            ArrayList<String> items = new ArrayList<String>();
//            try {
//                JSONArray menuArr = obj.getJSONArray(key);
//                for (int i = 0; i < menuArr.length(); i++) {
//                    items.add(menuArr.getString(i));
//                }
//            }
//            catch (JSONException e) {
//                System.out.println("yoloswag");
//            }
//            map.put(key, items);
//        }
//
//        return map;
//    }
//
//    private HashMap<String, HashMap<String, Integer>> readHistoryMapFromJson(JSONObject restaurantJson) {
//        HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>> ();
//        Iterator<String> restaurantIt = restaurantJson.keys();
//        while (restaurantIt.hasNext()) {
//            String key = restaurantIt.next();
//            HashMap<String, Integer> restaurantDishes = new HashMap<String, Integer>();
//            try {
//                JSONObject dishCounts = restaurantJson.getJSONObject(key);
//                Iterator<String> dishesIt = dishCounts.keys();
//                while (dishesIt.hasNext()) {
//                    String dishName = dishesIt.next();
//                    restaurantDishes.put(dishName, dishCounts.getInt(dishName));
//                }
//            }
//            catch (JSONException e) {
//                System.out.println("JSONException occurred in readMapFromJson for history!");
//            }
//            map.put(key, restaurantDishes);
//        }
//
//        return map;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO: FOR TESTING!!!
//        File dir = getFilesDir();
//        File file = new File(dir, JsonReader.HISTORY_FILENAME);
//        file.delete();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        Bundle bundle = getIntent().getExtras();
        final String restaurantName = bundle.getString("NAME");

        System.out.println(restaurantName);
        System.out.println(nameToMenu.get(restaurantName));

        TextView restaurantNameText = (TextView) findViewById(R.id.restaurant_name);
        restaurantNameText.setText(restaurantName);

        ListView lv = findViewById(R.id.menu_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.content_restaurant_menu, nameToMenu.get(restaurantName));
        lv.setAdapter(arrayAdapter);
        final Context CTX = this.getApplicationContext();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                String dishName = (String) adapter.getItemAtPosition(position);

                // read in history
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
                history.get(restaurantName).put(dishName, history.get(restaurantName).get(dishName) + 1);
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
