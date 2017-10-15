package com.example.athidya.mydataapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.athidya.mydataapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.*;


/*
* remember to display the teams as the current users first, next the team he will be facing in the next week
* and then in order underneath
* as the weeks change the order it should be viewed in should also change
* so make entries to match the current week
* also default view should be current weeks line up
* */
public class StatViewerActivity extends AppCompatActivity {

    String access_token = "";
    JSONObject playercount = null;
    JSONArray players = null;
    JSONArray playerstats = null;
    JSONObject player = null;
    JSONObject name = null;
    String playerid = "";
    String[] stats = new String[28];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle info = getIntent().getExtras();
        access_token = info.getString("access_token");
        Log.d("access token",access_token);
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        initialStatView(queue);
    }



    public void initialStatView(RequestQueue queue) {

        String URL_START = "https://fantasysports.yahooapis.com";

        String USER_INFO_URL = URL_START + "/fantasy/v2/users;use_login=1/games";
        getResponse(queue,USER_INFO_URL); // figure out how we can get something back from this function
        //String games_id // we need to run commandgetGamesId and put in the url above then we add it to find the next information....
        String playerQueryUrl = URL_START + "/fantasy/v2/users;use_login=1/games;game_keys=375/players/stats";
        String leagueQueryUrl = URL_START + "/fantasy/v2/users;use_login=1/games;game_keys=375/leagues";
        String StestQueryUrl = URL_START + "/fantasy/v2/users;use_login=1/games;game_keys=375/leagues;league_key=375.1.27726/teams";
        String workingtestQueryUrl = URL_START + "/fantasy/v2/games;game_keys=364/leagues;league_keys=364.l.46339/teams";
        String testQueryUrl = URL_START + "/fantasy/v2/games;game_keys=375/leagues;league_keys=375.1.27726/teams";
        String TtestQueryUrl = URL_START + "/fantasy/v2/users;use_login=1/games;game_keys=364/leagues";

        //ABOVE alot of cleaning to do and organizing so we can get data through functions and automate the process for any user
        //fetch all teams from a league
        //http://fantasysports.yahooapis.com/fantasy/v2/league/{league_key}/teams

    }

    public void getResponse(RequestQueue queue, String queryUrl){

        RequestFuture<String> future = RequestFuture.newFuture();
        // Request a string response from the provided URL.

        StringRequest request2 = new StringRequest(Request.Method.POST,queryUrl,future,future);
        StringRequest request = new StringRequest(Request.Method.GET, queryUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        privateLogger(response);
                        //Log.d("Data call back", response);
                        JSONObject responseJSON = convertToJSON(response);
                        Log.d("json", responseJSON.toString());
                        Log.d("json", getGamesId(responseJSON).toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("data error","That didn't work!");
                error.printStackTrace();
            }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + access_token);
                return headers;
            }
        };



        // Add the request to the RequestQueue.
        queue.add(request2);
       /*try {
            String response = future.get(1, TimeUnit.SECONDS);
            Log.d("json",response);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
       }*/
    }

    public void privateLogger(String response){
        // displays all received information correctly in the Log without cutoff
        List<String> printlog = new ArrayList<String>();
        int index = 0;
        while (index < response.length()) {
            printlog.add(response.substring(index, Math.min(index + 2000, response.length())));
            index +=2000;
        }
        for (int i = 0; i<printlog.size(); i++) {
            Log.d("data call", printlog.get(i));
        }
    }

    public void responseParse(JSONObject response) {
        try {
            playercount = response.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("user").getJSONObject("games").getJSONObject("game").getJSONObject("players"); //to know the number of iterations
            String count = playercount.get("count").toString();
            players = playercount.getJSONArray("player");
            PlayerInfo[] playersList = new PlayerInfo[Integer.parseInt(count)];

            for (int i = 0; i< Integer.parseInt(count); i++) {
                player = players.getJSONObject(i);
                name = player.getJSONObject("name");
                playerid = player.get("player_id").toString();
                playerstats = player.getJSONObject("player_stats").getJSONObject("stats").getJSONArray("stat");
                playersList[i]  = new PlayerInfo(playerid, name.get("first").toString(), name.get("last").toString());
                for (int j = 0; j<28; j++) {
                    stats[j] = playerstats.getJSONObject(j).get("value").toString();
                    Log.d("stat", stats[j].toString());
                }
                Log.d("name", name.toString());
                Log.d("playerinfo", playersList[i].toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // This will retrieve the gameID
    public String getGamesId(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("user").getJSONObject("games");
            tempJSONArray = tempJSON.getJSONArray("game");
            tempJSON = tempJSONArray.getJSONObject(0);
            //tempJSON = response.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("user").getJSONObject("games").getJSONObject("game");
            tempString  = tempJSON.get("game_key").toString();
                //Log.d("name", tempString +"Hi its sath and this worked");
                return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject convertToJSON(String response){

        JSONObject jsonresponse = null;
        try {
            jsonresponse = XML.toJSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonresponse;
    }
}
