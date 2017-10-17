package com.example.athidya.mydataapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.*;

import static com.example.athidya.mydataapp.R.id.spinner10;
import static com.example.athidya.mydataapp.R.id.spinner11;
import static com.example.athidya.mydataapp.R.id.spinner12;
import static com.example.athidya.mydataapp.R.id.spinner13;


/*
* remember to display the teams as the current users first, next the team he will be facing in the next week
* and then in order underneath
* as the weeks change the order it should be viewed in should also change
* so make entries to match the current week
* also default view should be current weeks line up
* */
public class StatViewerActivity extends AppCompatActivity {


    String URL_START = "https://fantasysports.yahooapis.com";
    String USER_INFO_URL = URL_START + "/fantasy/v2/users;use_login=1/games/leagues";

    String access_token = "";
    JSONObject playercount = null;
    JSONArray players = null;
    JSONArray playerstats = null;
    JSONObject player = null;
    JSONObject name = null;
    String playerid = "";
    String[] stats = new String[28];

    GMTeamInfo[] globalGmTeamInfos = new GMTeamInfo[14];

    public String[] statnames = {"GP", "GS", "MIN", "FGA", "FGM", "FG%", "FTA", "FTM", "FT%", "3PTA",
            "3PTM", "3PT%", "PTS", "OREB", "DREB", "REB", "AST", "ST", "BLK","TO",
            "A/T", "PF", "DISQ", "TECH", "EJCT", "FF", "MPG", "DD", "TD"};

    /*
      Initializing objects to see the stats on screen; to be called later and information entered into
    */
    Spinner timeSpinner;
    Spinner teamSpinner;
    Spinner playerSpinner;
    Spinner statSpinner;

    List<String> timeOptions;
    List<String> teamOptions;
    List<String> playerOptions;
    List<String> statOptions;

    TextView[] statsDisplay;
    Integer teamcountager;

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

        //Initializing objects to see the stats on screen; to be called later and information entered into
        timeSpinner = (Spinner) findViewById(spinner13);
        teamSpinner = (Spinner) findViewById(spinner10);
        playerSpinner = (Spinner) findViewById(spinner11);
        statSpinner = (Spinner) findViewById(spinner12);

        timeOptions = new ArrayList<String>();
        teamOptions = new ArrayList<String>();
        playerOptions = new ArrayList<String>();
        statOptions = new ArrayList<String>();

        /*
        time and stat spinners options are constant so initialized and updated here, other spinners vary so they will be initialized
        here but updated when teams and players info have been collected from our requests
         */
        timeOptions.add("This Week");
        timeOptions.add("Last Week");
        timeOptions.add("Last Month");
        timeOptions.add("Last Season");



        ArrayAdapter<String> timedataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeOptions);
        timedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timedataAdapter);

        ArrayAdapter<String> teamdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamOptions);
        teamdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamdataAdapter);

        ArrayAdapter<String> playerdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerOptions);
        playerdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(playerdataAdapter);


        for(int i = 0; i<stats.length; i++) {
            statOptions.add(statnames[i]);
        }
        ArrayAdapter<String> statdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statOptions);
        statdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statSpinner.setAdapter(statdataAdapter);
    }



    public void initialStatView(RequestQueue queue) {


        getResponse(queue,USER_INFO_URL); // figure out how we can get something back from this function

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

    //Gets the first Response on the league informationa and etc
    public void getResponse(RequestQueue queue, String queryUrl){

        final String[] resp = new String[1];
        final RequestQueue queue2 = queue;
        RequestFuture<String> future = RequestFuture.newFuture();
        // Request a string response from the provided URL.

        StringRequest request = new StringRequest(Request.Method.GET, queryUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //privateLogger(response);
                        //Log.d("Data call back", response);
                        JSONObject responseJSON = convertToJSON(response);
                        Log.d("json", responseJSON.toString());
                        String gameID =  getGamesId(responseJSON).toString();
                        String league_ID = getLeagueId(responseJSON).toString();
                        int teamCount = Integer.parseInt(getTeamCount(responseJSON));
                        teamcountager = teamCount;

                        Log.d("json", "GAME_ID = " +gameID);
                        Log.d("json", "LEAGUE_ID = " + league_ID);
                        Log.d("json", "team_count = " + teamCount);
                        pullTeamData(queue2,gameID,league_ID,teamCount);
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
        queue.add(request);
    }

    //Pull second set of Data, runs after the first request has been pushed
    public void pullTeamData(RequestQueue queue, String gamesID, String leagueID, final int teamCount) {

        String teamsUrl = URL_START + "/fantasy/v2/games;game_keys="+gamesID+"/leagues;league_keys="+leagueID+"/teams;team_keys="+leagueID+".t.1";
        String statsUrl = "";
        final RequestQueue queue2request = queue;
        RequestFuture<String> future = RequestFuture.newFuture();
        // Request a string response from the provided URL.
        for (int i=1;i<teamCount+1;i++){
            teamsUrl = URL_START + "/fantasy/v2/games;game_keys="+gamesID+"/leagues;league_keys="+leagueID+"/teams;team_keys="+leagueID+".t."+i+"/roster";
            addToQueue(teamsUrl,queue2request, gamesID, leagueID);
        }
    }

    //Adds data to get put into the queue to store all gm and team member information helps pullAllData
    public void addToQueue(final String url, final RequestQueue queue, final String gamesID, final String leagueID){

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //privateLogger(response);
                        //Log.d("Data call back", response);
                        JSONObject responseJSON = convertToJSON(response);
                        Log.d("json", responseJSON.toString());

                        int teamID = Integer.parseInt(getTeamID(responseJSON));
                        Log.d("json", "This is the order  ================== "+teamID);
                        Log.d("json", "Team name = " + getTeamName(responseJSON));
                        int playerCount = Integer.parseInt(getPlayerCount(responseJSON));
                        Log.d("json", "Players Count = " + playerCount);


                        /*
                        save team names to spinner to allow user to choose specific teams info
                         */
                        teamOptions.add(getTeamName(responseJSON));

                        PlayerInfo[] playerInfo = new PlayerInfo[playerCount];
                        for (int i = 0; i<playerCount;i++)
                        {
                            String[] info = getPlayerInfo(responseJSON,i);
                            Log.d("json", "Player Key = " + info[0]);
                            Log.d("json", "First Name = " + info[1]);
                            Log.d("json", "Last Name = " + info[2]);
                            playerInfo[i]=new PlayerInfo(info);
                            //put somewhere else because all the damn players showing up at once fool
                            playerOptions.add(info[1] + " " + info[2]);
                        }
                        String teamKey = getTeamKey(responseJSON);
                        String teamName = getTeamName(responseJSON);
                        globalGmTeamInfos[teamID-1]=new GMTeamInfo(teamKey,teamName,playerInfo);
                        getPlayerStats(globalGmTeamInfos[teamID-1], queue, gamesID, leagueID, teamKey);
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

        queue.add(request);

    }


    public void getPlayerStats(GMTeamInfo GMTeam, RequestQueue queue, String gamesID, String leagueID ,String teamkey) {
        for(int i=0; i<GMTeam.players.length; i++) {
            String STATS_URL = "/fantasy/v2/player/" + GMTeam.players[i].getPlayerid() + "/stats" ;
            addtestreq(STATS_URL, queue);
        }
    }
    /*
    TESTINGGG
     */
    public void addtestreq(String url,RequestQueue queue){

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        privateLogger(response);
                        //Log.d("Data call back", response);
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

        queue.add(request);

    }







    //Will pull all the specific information and data for each player
   /* public void pullPlayerData(RequestQueue queue, GMTeamInfo gmTeamInfo) {

        String teamsUrl = URL_START + "/fantasy/v2/games;game_keys="+gamesID+"/leagues;league_keys="+leagueID+"/teams;team_keys="+leagueID+".t.1";
        final RequestQueue queue2request = queue;
        RequestFuture<String> future = RequestFuture.newFuture();
        // Request a string response from the provided URL.
        for (int i=1;i<teamCount+1;i++){
            teamsUrl = URL_START + "/fantasy/v2/games;game_keys="+gamesID+"/leagues;league_keys="+leagueID+"/teams;team_keys="+leagueID+".t."+i+"/roster";
            addToQueue(teamsUrl,queue2request);
        }
    }*/



    //Logs the response for us to see
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


    /*
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
*/

    //======================BELOW ARE THE JSON KEYS TO SEARCH FOR EACH TYPE OF OBJECT===============
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

    // This will retrieve the LeagueID
    public String getLeagueId(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("user").getJSONObject("games");
            tempJSONArray = tempJSON.getJSONArray("game");
            tempJSON = tempJSONArray.getJSONObject(0);
            //tempJSON = response.getJSONObject("fantasy_content").getJSONObject("users").getJSONObject("user").getJSONObject("games").getJSONObject("game");
            tempString  = tempJSON.getJSONObject("leagues").getJSONObject("league").get("league_key").toString();
            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get number of teams
    public String getTeamCount(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content");
            tempJSON = tempJSON.getJSONObject("users");
            tempJSON = tempJSON.getJSONObject("user");
            tempJSON = tempJSON.getJSONObject("games");
            tempJSONArray = tempJSON.getJSONArray("game");
            tempJSON = tempJSONArray.getJSONObject(0);
            tempJSON = tempJSON.getJSONObject("leagues");
            tempJSON = tempJSON.getJSONObject("league");
            tempString  = tempJSON.get("num_teams").toString();

            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get team name
    public String getTeamName(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content");
            tempJSON = tempJSON.getJSONObject("games");
            tempJSON = tempJSON.getJSONObject("game");
            tempJSON = tempJSON.getJSONObject("leagues");
            tempJSON = tempJSON.getJSONObject("league");
            tempJSON = tempJSON.getJSONObject("teams");
            tempJSON = tempJSON.getJSONObject("team");

            tempString  = tempJSON.get("name").toString();

            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get team key
    public String getTeamKey(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content");
            tempJSON = tempJSON.getJSONObject("games");
            tempJSON = tempJSON.getJSONObject("game");
            tempJSON = tempJSON.getJSONObject("leagues");
            tempJSON = tempJSON.getJSONObject("league");
            tempJSON = tempJSON.getJSONObject("teams");
            tempJSON = tempJSON.getJSONObject("team");

            tempString  = tempJSON.get("team_key").toString();

            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get team key
    public String getTeamID(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content");
            tempJSON = tempJSON.getJSONObject("games");
            tempJSON = tempJSON.getJSONObject("game");
            tempJSON = tempJSON.getJSONObject("leagues");
            tempJSON = tempJSON.getJSONObject("league");
            tempJSON = tempJSON.getJSONObject("teams");
            tempJSON = tempJSON.getJSONObject("team");

            tempString  = tempJSON.get("team_id").toString();

            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get players count
    public String getPlayerCount(JSONObject response) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String tempString;
        try {
            tempJSON = response.getJSONObject("fantasy_content");
            tempJSON = tempJSON.getJSONObject("games");
            tempJSON = tempJSON.getJSONObject("game");
            tempJSON = tempJSON.getJSONObject("leagues");
            tempJSON = tempJSON.getJSONObject("league");
            tempJSON = tempJSON.getJSONObject("teams");
            tempJSON = tempJSON.getJSONObject("team");
            tempJSON = tempJSON.getJSONObject("roster");
            tempJSON = tempJSON.getJSONObject("players");

            tempString  = tempJSON.get("count").toString();

            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get playerkey,first name, last name
    public String[] getPlayerInfo(JSONObject response, int index) {
        JSONObject tempJSON;
        JSONArray tempJSONArray;
        String[] tempString=new String[3];
        try {
            tempJSON = response.getJSONObject("fantasy_content");
            tempJSON = tempJSON.getJSONObject("games");
            tempJSON = tempJSON.getJSONObject("game");
            tempJSON = tempJSON.getJSONObject("leagues");
            tempJSON = tempJSON.getJSONObject("league");
            tempJSON = tempJSON.getJSONObject("teams");
            tempJSON = tempJSON.getJSONObject("team");
            tempJSON = tempJSON.getJSONObject("roster");
            tempJSON = tempJSON.getJSONObject("players");
            tempJSONArray = tempJSON.getJSONArray("player");
            tempJSON = tempJSONArray.getJSONObject(index);
            tempString[0]  = tempJSON.get("player_key").toString();
            tempString[1]  = tempJSON.getJSONObject("name").get("first").toString();
            tempString[2]  = tempJSON.getJSONObject("name").get("last").toString();

            return tempString;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Converts XML to JSON
    public JSONObject convertToJSON(String response){

        JSONObject jsonresponse = null;
        try {
            jsonresponse = XML.toJSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonresponse;
    }

    //==============================================================================================



}
