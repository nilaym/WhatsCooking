package com.dubs.whatscooking.whatscooking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Nilay on 11/5/17.
 */

public class JsonReader {
    public static String HISTORY_FILENAME = "history.json";

    public static HashMap<String, ArrayList<String>> readMapFromJson(JSONObject obj) {
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        System.out.println("OBJ IS: " + obj.toString());

        Iterator<String> it = obj.keys();
        while (it.hasNext()) {
            String key = it.next();
            ArrayList<String> items = new ArrayList<String>();
            try {
                JSONArray menuArr = obj.getJSONArray(key);
                for (int i = 0; i < menuArr.length(); i++) {
                    items.add(menuArr.getString(i));
                }
            }
            catch (JSONException e) {
                System.out.println("yoloswag");
            }
            map.put(key, items);
        }

        return map;
    }

    public static HashMap<String, HashMap<String, Integer>> readHistoryMapFromJson(JSONObject restaurantJson) {
        HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>> ();
        Iterator<String> restaurantIt = restaurantJson.keys();
        while (restaurantIt.hasNext()) {
            String key = restaurantIt.next();
            HashMap<String, Integer> restaurantDishes = new HashMap<String, Integer>();
            try {
                JSONObject dishCounts = restaurantJson.getJSONObject(key);
                Iterator<String> dishesIt = dishCounts.keys();
                while (dishesIt.hasNext()) {
                    String dishName = dishesIt.next();
                    restaurantDishes.put(dishName, dishCounts.getInt(dishName));
                }
            }
            catch (JSONException e) {
                System.out.println("JSONException occurred in readMapFromJson for history!");
            }
            map.put(key, restaurantDishes);
        }

        return map;
    }

    public static JSONObject readJsonFromFile(InputStream fis) throws IOException, JSONException {
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        // TODO: inefficient. Look into StringBuilder
        String jsonText = "";
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            jsonText += line;
        }
        if(jsonText.isEmpty()) {
            throw new FileNotFoundException();
        }
        JSONObject obj = new JSONObject(jsonText);

        return obj;
    }
}
