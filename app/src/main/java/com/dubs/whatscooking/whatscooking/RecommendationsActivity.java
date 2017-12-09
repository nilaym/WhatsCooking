package com.dubs.whatscooking.whatscooking;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class RecommendationsActivity extends AppCompatActivity {
    private final static String RECOMMENDATIONS_FILENAME = "recommendations.json";

    private HashMap<String, HashMap<String, Integer>> history;
    private HashMap<String, ArrayList<String>> dishToRecs;

    public HashMap<String, String> reccToSource;

    private class GetRecommendationsAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... passing) {
            String baseYummlyUrl = "https://api.yummly.com/v1/api/recipes";

            try {
                System.out.println("LENGTH OF HISTORY: " + RecommendationsActivity.this.history.keySet().size());
                Iterator<String> it = RecommendationsActivity.this.history.keySet().iterator();
                while (it.hasNext()) {
                    String restaurantName = it.next();
                    Set<String> dishes = RecommendationsActivity.this.history.get(restaurantName).keySet();
                    for (String dish : dishes) {
                        String queryArgs = "";
                        for (String namePart : dish.split(" ")) {
                            queryArgs += namePart + "+";
                        }
                        queryArgs = queryArgs.substring(0, queryArgs.length()-1);
                        System.out.println("QUERY:" + queryArgs);
                        // Create URL
                        URL yummlyEndpoint = new URL(baseYummlyUrl + "?q=" + queryArgs);
                        System.out.println("FULL URL: " + baseYummlyUrl + "?q=" + queryArgs);
                        HttpsURLConnection myConnection =
                                (HttpsURLConnection) yummlyEndpoint.openConnection();
                        // Request Headers
                        myConnection.setRequestProperty("User-Agent", "whats-cooking-v0.1");
                        myConnection.setRequestProperty("X-Yummly-App-ID", "18a94cf5");
                        myConnection.setRequestProperty("X-Yummly-App-Key", "1631dceffd5445513e8982e9d47431b7");
                        if (myConnection.getResponseCode() == 200) {
                            InputStream responseBody = myConnection.getInputStream();
                            JSONObject yummlyResponse = JsonReader.readJsonFromFile(responseBody);

                            // Get a list of match objects. Convert those to a list of IDs
                            JSONArray matches = (JSONArray) yummlyResponse.get("matches");
                            ArrayList<String> matchids = new ArrayList<>();
                            for (int i = 0; i < matches.length(); i++) {
                                JSONObject match = (JSONObject) matches.get(i);
                                matchids.add((String) match.get("id"));
                            }

                            // For each ID, execute another API request
                            for (int i = 0; i < matchids.size(); i++) {
                                String endpointUrl = "https://api.yummly.com/v1/api/recipe/" + matchids.get(i);
                                URL yummlyIdEndpoint = new URL(endpointUrl);
                                HttpsURLConnection idConnection =
                                        (HttpsURLConnection) yummlyIdEndpoint.openConnection();
                                // Request Headers
                                idConnection.setRequestProperty("User-Agent", "whats-cooking-v0.1");
                                idConnection.setRequestProperty("X-Yummly-App-ID", "18a94cf5");
                                idConnection.setRequestProperty("X-Yummly-App-Key", "1631dceffd5445513e8982e9d47431b7");

                                if (idConnection.getResponseCode() == 200) {
                                    InputStream idResponseBody = idConnection.getInputStream();
                                    JSONObject yummlyIdResponse = JsonReader.readJsonFromFile(idResponseBody);

//                                RecommendationsActivity.prettyPrintJson(yummlyIdResponse.toString());
                                    String recipeName = (String) yummlyIdResponse.get("name");
                                    String sourceUrl = (String) ((JSONObject) yummlyIdResponse.get("source")).get("sourceRecipeUrl");
                                    RecommendationsActivity.this.reccToSource.put(recipeName, sourceUrl);
                                }
                            }
                        } else {
                            System.out.println("YOU SUCK");
                        }
                    }
                }
            } catch(MalformedURLException e) {
                System.out.println("malformed url!");
            } catch(IOException e) {
                e.printStackTrace();
                System.out.println("wtf io exception thrown");
            } catch(JSONException e) {
                e.printStackTrace();
                System.out.println("yayayayaya fuck json exceptions");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListView lv = findViewById(R.id.recs_list_view);
            Set<String> reccNamesSet = RecommendationsActivity.this.reccToSource.keySet();
            ArrayList<String> reccNames = new ArrayList<>();
            Iterator<String> it = reccNamesSet.iterator();
            while (it.hasNext()) {
                reccNames.add(it.next());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RecommendationsActivity.this,
                    R.layout.content_restaurant_menu, reccNames)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    View row = super.getView(position, convertView, parent);
                    switch (position % 2)
                    {
                        case 0:
                            row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.recA));
                            break;
                        case 1:
                            row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.recB));
                            break;
//                        case 2:
//                            row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec3));
//                            break;
//                        case 3:
//                            row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec4));
//                            break;
//                        case 4:
//                            row.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.rec5));
//                            break;
                    }
                    return row;
                }
            };
            System.out.println("WE FINISHED");
            System.out.println(reccNames.toString());
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String itemClicked = (String) adapterView.getItemAtPosition(i);
                    String url = RecommendationsActivity.this.reccToSource.get(itemClicked);

                    Intent webViewIntent = new Intent(RecommendationsActivity.this, WebViewActivity.class);
                    webViewIntent.putExtra("url", url);
                    webViewIntent.putExtra("recipe", itemClicked);
                    startActivity(webViewIntent);
                }
            });
        }
    }

    public static void prettyPrintJson(String jsonStr) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonStr);
        String prettyJsonString = gson.toJson(je);
        System.out.println(prettyJsonString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.recs_toolbae);
        setSupportActionBar(toolbar);

        try {
            FileInputStream fis = openFileInput(JsonReader.HISTORY_FILENAME);
            JSONObject obj = JsonReader.readJsonFromFile(fis);
            this.history = JsonReader.readHistoryMapFromJson(obj);
        }
        catch(FileNotFoundException e) {
            this.history = new HashMap<>();
            // create file
            File f = new File(this.getApplicationContext().getFilesDir(), JsonReader.HISTORY_FILENAME);

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

        this.reccToSource = new HashMap<>();

        try {
            FileInputStream fis = openFileInput(JsonReader.HISTORY_FILENAME);
            JSONObject obj = JsonReader.readJsonFromFile(fis);
            history = JsonReader.readHistoryMapFromJson(obj);
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

        GetRecommendationsAsync asyncTask = new GetRecommendationsAsync();
        asyncTask.execute();
    }
}
