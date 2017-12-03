package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

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

//            InputStream is = this.getApplicationContext().getAssets().open("recommendations.json");
//            JSONObject recsObj = JsonReader.readJsonFromFile(is);
//            dishToRecs = JsonReader.readMapFromJson(recsObj);
//            System.out.println(dishToRecs.toString());
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

//        ArrayList<String> names = new ArrayList<String>();
//        for(String restaurantName : history.keySet()) {
//            for (String dish : history.get(restaurantName).keySet()) {
//                System.out.println(dish);
//                names.add(dishToRecs.get(dish).get(0));
//            }
//        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Create URL
                try {
                    URL yummlyEndpoint = new URL("https://api.yummly.com/v1/api/recipes?q=onion+soup");
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) yummlyEndpoint.openConnection();
                    myConnection.setRequestProperty("User-Agent", "whats-cooking-v0.1");
                    myConnection.setRequestProperty("X-Yummly-App-ID", "18a94cf5");
                    myConnection.setRequestProperty("X-Yummly-App-Key", "1631dceffd5445513e8982e9d47431b7");
//                    String queryParam = "q=onion";
//                    myConnection.setDoOutput(true);
//                    myConnection.getOutputStream().write(queryParam.getBytes());
                    if (myConnection.getResponseCode() == 200) {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JSONObject yummlyResponse = JsonReader.readJsonFromFile(responseBody);

                        System.out.println(yummlyResponse);
                        JSONArray matches = (JSONArray) yummlyResponse.get("matches");
                        ArrayList<String> matchids = new ArrayList<>();
                        for (int i = 0; i < matches.length(); i++) {
                            // do nothing
                        }
                    } else {
                        System.out.println("YOU SUCK");
                    }
                } catch(MalformedURLException e) {
                    System.out.println("malformed url!");
                } catch(IOException e) {
                    System.out.println("wtf io exception thrown");
                } catch(JSONException e) {
                    System.out.println("yayayayaya fuck json exceptions");
                }
            }
        });


//        ListView lv = findViewById(R.id.recs_list_view);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
//                R.layout.content_restaurant_menu, names);
//        lv.setAdapter(arrayAdapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String itemClicked = (String) adapterView.getItemAtPosition(i);
//
//                // search for the associated URL in dishToRecs
//                for (String key : dishToRecs.keySet()) {
//                    if (dishToRecs.get(key).get(0).equals(itemClicked)) {
//                        String url = dishToRecs.get(key).get(1);
//                        Intent webViewIntent = new Intent(getBaseContext(), WebViewActivity.class);
//                        webViewIntent.putExtra("url", url);
//                        startActivity(webViewIntent);
//                    }
//                }
//            }
//        });
    }
}
