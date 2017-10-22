package com.example.athidya.mydataapp;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.example.athidya.mydataapp.R.id.spinner10;
//import static com.example.athidya.mydataapp.R.id.spinner11;
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


    final GMTeamInfo[] globalGmTeamInfos = new GMTeamInfo[14];
    public String[] statnames = {"FGA", "FGM", "FTA", "FTM", "3PTM", "PTS", "REB", "AST", "ST", "BLK","TO"};
    String URL_START = "https://fantasysports.yahooapis.com";
    String USER_INFO_URL = URL_START + "/fantasy/v2/users;use_login=1/games/leagues";
    String access_token = "";
    String matchup_response = "";
    JSONObject playercount = null;
    JSONArray players = null;
    JSONArray playerstats = null;
    JSONObject player = null;
    JSONObject name = null;
    String playerid = "";
    String [] teamOrder = new String[20]; ///automate count of this by pulling matchups: count,
    /*
      Initializing objects to see the stats on screen; to be called later and information entered into
    */
    Spinner timeSpinner;
    Spinner teamSpinner;
    Spinner statSpinner;

    List<String> timeOptions;
    List<String> teamOptions;
    List<String> statOptions;

    Integer teamcountager;
    RequestQueue mrequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle info = getIntent().getExtras();
        access_token = info.getString("access_token");
        Log.d("access token",access_token);

        //Initializing objects to see the stats on screen; to be called later and information entered into
        timeSpinner = (Spinner) findViewById(spinner13);
        teamSpinner = (Spinner) findViewById(spinner10);
        statSpinner = (Spinner) findViewById(spinner12);

        timeOptions = new ArrayList<String>();
        teamOptions = new ArrayList<String>();
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

        for(int i = 0; i<statnames.length; i++) {
            statOptions.add(statnames[i]);
        }
        ArrayAdapter<String> statdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statOptions);
        statdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statSpinner.setAdapter(statdataAdapter);

        new MyAsyncTask().execute();


        /*Button button = (Button) findViewById(button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMatchups();
            }
        });*/
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            Log.d("pre thread", "here first");

        }


        protected Void doInBackground(Void... params) {
            Log.d("thread start", "i got here");
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            initialStatView(queue);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;

        }



        protected void onPostExecute(Void avoid) {
            Log.d("thread end", "here");
            viewMatchups();
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
        public void getResponse(final RequestQueue queue, String url){

            StringRequest strrequest = new StringRequest(Request.Method.GET, url,
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
                            pullTeamData(queue, gameID, league_ID, teamCount);
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
            queue.add(strrequest);
        }


        //Pull second set of Data, runs after the first request has been pushed
        public void pullTeamData(RequestQueue queue, String gamesID, String leagueID, final int teamCount) {

            String teamsUrl = URL_START + "/fantasy/v2/games;game_keys="+gamesID+"/leagues;league_keys="+leagueID+"/teams;team_keys="+leagueID+".t.1";
            String matchupURL = "";
            final RequestQueue queue2request = queue;
            RequestFuture<String> future = RequestFuture.newFuture();
            matchupRequest(queue, gamesID, leagueID, teamCount);

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
                            //Log.d("json", responseJSON.toString());

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
                                //Log.d("json", "Player Key = " + info[0]);
                                //Log.d("json", "First Name = " + info[1]);
                                //Log.d("json", "Last Name = " + info[2]);
                                playerInfo[i]=new PlayerInfo(info);
                                //put somewhere else because all the damn players showing up at once fool
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

        //gets team matchups information from volley request
        public void matchupRequest(final RequestQueue queue, final String gameID, final String leagueID, final int teamCount) {
            String url = URL_START + "/fantasy/v2/users;use_login=1/games;game_keys=" + gameID + "/leagues;league_keys=" + leagueID + "/teams/matchups" ;
            JSONArray tempJSONArray;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("matchup", getMatchupInfo(response));
                            matchup_response = response;
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

        //matchup information handled here and stored in string array of teamkeys in the order of matchup against
        //logged in player
        public String getMatchupInfo(String response) {
            JSONObject tempJSON = convertToJSON(response);
            JSONArray tempJSONArray;
            JSONArray temp2JSONArray;
            try {
                tempJSON = tempJSON.getJSONObject("fantasy_content");
                tempJSON = tempJSON.getJSONObject("users");
                tempJSON = tempJSON.getJSONObject("user");
                tempJSON = tempJSON.getJSONObject("games");
                tempJSON = tempJSON.getJSONObject("game");
                tempJSON = tempJSON.getJSONObject("leagues");
                tempJSON = tempJSON.getJSONObject("league");
                tempJSON = tempJSON.getJSONObject("teams");
                tempJSON = tempJSON.getJSONObject("team");
                teamOrder[0] = tempJSON.get("team_key").toString();
                tempJSON = tempJSON.getJSONObject("matchups");
                tempJSONArray = tempJSON.getJSONArray("matchup");
                for(int i=1; i<tempJSONArray.length(); i++) {
                    tempJSON = tempJSONArray.getJSONObject(i-1);
                    tempJSON = tempJSON.getJSONObject("teams");
                    temp2JSONArray = tempJSON.getJSONArray("team");
                    tempJSON = temp2JSONArray.getJSONObject(1);
                    teamOrder[i] = tempJSON.get("team_key").toString();
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        /* for (int i=1;i<teamCount+1;i++){
            teamsUrl = URL_START + "/fantasy/v2/games;game_keys="+gamesID+"/leagues;league_keys="+leagueID+"/teams;team_keys="+leagueID+".t."+i+"/roster";
            addToQueue(teamsUrl,queue2request, gamesID, leagueID);
        }*/
            // for (int i=0; i<globalGmTeamInfos.length; i++) {

            //  }

            return teamOrder.toString();
        }

        //handles set up to get the individual player stats using the team keys
        public void getPlayerStats(GMTeamInfo GMTeam, RequestQueue queue, String gamesID, String leagueID ,String teamkey) {
            for(int i=0; i<GMTeam.players.length; i++) {
                String STATS_URL = URL_START + "/fantasy/v2/player/" + GMTeam.players[i].getPlayerid() + "/stats" ;
                playerStatsReq(STATS_URL, queue, GMTeam, i);
            }
        }

        //volley request to get individual player stats info
        public void playerStatsReq(String url, RequestQueue queue, final GMTeamInfo GMTeam, final int i){

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //privateLogger(response);
                            //Log.d("Data call back", response);
                            setPlayersStats(response, GMTeam, i);
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

        //info handled here
        public void setPlayersStats(String response, GMTeamInfo GMTeam, int i) {

            JSONObject tempJSON = convertToJSON(response);
            JSONObject temp2JSON;
            JSONArray tempJSONArray;
            String tempString;
            try {
                tempJSON = tempJSON.getJSONObject("fantasy_content");
                tempJSON = tempJSON.getJSONObject("player");
                tempJSON = tempJSON.getJSONObject("player_stats");
                tempJSON = tempJSON.getJSONObject("stats");
                tempJSONArray = tempJSON.getJSONArray("stat");
                GMTeam.players[i].fieldGoalAttempts = convertwCheck(tempJSONArray.getJSONObject(3).getString("value"));
                GMTeam.players[i].fieldGoalMade = convertwCheck(tempJSONArray.getJSONObject(4).getString("value"));
                GMTeam.players[i].freeThrowAttempts = convertwCheck(tempJSONArray.getJSONObject(6).getString("value"));
                GMTeam.players[i].freeThrowMade=convertwCheck(tempJSONArray.getJSONObject(7).getString("value"));
                GMTeam.players[i].threePointsMade=convertwCheck(tempJSONArray.getJSONObject(10).getString("value"));
                GMTeam.players[i].points=convertwCheck(tempJSONArray.getJSONObject(12).getString("value"));
                GMTeam.players[i].rebounds=convertwCheck(tempJSONArray.getJSONObject(15).getString("value"));
                GMTeam.players[i].assists=convertwCheck(tempJSONArray.getJSONObject(16).getString("value"));
                GMTeam.players[i].steals=convertwCheck(tempJSONArray.getJSONObject(17).getString("value"));
                GMTeam.players[i].blocks=convertwCheck(tempJSONArray.getJSONObject(18).getString("value"));
                GMTeam.players[i].turnovers=convertwCheck(tempJSONArray.getJSONObject(19).getString("value"));
                //Log.d("set outcome", tempJSONArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //just to help converting players stats json
        public Integer convertwCheck(String temp) {
            if(temp.equals("-")) {
                return 0;
            }
            else {
                return Integer.parseInt(temp);}
        }

    }


    //Display set up
    public void viewMatchups() {
        TableLayout t1 = (TableLayout) findViewById(R.id.main_table);
        TableRow tr_head = new TableRow(this);
        tr_head.setId(View.generateViewId());
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        //create a column
        TextView label_TeamName = new TextView(this);
        label_TeamName.setId(View.generateViewId());
        label_TeamName.setText("Team Name");
        label_TeamName.setTextColor(Color.BLACK);
        label_TeamName.setPadding(5, 5, 5, 5);
        tr_head.addView(label_TeamName); //add to table row

        //create stats columns
        String[] statsnames = {"FGA", "FGM", "FG%", "3PM", "FTA", "FTM", "FT%", "PTS", "REB", "AST", "ST", "BLK",  "TO"};
        TextView[] label_stats = new TextView[statsnames.length];
        for (int i = 0; i<statsnames.length; i++) {
            label_stats[i] = new TextView(this);
            label_stats[i].setId(i+112);
            label_stats[i].setText(statsnames[i]);
            label_stats[i].setTextColor(Color.BLACK);
            label_stats[i].setPadding(5, 5, 5, 5);
            tr_head.addView(label_stats[i]);
        }
        /* TextView label_stats = new TextView(this);
        label_stats.setId(View.generateViewId());
        label_stats.setText("FGA  FGM  FG%  3PM  FTA  FTM  FT%  PTS  REB  AST ST  BLK  TO");
        label_stats.setTextColor(Color.BLACK);
        label_stats.setPadding(5, 5, 5, 5);
        tr_head.addView(label_stats); */

        t1.addView(tr_head, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextView[] teamNames = new TextView[teamOrder.length];
        TextView[][] teamStats = new TextView[teamOrder.length][statsnames.length];
        TableRow[] tr_list = new TableRow[teamOrder.length];


        for (int i = 1; i<teamOrder.length; i++) {
            tr_list[i] = new TableRow(this);
            tr_list[i].setId(i+1);
            tr_list[i].setBackgroundColor(Color.WHITE);
            tr_list[i].setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            teamNames[i] = new TextView(this);
            teamNames[i].setId(i+111);
            teamNames[i].setTextColor(Color.BLACK);
            teamNames[i].setPadding(5, 5, 5, 5);

            for(int j = 0; j<globalGmTeamInfos.length; j++) {
                if(teamOrder[i-1].equals(globalGmTeamInfos[j].teamKey)) {
                    globalGmTeamInfos[j].CalculateTotal();
                    teamNames[i].setText(globalGmTeamInfos[j].teamName);

                    String [] stats = globalGmTeamInfos[j].getStats();
                    tr_list[i].addView(teamNames[i]);
                    for (int k=0; k<statsnames.length; k++) {
                        teamStats[i][k] = new TextView(this);
                        teamStats[i][k].setId(i+112);
                        teamStats[i][k].setTextColor(Color.BLACK);
                        teamStats[i][k].setPadding(5, 5, 5, 5);
                        teamStats[i][k].setText(stats[k]);
                        tr_list[i].addView(teamStats[i][k]);
                    }



                }
            }
            t1.addView(tr_list[i]);
        }
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
            tempJSON = tempJSONArray.getJSONObject(1);
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
            tempJSON = tempJSONArray.getJSONObject(1);
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
            tempJSON = tempJSONArray.getJSONObject(1);
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
